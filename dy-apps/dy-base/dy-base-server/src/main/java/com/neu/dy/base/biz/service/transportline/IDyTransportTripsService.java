package com.neu.dy.base.biz.service.transportline;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.base.entity.transportline.DyTransportTrips;

import java.util.List;


public interface IDyTransportTripsService extends IService<DyTransportTrips> {
    /**
     * 添加车次
     *
     * @param pdTransportTrips 车次信息
     * @return 车次信息
     */
    DyTransportTrips saveTransportTrips(DyTransportTrips pdTransportTrips);

    /**
     * 获取车次列表
     *
     * @param transportLineId 线路id
     * @param ids             车次id列表
     * @return 车次列表
     */
    List<DyTransportTrips> findAll(String transportLineId, List<String> ids);

    /**
     * 删除车次
     *
     * @param id
     */
    void disable(String id);
}
