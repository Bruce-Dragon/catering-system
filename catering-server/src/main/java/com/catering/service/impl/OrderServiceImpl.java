package com.catering.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.catering.constant.MessageConstant;
import com.catering.context.BaseContext;
import com.catering.dto.OrdersPageQueryDTO;
import com.catering.dto.OrdersPaymentDTO;
import com.catering.dto.OrdersSubmitDTO;
import com.catering.entity.*;
import com.catering.exception.OrderBusinessException;
import com.catering.mapper.*;
import com.catering.result.PageResult;
import com.catering.service.OrderService;
import com.catering.utils.WeChatPayUtil;
import com.catering.vo.OrderPaymentVO;
import com.catering.vo.OrderSubmitVO;
import com.catering.vo.OrderVO;
import com.catering.websocket.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    @Transactional
    public OrderSubmitVO orderSubmit(OrdersSubmitDTO ordersSubmitDTO) {
        Long currentId = BaseContext.getCurrentId();
        //业务异常检查，判断地址薄和购物车是否为空
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null){
            //抛出业务异常
            throw new OrderBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        List<ShoppingCart> list = shoppingCartMapper.list(currentId);
        if (list == null || list.size() == 0){
            //抛出业务异常
            throw new OrderBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        //向订单表插入一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setUserId(currentId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayMethod(1);
        orders.setPayStatus(0);
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orderMapper.insertOrder(orders);

        //向订单明细表插入数据
        for (ShoppingCart shoppingCart : list) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart,orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailMapper.insertOrderDetail(orderDetail);
        }
        //清空购物车数据
        shoppingCartMapper.delete(currentId);
        //封装VO数据返回结果
        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setOrderNumber(orders.getNumber());
        orderSubmitVO.setOrderTime(orders.getOrderTime());
        orderSubmitVO.setOrderAmount(orders.getAmount());
        orderSubmitVO.setId(orders.getId());

        return orderSubmitVO;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();
        orderMapper.update(orders);
        //通过websocket向客户端推送消息
        Map map = new HashMap();
        map.put("type",1);
        map.put("orderId",ordersDB.getId());
        map.put("content",outTradeNo);
        String toJSONString = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(toJSONString);

    }

    @Override
    public PageResult pageQuery(int page, int pageSize, Integer status) {
        PageHelper.startPage(page, pageSize);
        Orders orders = new Orders();
        orders.setUserId(BaseContext.getCurrentId());
        orders.setStatus(status);
        Page<Orders> ordersPage = orderMapper.pageQuery(orders);
        List<OrderVO> list = new ArrayList<>();
        if (ordersPage != null && ordersPage.size() > 0){
            for (Orders orders1 : ordersPage) {
                Long id = orders1.getId();
                List<OrderDetail> orderDetailList = orderDetailMapper.selectDetail(id);
                OrderVO orderVO = new OrderVO();
                orderVO.setOrderDetailList(orderDetailList);
                BeanUtils.copyProperties(orders1,orderVO);
                list.add(orderVO);
            }
        }
        return new PageResult(ordersPage.getTotal(),list);
    }

    @Override
    public void reminder(Long id) {
        Orders orders = orderMapper.orderNumber(id);
        String number = orders.getNumber();
        Map map = new HashMap();
        map.put("type",2);
        map.put("orderId",id);
        map.put("content",number);
        String toJSONString = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(toJSONString);

    }

    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
        Page<Orders> ordersList = orderMapper.select(ordersPageQueryDTO);
        List<OrderVO> orderVOList = new ArrayList<>();
        for (Orders orders1 : ordersList) {
            Long id = orders1.getId();
            List<OrderDetail> orderDetailList = orderDetailMapper.selectDetail(id);
            OrderVO orderVO = new OrderVO();
            orderVO.setOrderDetailList(orderDetailList);
            BeanUtils.copyProperties(orders1,orderVO);
            orderVOList.add(orderVO);
        }
        return new PageResult(ordersList.getTotal(),orderVOList);
    }
}
