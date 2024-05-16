package com.catering.task;


/*
* 定时任务类
* */
/*@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
    //定时处理订单超时
    //@Scheduled(cron = "0 * * * * ? ")
    @Scheduled(cron = "1/5 * * * * ?")
    public void processTimeoutOrder(){
        log.info("定时处理超时订单：{}", LocalDateTime.now());
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-15);
       List<Orders> orders = orderMapper.timeout(Orders.PENDING_PAYMENT,localDateTime);
       if (orders != null && orders.size() > 0){
           for (Orders order : orders) {
               order.setStatus(Orders.CANCELLED);
               order.setCancelReason("订单超时，自动取消");
               order.setCancelTime(LocalDateTime.now());
               orderMapper.update(order);
           }
       }
    }
    //@Scheduled(cron = "0 0 1 * * ? ")
    @Scheduled(cron = "0/5 * * * * ?")
    public void processDeliveryOrder(){
        log.info("定时处理超时派送订单:{}",LocalDateTime.now());
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-60);
        List<Orders> orders = orderMapper.OrderTime(Orders.DELIVERY_IN_PROGRESS,localDateTime);
        if (orders != null && orders.size() > 0){
            for (Orders order : orders) {
                order.setStatus(Orders.COMPLETED);
                order.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }
}*/
