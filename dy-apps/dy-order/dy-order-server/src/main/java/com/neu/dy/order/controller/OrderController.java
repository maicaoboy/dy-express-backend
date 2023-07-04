package com.neu.dy.order.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neu.dy.base.R;
import com.neu.dy.order.dto.OrderDTO;
import com.neu.dy.order.entitiy.Order;
import com.neu.dy.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 生成一个新订单，需要计算运费
     * @param orderDTO
     * @return
     */
    @PostMapping("/save")
    public R save(@RequestBody OrderDTO orderDTO){
        log.info("保存订单信息：{}", JSON.toJSONString(orderDTO));
        Order order = new Order();
        //计算预计到达时间,两天
        order.setEstimatedArrivalTime(LocalDateTime.now().plus(2, ChronoUnit.DAYS));

        //调用sevice根据指定的规则计算运费
        Map map = orderService.calculateAmount(orderDTO);
        log.info("实时计算运费：{}",map);
        orderDTO = (OrderDTO) map.get("orderDto");
        BeanUtils.copyProperties(orderDTO,order);
        //输入地址有误，无法计算距离
        if("send error msg".equals(orderDTO.getSenderAddress()) || "receive error msg".equals(orderDTO.getReceiverAddress())){
            return R.success(orderDTO);
        }
        //如果amount为空的话，默认为23
        order.setAmount(new BigDecimal(map.getOrDefault("amount", "23").toString()));
        orderService.saveOrder(order);
        log.info("订单信息入库:{}", order);

        //返回order对象
        OrderDTO result = new OrderDTO();
        BeanUtils.copyProperties(order, result);
        return R.success(order);
    }

    /**
     * 修改订单信息
     * @param id
     * @param orderDTO
     * @return
     */
    @PostMapping("/update/{id}")
    public R updateById(@PathVariable(name = "id")String id, @RequestBody OrderDTO orderDTO){
        orderDTO.setId(id);
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO,order);
        orderService.updateById(order);
        return R.success(orderDTO);
    }

    /**
     * 分页查询
     * @param orderDTO
     * @return
     */
    @PostMapping("/page")
    public R findByPage(@RequestBody OrderDTO orderDTO){
        Order order= new Order();
        BeanUtils.copyProperties(orderDTO,order);
        //分页查询
        IPage<Order> page = orderService.findByPage(orderDTO.getPage(), orderDTO.getPageSize(), order);
        return R.success(page);
    }

    /**
     * 根据id查询某一订单
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R findById(@PathVariable(name = "id") String id){
        OrderDTO orderDTO = new OrderDTO();
        Order order = orderService.getById(id);

        if(order != null){
            BeanUtils.copyProperties(order,orderDTO);
        }else{
            orderDTO = null;
        }
        return R.success(orderDTO);
    }

    /**
     * 根据多个id获取多个订单，应该是给客户端用的
     * @param ids
     * @return
     */
    @GetMapping("ids")
    public R findByIds(@RequestParam(name = "ids")List<String> ids){
        List<Order> orders = orderService.listByIds(ids);
        List<OrderDTO> orderDTOList = orders.stream().map(item -> {
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(item, orderDTO);
            return orderDTO;
        }).collect(Collectors.toList());
        return R.success(orderDTOList);
    }

}
