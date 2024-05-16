package com.catering.controller.user;
import com.catering.dto.OrdersPaymentDTO;
import com.catering.dto.OrdersSubmitDTO;
import com.catering.result.PageResult;
import com.catering.result.Result;
import com.catering.service.OrderService;
import com.catering.vo.OrderSubmitVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;


    @PostMapping("/submit")
    public Result<OrderSubmitVO> orderSubmit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){

        OrderSubmitVO orderSubmitVO = orderService.orderSubmit(ordersSubmitDTO);

        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<String> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        return  Result.success();
    }

    /**
     * 历史订单
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    @GetMapping("/historyOrders")
    public Result<PageResult> pageQuery(int page,int pageSize,Integer status){
        PageResult pageResult = orderService.pageQuery(page,pageSize,status);
        return Result.success(pageResult);
    }
    @GetMapping("/reminder/{id}")
    public Result reminderOrder(@PathVariable Long id){
        orderService.reminder(id);

        return Result.success();
    }
}
