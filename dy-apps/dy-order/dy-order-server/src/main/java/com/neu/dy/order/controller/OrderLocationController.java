package com.neu.dy.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.neu.dy.base.R;
import com.neu.dy.order.dto.OrderLocationDto;
import com.neu.dy.order.entitiy.OrderLocation;
import com.neu.dy.order.service.OrderLocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
@Api(value = "OrderLocation", tags = "订单位置")
public class OrderLocationController {

    @Autowired
    private OrderLocationService orderLocationService;

    /**
     * 保存或更新订单位置信息
     * @param orderLocationDto
     * @return
     */
    @PostMapping("saveOrUpdate")
    @ApiOperation("保存或更新订单位置信息")
    public R saveOrUpdateLocation(@RequestBody OrderLocationDto orderLocationDto){
        String id = orderLocationDto.getId();
        String orderId = orderLocationDto.getOrderId();
        if(StringUtils.isNotBlank(id)){
            //获取最后一次更新的订单位置数据
            LambdaQueryWrapper<OrderLocation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderLocation::getOrderId,orderId);
            queryWrapper.last(" limit 1 ");
            OrderLocation orderLocation = orderLocationService.getBaseMapper().selectOne(queryWrapper);

            OrderLocation newLocation = new OrderLocation();
            if(orderLocation != null){
                //修改
                BeanUtils.copyProperties(orderLocation,newLocation);
                newLocation.setId(orderLocation.getId());
                orderLocationService.updateById(newLocation);
            }else {
                //新增
                BeanUtils.copyProperties(orderLocation,newLocation);
                newLocation.setId(orderLocation.getId());
                orderLocationService.save(newLocation);
            }
            BeanUtils.copyProperties(newLocation,orderLocationDto);
        }
        return R.success(orderLocationDto);
    }

    /**
     * 根据订单id获取订单位置信息
     * @param orderId
     * @return
     */
    @GetMapping("orderId")
    @ApiOperation("根据订单id获取订单位置信息")
    public R selectByOrderId(@RequestParam(name = "orderId") String orderId){
        LambdaQueryWrapper<OrderLocation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderLocation::getOrderId,orderId);
        OrderLocation orderLocation = orderLocationService.getOne(queryWrapper);

        OrderLocationDto orderLocationDto = new OrderLocationDto();
        BeanUtils.copyProperties(orderLocation,orderLocationDto);
        return R.success(orderLocationDto);
    }

    /**
     * 删除订单位置信息
     * @param orderLocationDto
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除订单位置信息")
    public R delete(@RequestBody OrderLocationDto orderLocationDto){
        String orderId = orderLocationDto.getOrderId();
        int result = 0;
        if(StringUtils.isNotBlank(orderId)){
            LambdaUpdateWrapper<OrderLocation> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(OrderLocation::getOrderId,orderId);
            result = orderLocationService.getBaseMapper().delete(updateWrapper);
        }
        return R.success(result);
    }

}
