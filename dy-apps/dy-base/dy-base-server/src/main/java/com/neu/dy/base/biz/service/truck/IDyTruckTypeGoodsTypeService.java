package com.neu.dy.base.biz.service.truck;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.base.entity.truck.DyTruckTypeGoodsType;

import java.util.List;


public interface IDyTruckTypeGoodsTypeService extends IService<DyTruckTypeGoodsType> {
    /**
     * 添加车辆类型与货物类型关联
     *
     * @param dyTruckTypeGoodsType 车辆类型与货物类型信息
     */
    void saveTruckTypeGoodsType(DyTruckTypeGoodsType dyTruckTypeGoodsType);

    /**
     * 批量添加车辆类型与货物类型关联
     *
     * @param truckTypeGoodsTypeList 车辆类型与货物类型信息
     */
    void batchSave(List<DyTruckTypeGoodsType> truckTypeGoodsTypeList);

    /**
     * 删除关联关系
     *
     * @param truckTypeId 车辆类型id
     * @param goodsTypeId 货物类型id
     */
    void delete(String truckTypeId, String goodsTypeId);

    /**
     * 获取车辆类型与货物类型关联
     *
     * @param truckTypeId 车辆类型id
     * @param goodsTypeId 货物类型id
     * @return 车辆类型与货物类型关联
     */
    List<DyTruckTypeGoodsType> findAll(String truckTypeId, String goodsTypeId);
}
