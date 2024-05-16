package com.catering.controller.admin;

import com.catering.dto.OrdersPageQueryDTO;
import com.catering.result.PageResult;
import com.catering.result.Result;
import com.catering.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/conditionSearch")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
       PageResult pageResult =  orderService.conditionSearch(ordersPageQueryDTO);
       return Result.success(pageResult);
    }

}
