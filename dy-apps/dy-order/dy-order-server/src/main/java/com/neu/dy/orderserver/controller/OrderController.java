package com.neu.dy.orderserver.controller;

import com.alibaba.fastjson.JSON;
import com.neu.dy.base.R;
import com.neu.dy.order.dto.OrderDTO;
import com.neu.dy.order.entitiy.Order;
import com.neu.dy.orderserver.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/save")
    public R save(@RequestBody OrderDTO orderDTO){
        log.info("保存订单信息：{}", JSON.toJSONString(orderDTO));
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO,order);
        //计算预计到达时间,两天
        order.setEstimatedArrivalTime(LocalDateTime.now().plus(2, ChronoUnit.DAYS));

        return null;
    }

}
