package com.neu.dy.base.biz.service.transportline.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.neu.dy.base.biz.dao.transportline.DyTransportTripsMapper;
import com.neu.dy.base.biz.service.transportline.IDyTransportTripsService;
import com.neu.dy.base.common.Constant;
import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.base.entity.transportline.DyTransportTrips;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DyTransportTripsServiceImpl extends ServiceImpl<DyTransportTripsMapper, DyTransportTrips>
        implements IDyTransportTripsService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public DyTransportTrips saveTransportTrips(DyTransportTrips pdTransportTrips) {
        pdTransportTrips.setId(idGenerator.nextId(pdTransportTrips) + "");
        baseMapper.insert(pdTransportTrips);
        return pdTransportTrips;
    }

    @Override
    public List<DyTransportTrips> findAll(String transportLineId, List<String> ids) {
        LambdaQueryWrapper<DyTransportTrips> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(transportLineId)) {
            lambdaQueryWrapper.eq(DyTransportTrips::getTransportLineId, transportLineId);
        }
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(DyTransportTrips::getId, ids);
        }
        lambdaQueryWrapper.orderBy(true, true, DyTransportTrips::getId);
        lambdaQueryWrapper.eq(DyTransportTrips::getStatus, Constant.DATA_DEFAULT_STATUS);
        return baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void disable(String id) {
        DyTransportTrips pdTransportTrips = new DyTransportTrips();
        pdTransportTrips.setId(id);
        pdTransportTrips.setStatus(Constant.DATA_DISABLE_STATUS);
        baseMapper.updateById(pdTransportTrips);
    }

}
