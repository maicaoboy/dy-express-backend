package com.neu.dy.base.biz.service.truck;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.base.entity.truck.DyTruckLicense;


public interface IDyTruckLicenseService extends IService<DyTruckLicense> {
    /**
     * 保存车辆行驶证信息
     *
     * @param pdTruckLicense 车辆行驶证信息
     * @return 车辆行驶证信息
     */
    DyTruckLicense saveTruckLicense(DyTruckLicense pdTruckLicense);
}
