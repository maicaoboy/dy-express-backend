package com.neu.dy.order.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neu.dy.base.R;
import com.neu.dy.feign.PickupDispatchTaskFeign;
import com.neu.dy.feign.TransportOrderFeign;
import com.neu.dy.feign.TransportTaskFeign;
import com.neu.dy.order.dto.OrderDTO;
import com.neu.dy.order.dto.OrderSearchDTO;
import com.neu.dy.order.entitiy.Order;
import com.neu.dy.order.service.OrderService;
import com.neu.dy.work.dto.TaskPickupDispatchDTO;
import com.neu.dy.work.dto.TaskTransportDTO;
import com.neu.dy.work.dto.TransportOrderDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
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
@Api(value = "Order", tags = "订单")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    PickupDispatchTaskFeign pickupDispatchTaskFeign;

    @Autowired
    TransportOrderFeign transportOrderFeign;



    /**
     * 生成一个新订单，需要计算运费
     * @param orderDTO
     * @return
     */
    @PostMapping("/save")
    @ApiOperation("保存订单信息")
    public R save(@RequestBody OrderDTO orderDTO){
        log.info("保存订单信息：{}", JSON.toJSONString(orderDTO));
        Order order = new Order();
        //计算预计到达时间
        Integer seconds = orderService.calculatetime(orderDTO);
        order.setEstimatedArrivalTime(LocalDateTime.now().plus(seconds, ChronoUnit.SECONDS));

        //调用sevice根据指定的规则计算运费
        Map map = orderService.calculateAmount(orderDTO);
        log.info("实时计算运费：{}",map);
        orderDTO = (OrderDTO) map.get("orderDto");
        BeanUtils.copyProperties(orderDTO,order);
        //输入地址有误，无法计算距离
        if("send error msg".equals(orderDTO.getSenderAddress()) || "receive error msg".equals(orderDTO.getReceiverAddress())){
            return R.success(orderDTO);
        }

        //Todo 修改为获取快递员的当前网点


        //获取订单当前网点
        String agencyId = orderService.caculateAgencyId(order);
        order.setCurrentAgencyId(agencyId);
        if(agencyId == null) return R.fail("当前网点不存在");
        //如果amount为空的话，默认为2
        order.setAmount(new BigDecimal(map.getOrDefault("amount", "23").toString()));
        orderService.saveOrder(order);
        log.info("订单信息入库:{}", order);

        //添加运单
        TransportOrderDTO transportOrderDTO = new TransportOrderDTO();
        transportOrderDTO.setOrderId(order.getId());
        transportOrderDTO.setStatus(1);
        transportOrderDTO.setSchedulingStatus(1);
        R resultsave = transportOrderFeign.save(transportOrderDTO);
        if(resultsave.getIsError()) {
            log.error("添加运单失败：{}", resultsave);
            log.info("重试添加运单");
            resultsave = transportOrderFeign.save(transportOrderDTO);
            if(resultsave.getIsError()) {
                orderService.removeById(order.getId());
                log.error("重试添加运单失败：{}", resultsave);
                return R.fail("添加运单失败");
            }
        }

        //添加取件任务单
        TaskPickupDispatchDTO taskPickupDispatchDTO = new TaskPickupDispatchDTO();
        taskPickupDispatchDTO.setOrderId(order.getId());
        taskPickupDispatchDTO.setAgencyId(agencyId);
        taskPickupDispatchDTO.setTaskType(1);
        taskPickupDispatchDTO.setStatus(1);
        taskPickupDispatchDTO.setSignStatus(1);
        taskPickupDispatchDTO.setEstimatedStartTime(LocalDateTime.now());
        taskPickupDispatchDTO.setAssignedStatus(1);
        R<TaskPickupDispatchDTO> save = pickupDispatchTaskFeign.save(taskPickupDispatchDTO);
        if(save.getIsError()) {
            log.error("添加取件任务单失败：{}", save);
            log.info("重试添加取件任务单");
            save = pickupDispatchTaskFeign.save(taskPickupDispatchDTO);
            if(save.getIsError()) {
                log.error("重试添加取件任务单失败：{}", save);
                return R.fail("添加取件任务单失败");
            }
            //删除订单
            orderService.removeById(order.getId());
            return R.fail("添加取件任务单失败");
        }


        //返回order对象
        OrderDTO result = new OrderDTO();
        BeanUtils.copyProperties(order, result);
        return R.success(result);
    }

    /**
     * 修改订单信息
     * @param id
     * @param orderDTO
     * @return
     */
    @ApiOperation("根据id更新订单信息")
    @PostMapping("/update/{id}")
    public R updateById(@PathVariable(name = "id")String id, @RequestBody OrderDTO orderDTO){
        orderDTO.setId(id);
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO,order);
        orderService.updateById(order);
        return R.success(orderDTO);
    }

    /**
     * 修改订单信息
     * @param orderDTO
     * @return
     */
    @PostMapping("/update")
    public R updateByIdNoId(@RequestBody OrderDTO orderDTO){
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
    @ApiOperation("分页查询订单信息")
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
    @ApiOperation("根据id查询订单信息")
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
    @ApiOperation("根据多个id查询订单信息")
    public R findByIds(@RequestParam(name = "ids")List<String> ids){
        List<Order> orders = orderService.listByIds(ids);
        List<OrderDTO> orderDTOList = orders.stream().map(item -> {
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(item, orderDTO);
            return orderDTO;
        }).collect(Collectors.toList());
        return R.success(orderDTOList);
    }

    /**
     * 根据条件查询订单
     * @param orderSearchDTO
     * @return
     */
    @PostMapping("list")
    @ApiOperation("根据条件查询订单信息")
    public R<List<Order>> list(@RequestBody OrderSearchDTO orderSearchDTO){
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(orderSearchDTO.getStatus() != null,Order::getStatus,orderSearchDTO.getStatus());
        queryWrapper.in(!CollectionUtils.isEmpty(orderSearchDTO.getReceiverCountyIds()),Order::getReceiverCountyId,orderSearchDTO.getReceiverCountyIds());
        queryWrapper.in(!CollectionUtils.isEmpty(orderSearchDTO.getSenderCountyIds()),Order::getSenderCountyId,orderSearchDTO.getSenderCountyIds());
        queryWrapper.eq(StringUtils.isNotEmpty(orderSearchDTO.getCurrentAgencyId()),Order::getCurrentAgencyId,orderSearchDTO.getCurrentAgencyId());

        List<Order> orderList = orderService.list(queryWrapper);
        return R.success(orderList);
    }

}
