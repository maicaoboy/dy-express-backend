package com.neu.dy.dispatch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neu.dy.dispatch.mapper.OrderClassifyOrderMapper;
import com.neu.dy.dispatch.service.OrderClassifyOrderService;
import com.neu.dy.entity.OrderClassifyOrderEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderClassifyOrderServiceImpl extends ServiceImpl<OrderClassifyOrderMapper, OrderClassifyOrderEntity> implements OrderClassifyOrderService {
    @Override
    public String getClassifyId(String orderId) {
        return null;
    }
}
