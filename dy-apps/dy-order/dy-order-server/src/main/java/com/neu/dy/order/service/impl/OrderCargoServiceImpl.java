package com.neu.dy.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.order.entitiy.OrderCargo;
import com.neu.dy.order.mapper.OrderCargoMapper;
import com.neu.dy.order.service.OrderCargoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 货物服务实现类
 */
@Service
public class OrderCargoServiceImpl extends ServiceImpl<OrderCargoMapper, OrderCargo> implements OrderCargoService {

    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public OrderCargo saveSelective(OrderCargo orderCargo) {
        if (orderCargo.getId() != null) {
            this.baseMapper.updateByPrimaryKey(orderCargo);
        } else {
            orderCargo.setId(idGenerator.nextId(orderCargo) + "");
            this.baseMapper.insertSelective(orderCargo);
        }
        return orderCargo;
    }

    @Override
    public List<OrderCargo> findAll(String tranOrderId, String orderId) {
        LambdaQueryWrapper<OrderCargo> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(tranOrderId)) {
            queryWrapper.eq(OrderCargo::getTranOrderId, tranOrderId);
        }
        if (StringUtils.isNotEmpty(orderId)) {
            queryWrapper.eq(OrderCargo::getOrderId, orderId);
        }
        queryWrapper.orderBy(true, true, OrderCargo::getId);
        return baseMapper.selectList(queryWrapper);
    }
}
