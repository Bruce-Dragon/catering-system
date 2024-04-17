package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {


    void insertOrder(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from sky_take_out.orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    Page<Orders> pageQuery(Orders orders);

    @Select("select * from sky_take_out.orders where status = #{pendingPayment} and order_time = #{localDateTime}")
    List<Orders> timeout(Integer pendingPayment, LocalDateTime localDateTime);

    @Select("select * from sky_take_out.orders where status = #{deliveryInProgress} and order_time < #{localDateTime};")
    List<Orders> OrderTime(Integer deliveryInProgress, LocalDateTime localDateTime);

    @Select("select * from sky_take_out.orders where id = #{id}")
    Orders orderNumber(Long id);

    Page<Orders> select(OrdersPageQueryDTO ordersPageQueryDTO);
}
