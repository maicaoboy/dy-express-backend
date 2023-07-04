package com.neu.dy.base.biz.service.truck.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.neu.dy.base.biz.dao.truck.DyTruckMapper;
import com.neu.dy.base.biz.service.truck.IDyTruckService;
import com.neu.dy.base.common.Constant;
import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.base.entity.truck.DyTruck;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DyTruckServiceImpl extends ServiceImpl<DyTruckMapper, DyTruck> implements IDyTruckService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public DyTruck saveTruck(DyTruck dyTruck) {
        dyTruck.setId(idGenerator.nextId(dyTruck) + "");
        baseMapper.insert(dyTruck);
        return dyTruck;
    }

    @Override
    public IPage<DyTruck> findByPage(Integer page, Integer pageSize, String truckTypeId, String licensePlate, String fleetId) {
        Page<DyTruck> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<DyTruck> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(licensePlate)) {
            lambdaQueryWrapper.like(DyTruck::getLicensePlate, licensePlate);
        }
        if (StringUtils.isNotEmpty(truckTypeId)) {
            lambdaQueryWrapper.eq(DyTruck::getTruckTypeId, truckTypeId);

        }
        if (StringUtils.isNotEmpty(fleetId)) {
            lambdaQueryWrapper.eq(DyTruck::getFleetId, fleetId);

        }
        lambdaQueryWrapper.eq(DyTruck::getStatus, Constant.DATA_DEFAULT_STATUS);
        lambdaQueryWrapper.orderBy(true, false, DyTruck::getId);
        return baseMapper.selectPage(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<DyTruck> findAll(List<String> ids, String fleetId) {
        LambdaQueryWrapper<DyTruck> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(DyTruck::getId, ids);
        }
        if (StringUtils.isNotEmpty(fleetId)) {
            lambdaQueryWrapper.eq(DyTruck::getFleetId, fleetId);
        }
        lambdaQueryWrapper.eq(DyTruck::getStatus, Constant.DATA_DEFAULT_STATUS);
        lambdaQueryWrapper.orderBy(true, false, DyTruck::getId);
        return baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public Integer count(String fleetId) {
        LambdaQueryWrapper<DyTruck> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(fleetId)) {
            lambdaQueryWrapper.eq(DyTruck::getFleetId, fleetId);
        }
        lambdaQueryWrapper.eq(DyTruck::getStatus, Constant.DATA_DEFAULT_STATUS);
        return baseMapper.selectCount(lambdaQueryWrapper);
    }

    @Override
    public void disableById(String id) {
        DyTruck dyTruck = new DyTruck();
        dyTruck.setId(id);
        dyTruck.setStatus(Constant.DATA_DISABLE_STATUS);
        baseMapper.updateById(dyTruck);
    }

}
