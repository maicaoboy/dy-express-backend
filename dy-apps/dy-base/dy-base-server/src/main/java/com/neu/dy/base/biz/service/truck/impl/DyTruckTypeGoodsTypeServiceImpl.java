package com.neu.dy.base.biz.service.truck.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.neu.dy.base.biz.dao.truck.DyTruckTypeGoodsTypeMapper;
import com.neu.dy.base.biz.service.truck.IDyTruckTypeGoodsTypeService;
import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.base.entity.truck.DyTruckTypeGoodsType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 车辆类型与货物类型关联表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Service
public class DyTruckTypeGoodsTypeServiceImpl extends ServiceImpl<DyTruckTypeGoodsTypeMapper, DyTruckTypeGoodsType>
        implements IDyTruckTypeGoodsTypeService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public void saveTruckTypeGoodsType(DyTruckTypeGoodsType dyTruckTypeGoodsType) {
        dyTruckTypeGoodsType.setId(idGenerator.nextId(dyTruckTypeGoodsType) + "");
        baseMapper.insert(dyTruckTypeGoodsType);
    }

    @Override
    public void batchSave(List<DyTruckTypeGoodsType> truckTypeGoodsTypeList) {
        truckTypeGoodsTypeList.forEach(dyTruckTypeGoodsType -> dyTruckTypeGoodsType.setId(idGenerator.nextId(dyTruckTypeGoodsType) + ""));
        saveBatch(truckTypeGoodsTypeList);
    }

    @Override
    public void delete(String truckTypeId, String goodsTypeId) {
        LambdaQueryWrapper<DyTruckTypeGoodsType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        boolean canExecute = false;
        if (StringUtils.isNotEmpty(truckTypeId)) {
            lambdaQueryWrapper.eq(DyTruckTypeGoodsType::getTruckTypeId, truckTypeId);
            canExecute = true;
        }
        if (StringUtils.isNotEmpty(goodsTypeId)) {
            lambdaQueryWrapper.eq(DyTruckTypeGoodsType::getGoodsTypeId, goodsTypeId);
            canExecute = true;
        }
        if (canExecute) {
            baseMapper.delete(lambdaQueryWrapper);
        }
    }

    @Override
    public List<DyTruckTypeGoodsType> findAll(String truckTypeId, String goodsTypeId) {
        LambdaQueryWrapper<DyTruckTypeGoodsType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(truckTypeId)) {
            lambdaQueryWrapper.eq(DyTruckTypeGoodsType::getTruckTypeId, truckTypeId);
        }
        if (StringUtils.isNotEmpty(goodsTypeId)) {
            lambdaQueryWrapper.eq(DyTruckTypeGoodsType::getGoodsTypeId, goodsTypeId);
        }
        return baseMapper.selectList(lambdaQueryWrapper);
    }

}
