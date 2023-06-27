package com.neu.dy.base.biz.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.base.entity.user.DyTruckDriverLicense;


public interface IDyTruckDriverLicenseService extends IService<DyTruckDriverLicense> {
    /**
     * 保存司机驾驶证信息
     *
     * @param pdTruckDriverLicense 司机驾驶证信息
     * @return 司机驾驶证信息
     */
    DyTruckDriverLicense saveTruckDriverLicense(DyTruckDriverLicense pdTruckDriverLicense);

    /**
     * 获取司机驾驶证信息
     *
     * @param userId 司机id
     * @return 司机驾驶证信息
     */
    DyTruckDriverLicense findOne(String userId);
}
