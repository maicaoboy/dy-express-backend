/**
 * Copyright (c) 2019 联智合创 All rights reserved.
 * <p>
 * http://www.witlinked.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.neu.dy.dispatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neu.dy.dispatch.mapper.CacheLineUseMapper;
import com.neu.dy.dispatch.service.CacheLineUseService;
import com.neu.dy.entity.CacheLineUseEntity;
import org.springframework.stereotype.Service;

@Service
public class CacheLineUseServiceImpl extends ServiceImpl<CacheLineUseMapper, CacheLineUseEntity> implements CacheLineUseService {

    @Override
    public CacheLineUseEntity getByOrderClassifyId(String id) {
        LambdaQueryWrapper<CacheLineUseEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CacheLineUseEntity::getOrderClassifyId, id);
        return this.baseMapper.selectOne(wrapper);
    }
}