package com.neu.dy.base.biz.service.truck.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.neu.dy.base.biz.dao.truck.DyTruckTypeMapper;
import com.neu.dy.base.biz.service.truck.IDyTruckTypeService;
import com.neu.dy.base.common.Constant;
import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.base.entity.truck.DyTruckType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class DyTruckTypeServiceImpl extends ServiceImpl<DyTruckTypeMapper, DyTruckType> implements IDyTruckTypeService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public DyTruckType saveTruckType(DyTruckType pdTruckType) {
        pdTruckType.setId(idGenerator.nextId(pdTruckType) + "");
        baseMapper.insert(pdTruckType);
        return pdTruckType;
    }

    @Override
    public IPage<DyTruckType> findByPage(Integer page, Integer pageSize, String name, BigDecimal allowableLoad,
                                         BigDecimal allowableVolume) {
        Page<DyTruckType> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<DyTruckType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            lambdaQueryWrapper.like(DyTruckType::getName, name);
        }
        if (allowableLoad != null) {
            lambdaQueryWrapper.eq(DyTruckType::getAllowableLoad, allowableLoad);
        }
        if (allowableVolume != null) {
            lambdaQueryWrapper.eq(DyTruckType::getAllowableVolume, allowableVolume);
        }
        lambdaQueryWrapper.eq(DyTruckType::getStatus, Constant.DATA_DEFAULT_STATUS);
        return baseMapper.selectPage(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<DyTruckType> findAll(List<String> ids) {
        LambdaQueryWrapper<DyTruckType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(DyTruckType::getId, ids);
        }
        lambdaQueryWrapper.eq(DyTruckType::getStatus, Constant.DATA_DEFAULT_STATUS);
        return baseMapper.selectList(lambdaQueryWrapper);
    }
}
