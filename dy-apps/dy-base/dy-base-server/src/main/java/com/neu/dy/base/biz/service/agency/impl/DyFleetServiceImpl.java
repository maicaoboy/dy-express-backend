package com.neu.dy.base.biz.service.agency.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.neu.dy.base.biz.dao.agency.DyFleetMapper;
import com.neu.dy.base.biz.service.agency.IDyFleetService;
import com.neu.dy.base.common.Constant;
import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.base.entity.agency.DyFleet;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DyFleetServiceImpl extends ServiceImpl<DyFleetMapper, DyFleet> implements IDyFleetService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public DyFleet saveFleet(DyFleet fleet) {
        fleet.setId(idGenerator.nextId(fleet) + "");
        baseMapper.insert(fleet);
        return fleet;
    }

    @Override
    public IPage<DyFleet> findByPage(Integer page, Integer pageSize, String name, String fleetNumber, String manager) {
        Page<DyFleet> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<DyFleet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            lambdaQueryWrapper.like(DyFleet::getName, name);
        }
        if (StringUtils.isNotEmpty(fleetNumber)) {
            lambdaQueryWrapper.like(DyFleet::getFleetNumber, fleetNumber);
        }
        if (StringUtils.isNotEmpty(manager)) {
            lambdaQueryWrapper.eq(DyFleet::getManager, manager);
        }
        lambdaQueryWrapper.eq(DyFleet::getStatus, Constant.DATA_DEFAULT_STATUS);
        lambdaQueryWrapper.orderBy(true, true, DyFleet::getId);
        return baseMapper.selectPage(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<DyFleet> findAll(List<String> ids, String agencyId) {
        LambdaQueryWrapper<DyFleet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(DyFleet::getId, ids);
        }
        if (StringUtils.isNotEmpty(agencyId)) {
            lambdaQueryWrapper.eq(DyFleet::getAgencyId, agencyId);
        }
        lambdaQueryWrapper.orderBy(true, true, DyFleet::getId);
        lambdaQueryWrapper.eq(DyFleet::getStatus, Constant.DATA_DEFAULT_STATUS);
        return baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void disableById(String id) {
        DyFleet fleet = new DyFleet();
        fleet.setId(id);
        fleet.setStatus(Constant.DATA_DISABLE_STATUS);
        baseMapper.updateById(fleet);
    }

}
