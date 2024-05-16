package com.catering.mapper;

import com.github.pagehelper.Page;
import com.catering.dto.GoodsSalesDTO;
import com.catering.dto.OrdersPageQueryDTO;
import com.catering.entity.Orders;
import com.catering.vo.BusinessDataVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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


    Integer orderCout(Map map);
    @Select("select o.name , sum(o.number) number from sky_take_out.order_detail o , sky_take_out.orders od where o.order_id = od.id and od.status = #{status} and od.order_time > #{begin} and od.order_time < #{end} group by o.name order by number desc;")
    List<GoodsSalesDTO> topsReport(Map map);

    BusinessDataVO getBusinessDate(Map map);
}
