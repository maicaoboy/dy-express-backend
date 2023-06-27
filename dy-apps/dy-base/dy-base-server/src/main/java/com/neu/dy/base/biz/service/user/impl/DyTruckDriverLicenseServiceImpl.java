package com.neu.dy.base.biz.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.neu.dy.base.biz.dao.user.DyTruckDriverLicenseMapper;
import com.neu.dy.base.biz.service.user.IDyTruckDriverLicenseService;
import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.base.entity.user.DyTruckDriverLicense;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DyTruckDriverLicenseServiceImpl extends ServiceImpl<DyTruckDriverLicenseMapper, DyTruckDriverLicense> implements IDyTruckDriverLicenseService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public DyTruckDriverLicense saveTruckDriverLicense(DyTruckDriverLicense pdTruckDriverLicense) {
        DyTruckDriverLicense driverLicense = baseMapper.selectOne(new LambdaQueryWrapper<DyTruckDriverLicense>().eq(DyTruckDriverLicense::getUserId, pdTruckDriverLicense.getUserId()));
        if (driverLicense == null) {
            pdTruckDriverLicense.setId(idGenerator.nextId(pdTruckDriverLicense) + "");
        } else {
            pdTruckDriverLicense.setId(driverLicense.getId());
        }
        saveOrUpdate(pdTruckDriverLicense);
        return pdTruckDriverLicense;
    }

    @Override
    public DyTruckDriverLicense findOne(String userId) {
        LambdaQueryWrapper<DyTruckDriverLicense> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(userId)) {
            lambdaQueryWrapper.eq(DyTruckDriverLicense::getUserId, userId);
        }
        return getOne(lambdaQueryWrapper);
    }

}
