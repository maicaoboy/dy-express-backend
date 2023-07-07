package com.neu.dy.order;

import com.neu.dy.order.dto.OrderDTO;
import com.neu.dy.order.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
//测试时将注释打开，并将启动类的@SpringBootApplication注释掉
//@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllertest {
    @Autowired
    private OrderService orderService;
    @Test
    public void testcalculatetime(){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setSenderAddress("东北大学浑南校区");
        orderDTO.setReceiverAddress("辽宁省沈阳市太原街万达广场");
        Integer calculatetime = orderService.calculatetime(orderDTO);
        System.out.println(
                "预计到达时间为："+calculatetime+"s"
        );
        return;
    }
}
