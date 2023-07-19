package com.neu.dy.feign;

import com.neu.dy.base.R;
import com.neu.dy.order.dto.OrderDTO;
import com.neu.dy.order.dto.OrderSearchDTO;
import com.neu.dy.order.entitiy.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("dy-order-server")
@RequestMapping("/order")
public interface OrderFeign {
    /**
     * 根据条件查询订单
     * @param orderSearchDTO
     * @return
     */
    @PostMapping("/list")
    R<List<Order>> list(@RequestBody OrderSearchDTO orderSearchDTO);

    @PostMapping("/update/{id}")
    public R updateById(@PathVariable(name = "id")String id, @RequestBody OrderDTO orderDTO);
}
