package com.neu.dy.base.biz.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.neu.dy.base.biz.dao.user.DyCourierScopMapper;
import com.neu.dy.base.biz.service.user.IDyCourierScopeService;
import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.base.entity.user.DyCourierScope;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DyCourierScopServiceImpl extends ServiceImpl<DyCourierScopMapper, DyCourierScope> implements IDyCourierScopeService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public void batchSave(List<DyCourierScope> scopeList) {
        scopeList.forEach(scope -> scope.setId(idGenerator.nextId(scope) + ""));
        saveBatch(scopeList);
    }

    @Override
    public void delete(String areaId, String userId) {
        LambdaQueryWrapper<DyCourierScope> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        boolean canExecute = false;
        if (StringUtils.isNotEmpty(areaId)) {
            lambdaQueryWrapper.eq(DyCourierScope::getAreaId, areaId);
            canExecute = true;
        }
        if (StringUtils.isNotEmpty(userId)) {
            lambdaQueryWrapper.eq(DyCourierScope::getUserId, userId);
            canExecute = true;
        }
        if (canExecute) {
            baseMapper.delete(lambdaQueryWrapper);
        }
    }

    @Override
    public List<DyCourierScope> findAll(String areaId, String userId) {
        LambdaQueryWrapper<DyCourierScope> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(areaId)) {
            lambdaQueryWrapper.eq(DyCourierScope::getAreaId, areaId);
        }
        if (StringUtils.isNotEmpty(userId)) {
            lambdaQueryWrapper.eq(DyCourierScope::getUserId, userId);
        }
        return baseMapper.selectList(lambdaQueryWrapper);
    }
}
