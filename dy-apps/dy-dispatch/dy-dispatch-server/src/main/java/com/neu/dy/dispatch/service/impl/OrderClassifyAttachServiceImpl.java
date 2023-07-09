/**
 * Copyright (c) 2019 联智合创 All rights reserved.
 * <p>
 * http://www.witlinked.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.neu.dy.dispatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neu.dy.dispatch.mapper.OrderClassifyAttachMapper;
import com.neu.dy.dispatch.service.OrderClassifyAttachService;
import com.neu.dy.entity.OrderClassifyAttachEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderClassifyAttachServiceImpl extends ServiceImpl<OrderClassifyAttachMapper, OrderClassifyAttachEntity> implements OrderClassifyAttachService {

    @Override
    public List<OrderClassifyAttachEntity> findAttachByClassifyId(String classifyId) {

        LambdaQueryWrapper<OrderClassifyAttachEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderClassifyAttachEntity::getOrderClassifyId, classifyId);

        // 正序排列  从先到后
        wrapper.orderByAsc(OrderClassifyAttachEntity::getCreateDate);
        return this.baseMapper.selectList(wrapper);
    }
}