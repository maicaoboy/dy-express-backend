package com.neu.dy.orderserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neu.dy.base.R;
import com.neu.dy.order.dto.OrderCargoDto;
import com.neu.dy.order.entitiy.OrderCargo;
import com.neu.dy.orderserver.service.OrderCargoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cargo")
public class OrderCargoController {
    @Autowired
    private OrderCargoService cargoService;

    /**
     * 查询某个订单或运单购买的商品，如果两个参数都没有的话就是获取所有购买的商品信息
     * @param tranOrderId
     * @param orderId
     * @return
     */
    public R findAll(@RequestParam(name = "tranOrderId",required = false)String tranOrderId, @RequestParam(name = "orderId",required = false)String orderId){
        List<OrderCargo> orderCargos = cargoService.findAll(tranOrderId, orderId);
        List<OrderCargoDto> orderCargoDtos = orderCargos.stream().map(orderCargo -> {
            OrderCargoDto orderCargoDto = new OrderCargoDto();
            BeanUtils.copyProperties(orderCargo, orderCargoDto);
            return orderCargoDto;
        }).collect(Collectors.toList());
        return R.success(orderCargoDtos);
    }

    /**
     * 根据多个orderId获取购买的商品信息
     * @param orderIds
     * @return
     */
    @GetMapping("/list")
    public R list(@RequestParam(name = "orderIds",required = false)List<String> orderIds){
        LambdaQueryWrapper<OrderCargo> queryWrapper = new LambdaQueryWrapper<>();

        if(orderIds == null || orderIds.size() == 0){
            return R.success();
        }
        queryWrapper.in(OrderCargo::getOrderId,orderIds);
        List<OrderCargo> orderCargos = cargoService.list(queryWrapper);
        List<OrderCargoDto> orderCargoDtos = orderCargos.stream().map(orderCargo -> {
            OrderCargoDto orderCargoDto = new OrderCargoDto();
            BeanUtils.copyProperties(orderCargo, orderCargoDto);
            return orderCargoDto;
        }).collect(Collectors.toList());
        return R.success(orderCargoDtos);
    }

    /**
     * 向购物车中添加商品,如果存在就修改对应的信息（例如数量），不存在就新增
     * @param orderCargoDto
     * @return
     */
    @PostMapping
    public R save(@RequestBody OrderCargoDto orderCargoDto){
        OrderCargo orderCargo = new OrderCargo();
        BeanUtils.copyProperties(orderCargoDto,orderCargo);
        orderCargo = cargoService.saveSelective(orderCargo);
        BeanUtils.copyProperties(orderCargo,orderCargoDto);
        return R.success(orderCargoDto);
    }

    /**
     * 更新货物信息
     * @param id
     * @param orderCargoDto
     * @return
     */
    @GetMapping("/{id}")
    public R update(@PathVariable(name = "id") String id, @RequestBody OrderCargoDto orderCargoDto){
        orderCargoDto.setId(id);
        OrderCargo orderCargo = new OrderCargo();
        BeanUtils.copyProperties(orderCargoDto,orderCargo);
        cargoService.updateById(orderCargo);
        return R.success(orderCargoDto);
    }

    /**
     * 删除货物信息
     *
     * @param id 货物id
     * @return 返回信息
     */
    @DeleteMapping("/{id}")
    public R del(@PathVariable(name = "id") String id) {
        cargoService.removeById(id);
        return R.success();
    }

    /**
     * 根据id获取货物详情
     *
     * @param id 货物id
     * @return 货物详情
     */
    @GetMapping("/{id}")
    public R findById(@PathVariable(name = "id") String id) {
        OrderCargo orderCargo = cargoService.getById(id);
        OrderCargoDto orderCargoDto = new OrderCargoDto();
        BeanUtils.copyProperties(orderCargo, orderCargoDto);
        return R.success(orderCargoDto);
    }

}
