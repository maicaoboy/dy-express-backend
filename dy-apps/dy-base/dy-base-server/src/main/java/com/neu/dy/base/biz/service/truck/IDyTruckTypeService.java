package com.neu.dy.base.biz.service.truck;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.base.entity.truck.DyTruckType;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 车辆类型表 服务类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
public interface IDyTruckTypeService extends IService<DyTruckType> {
    /**
     * 添加车辆类型
     *
     * @param pdTruckType 车辆类型信息
     * @return 车辆类型信息
     */
    DyTruckType saveTruckType(DyTruckType pdTruckType);

    /**
     * 获取车辆类型分页数据
     *
     * @param page            页码
     * @param pageSize        页尺寸
     * @param name            类型名称
     * @param allowableLoad   车型载重
     * @param allowableVolume 车型体积
     * @return 线路类型分页数据
     */
    IPage<DyTruckType> findByPage(Integer page, Integer pageSize, String name, BigDecimal allowableLoad,
                                  BigDecimal allowableVolume);

    /**
     * 获取车辆类型列表
     * @return 车辆类型列表
     */
    List<DyTruckType> findAll(List<String> ids);
}
