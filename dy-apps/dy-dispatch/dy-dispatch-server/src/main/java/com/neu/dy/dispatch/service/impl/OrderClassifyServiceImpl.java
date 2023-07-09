package com.neu.dy.dispatch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neu.dy.dispatch.mapper.OrderClassifyMapper;
import com.neu.dy.dispatch.service.OrderClassifyService;
import com.neu.dy.entity.OrderClassifyEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderClassifyServiceImpl extends ServiceImpl<OrderClassifyMapper, OrderClassifyEntity> implements OrderClassifyService {
    @Override
    public List<OrderClassifyEntity> findByJobLogId(String id) {
        return null;
    }
}
