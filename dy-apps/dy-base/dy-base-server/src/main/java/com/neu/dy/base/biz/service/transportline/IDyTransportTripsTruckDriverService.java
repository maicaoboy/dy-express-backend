package com.neu.dy.base.biz.service.transportline;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.base.entity.transportline.DyTransportTripsTruckDriver;

import java.util.List;


public interface IDyTransportTripsTruckDriverService extends IService<DyTransportTripsTruckDriver> {
    /**
     * 批量保存车次与车辆关联信息
     *
     * @param truckTransportTrips 车次与车辆关联信息
     */
    void batchSave(String truckTransportTripsId, List<DyTransportTripsTruckDriver> truckTransportTrips);

    /**
     * 获取车次与车辆关联列表
     *
     * @param transportTripsId 车次id
     * @param truckId          车辆Id
     * @param userId           司机id
     * @return 车次与车辆关联列表
     */
    List<DyTransportTripsTruckDriver> findAll(String transportTripsId, String truckId, String userId);
}
