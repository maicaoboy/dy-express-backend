package com.neu.dy.base.biz.service.agency.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.neu.dy.base.biz.dao.agency.DyAgencyScopMapper;
import com.neu.dy.base.biz.service.agency.IDyAgencyScopeService;
import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.base.dto.angency.AgencyScopeDto;
import com.neu.dy.base.entity.agency.DyAgencyScope;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DyAgencyScopeServiceImpl extends ServiceImpl<DyAgencyScopMapper, DyAgencyScope> implements IDyAgencyScopeService {
    @Autowired
    private CustomIdGenerator idGenerator;


    @Override
    public void batchSave(List<DyAgencyScope> scopeList) {
        scopeList.forEach(scope -> scope.setId(idGenerator.nextId(scope) + ""));
        saveBatch(scopeList);
    }

    @Override
    public void delete(String areaId, String agencyId) {
        LambdaQueryWrapper<DyAgencyScope> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        boolean canExecute = false;
        if (StringUtils.isNotEmpty(areaId)) {
            lambdaQueryWrapper.eq(DyAgencyScope::getAreaId, areaId);
            canExecute = true;
        }
        if (StringUtils.isNotEmpty(agencyId)) {
            lambdaQueryWrapper.eq(DyAgencyScope::getAgencyId, agencyId);
            canExecute = true;
        }
        if (canExecute) {
            baseMapper.delete(lambdaQueryWrapper);
        }
    }

    @Override
    public List<DyAgencyScope> findAll(String areaId, String agencyId, List<String> agencyIds, List<String> areaIds) {
        LambdaQueryWrapper<DyAgencyScope> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(areaId)) {
            lambdaQueryWrapper.eq(DyAgencyScope::getAreaId, areaId);
        }
        if (StringUtils.isNotEmpty(agencyId)) {
            lambdaQueryWrapper.eq(DyAgencyScope::getAgencyId, agencyId);
        }
        if (agencyIds != null && agencyIds.size() > 0) {
            lambdaQueryWrapper.in(DyAgencyScope::getAgencyId, agencyIds);
        }
        if (areaIds != null && areaIds.size() > 0) {
            lambdaQueryWrapper.in(DyAgencyScope::getAreaId, areaIds);
        }
        return baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public IPage<DyAgencyScope> getByPage(Integer page, Integer pageSize, DyAgencyScope dyAgencyScope) {
        Page<DyAgencyScope> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<DyAgencyScope> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (org.apache.commons.lang.StringUtils.isNotBlank(dyAgencyScope.getAreaId())) {
            lambdaQueryWrapper.like(DyAgencyScope::getAreaId, dyAgencyScope.getAgencyId());
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(dyAgencyScope.getAgencyId())) {
            lambdaQueryWrapper.like(DyAgencyScope::getAgencyId, dyAgencyScope.getAgencyId());
        }
        return page(iPage, lambdaQueryWrapper);
    }
}
