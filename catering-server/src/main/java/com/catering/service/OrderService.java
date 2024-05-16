package com.catering.service;

import com.catering.dto.OrdersPageQueryDTO;
import com.catering.dto.OrdersPaymentDTO;
import com.catering.dto.OrdersSubmitDTO;
import com.catering.result.PageResult;
import com.catering.vo.OrderPaymentVO;
import com.catering.vo.OrderSubmitVO;

public interface OrderService {

    OrderSubmitVO orderSubmit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    PageResult pageQuery(int page, int pageSize, Integer status);

    void reminder(Long id);

    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);
}
