package com.neu.dy.base.biz.service.truck.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.neu.dy.base.biz.dao.truck.DyTruckLicenseMapper;
import com.neu.dy.base.biz.service.truck.IDyTruckLicenseService;
import com.neu.dy.base.biz.service.truck.IDyTruckService;
import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.base.entity.truck.DyTruck;
import com.neu.dy.base.entity.truck.DyTruckLicense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DyTruckLicenseServiceImpl extends ServiceImpl<DyTruckLicenseMapper, DyTruckLicense>
        implements IDyTruckLicenseService {
    @Autowired
    private CustomIdGenerator idGenerator;
    @Autowired
    private IDyTruckService truckService;

    @Override
    public DyTruckLicense saveTruckLicense(DyTruckLicense pdTruckLicense) {
        if (pdTruckLicense.getId() == null) {
            pdTruckLicense.setId(idGenerator.nextId(pdTruckLicense) + "");
            baseMapper.insert(pdTruckLicense);
            // 处理车辆信息中的关联字段
            if (pdTruckLicense.getTruckId() != null) {
                DyTruck pdTruck = truckService.getById(pdTruckLicense.getTruckId());
                pdTruck.setTruckLicenseId(pdTruckLicense.getId());
                truckService.updateById(pdTruck);
            }
        } else {
            baseMapper.updateById(pdTruckLicense);
        }
        return pdTruckLicense;
    }

}
