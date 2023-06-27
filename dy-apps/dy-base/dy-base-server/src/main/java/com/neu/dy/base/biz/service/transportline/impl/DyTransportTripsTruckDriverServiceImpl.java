package com.neu.dy.base.biz.service.transportline.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.neu.dy.base.biz.dao.transportline.DyTransportTripsTruckDriverMapper;
import com.neu.dy.base.biz.service.transportline.IDyTransportTripsTruckDriverService;
import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.base.entity.transportline.DyTransportTripsTruckDriver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DyTransportTripsTruckDriverServiceImpl extends ServiceImpl<DyTransportTripsTruckDriverMapper, DyTransportTripsTruckDriver>
        implements IDyTransportTripsTruckDriverService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public void batchSave(String truckTransportTripsId, List<DyTransportTripsTruckDriver> truckTransportTripsTruckDriverList) {
        LambdaQueryWrapper<DyTransportTripsTruckDriver> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DyTransportTripsTruckDriver::getTransportTripsId, truckTransportTripsId);
        //查出操作前关系列表
        List<DyTransportTripsTruckDriver> transportTripsTruckDriverList = baseMapper.selectList(lambdaQueryWrapper);
        Map<String, DyTransportTripsTruckDriver> sourceTruckKeyMap = new HashMap<>();
        for (DyTransportTripsTruckDriver pdTransportTripsTruckDriver:transportTripsTruckDriverList){
            sourceTruckKeyMap.put(pdTransportTripsTruckDriver.getTransportTripsId() + "_" + pdTransportTripsTruckDriver.getTruckId(),pdTransportTripsTruckDriver);
        }
        //清除关系
        baseMapper.delete(lambdaQueryWrapper);
        List<DyTransportTripsTruckDriver> saveList = new ArrayList<>();
        //遍历传入数据
        truckTransportTripsTruckDriverList.forEach(pdTransportTripsTruckDriver -> {
            DyTransportTripsTruckDriver saveData = new DyTransportTripsTruckDriver();
            BeanUtils.copyProperties(pdTransportTripsTruckDriver, saveData);
            saveData.setId(idGenerator.nextId(saveData) + "");
            if (sourceTruckKeyMap.containsKey(pdTransportTripsTruckDriver.getTransportTripsId() + "_" + pdTransportTripsTruckDriver.getTruckId())) {
                DyTransportTripsTruckDriver source = sourceTruckKeyMap.get(pdTransportTripsTruckDriver.getTransportTripsId() + "_" + pdTransportTripsTruckDriver.getTruckId());
                if (saveData.getUserId() == null) {
                    saveData.setUserId(source.getUserId());
                }
            }
            saveList.add(saveData);
        });
        saveBatch(saveList);
    }

    @Override
    public List<DyTransportTripsTruckDriver> findAll(String transportTripsId, String truckId, String userId) {
        LambdaQueryWrapper<DyTransportTripsTruckDriver> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(transportTripsId)) {
            lambdaQueryWrapper.eq(DyTransportTripsTruckDriver::getTransportTripsId, transportTripsId);
        }
        if (StringUtils.isNotEmpty(truckId)) {
            lambdaQueryWrapper.eq(DyTransportTripsTruckDriver::getTruckId, truckId);
        }
        if (StringUtils.isNotEmpty(userId)) {
            lambdaQueryWrapper.eq(DyTransportTripsTruckDriver::getUserId, userId);
        }
        return baseMapper.selectList(lambdaQueryWrapper);
    }
}
