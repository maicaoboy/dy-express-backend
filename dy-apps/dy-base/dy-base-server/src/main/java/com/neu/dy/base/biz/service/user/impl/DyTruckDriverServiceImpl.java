package com.neu.dy.base.biz.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.neu.dy.base.biz.dao.user.DyTruckDriverMapper;
import com.neu.dy.base.biz.service.user.IDyTruckDriverService;
import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.base.entity.user.DyTruckDriver;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DyTruckDriverServiceImpl extends ServiceImpl<DyTruckDriverMapper, DyTruckDriver>
        implements IDyTruckDriverService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public DyTruckDriver saveTruckDriver(DyTruckDriver pdTruckDriver) {
        DyTruckDriver driver = baseMapper.selectOne(new LambdaQueryWrapper<DyTruckDriver>().eq(DyTruckDriver::getUserId, pdTruckDriver.getUserId()));
        if (driver == null) {
            pdTruckDriver.setId(idGenerator.nextId(pdTruckDriver) + "");
        } else {
            pdTruckDriver.setId(driver.getId());
        }
        saveOrUpdate(pdTruckDriver);
        return pdTruckDriver;
    }

    @Override
    public List<DyTruckDriver> findAll(List<String> userIds, String fleetId) {
        boolean hasUserIds = userIds != null && userIds.size() > 0;
        boolean hasFleetId = StringUtils.isNotEmpty(fleetId);
        if (!hasUserIds && !hasFleetId) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<DyTruckDriver> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (hasUserIds) {
            lambdaQueryWrapper.in(DyTruckDriver::getUserId, userIds);
        }
        if (hasFleetId) {
            lambdaQueryWrapper.eq(DyTruckDriver::getFleetId, fleetId);
        }
        lambdaQueryWrapper.orderBy(true, false, DyTruckDriver::getId);
        return baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public DyTruckDriver findOne(String userId) {
        LambdaQueryWrapper<DyTruckDriver> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(userId)) {
            lambdaQueryWrapper.eq(DyTruckDriver::getUserId, userId);
        }
        return getOne(lambdaQueryWrapper);
    }

    @Override
    public Integer count(String fleetId) {
        LambdaQueryWrapper<DyTruckDriver> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(fleetId)) {
            lambdaQueryWrapper.eq(DyTruckDriver::getFleetId, fleetId);
        }
        return count(lambdaQueryWrapper);
    }

    @Override
    public IPage<DyTruckDriver> findByPage(Integer page, Integer pageSize, String fleetId) {
        Page<DyTruckDriver> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<DyTruckDriver> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(fleetId)) {
            lambdaQueryWrapper.eq(DyTruckDriver::getFleetId, fleetId);
        }
        lambdaQueryWrapper.orderBy(true, false, DyTruckDriver::getId);
        return baseMapper.selectPage(iPage, lambdaQueryWrapper);
    }
}
