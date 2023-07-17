package com.neu.dy.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.work.entity.TransportOrder;
import com.neu.dy.work.enums.transportorder.TransportOrderSchedulingStatus;
import com.neu.dy.work.enums.transportorder.TransportOrderStatus;
import com.neu.dy.work.mapper.TransportOrderMapper;
import com.neu.dy.work.service.TransportOrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 运单服务实现类
 * </p>
 */
@Service
public class TransportOrderServiceImpl extends ServiceImpl<TransportOrderMapper, TransportOrder> implements TransportOrderService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public TransportOrder saveTransportOrder(TransportOrder transportOrder) {
        transportOrder.setCreateTime(LocalDateTime.now());
        transportOrder.setId(idGenerator.nextId(transportOrder) + "");
        transportOrder.setStatus(TransportOrderStatus.CREATED.getCode());
        transportOrder.setSchedulingStatus(TransportOrderSchedulingStatus.TO_BE_SCHEDULED.getCode());
        save(transportOrder);
        return transportOrder;
    }

    @Override
    public IPage<TransportOrder> findByPage(Integer page, Integer pageSize, String id, String orderId, Integer status, Integer schedulingStatus) {
        Page<TransportOrder> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<TransportOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(id)) {
            lambdaQueryWrapper.like(TransportOrder::getId, id);
        }
        if (StringUtils.isNotEmpty(orderId)) {
            lambdaQueryWrapper.like(TransportOrder::getOrderId, orderId);
        }
        if (status != null) {
            lambdaQueryWrapper.eq(TransportOrder::getStatus, status);
        }
        if (schedulingStatus != null) {
            lambdaQueryWrapper.eq(TransportOrder::getSchedulingStatus, schedulingStatus);
        }
        return page(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<TransportOrder> findAll(List<String> ids, String orderId, Integer status, Integer schedulingStatus) {
        LambdaQueryWrapper<TransportOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(TransportOrder::getId, ids);
        }
        if (StringUtils.isNotEmpty(orderId)) {
            lambdaQueryWrapper.like(TransportOrder::getOrderId, orderId);
        }
        if (status != null) {
            lambdaQueryWrapper.eq(TransportOrder::getStatus, status);
        }
        if (schedulingStatus != null) {
            lambdaQueryWrapper.eq(TransportOrder::getSchedulingStatus, schedulingStatus);
        }
        return list(lambdaQueryWrapper);
    }

    @Override
    public TransportOrder findByOrderId(String orderId) {
        return getOne(new LambdaQueryWrapper<TransportOrder>().eq(TransportOrder::getOrderId, orderId));
    }
}
