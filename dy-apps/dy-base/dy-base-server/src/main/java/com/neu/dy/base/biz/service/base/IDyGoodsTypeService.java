package com.neu.dy.base.biz.service.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.base.entity.base.DyGoodsType;


import java.util.List;

//继承mybatis-plus的IService接口
public interface IDyGoodsTypeService extends IService<DyGoodsType>{
    /**
     * 添加货物类型
     *
     * @param dyGoodsType 货物类型信息
     * @return 货物类型信息
     */
    DyGoodsType saveGoodsType(DyGoodsType dyGoodsType);

    List<DyGoodsType> findAll();

    /**
     * 获取分页货物类型数据
     * @param page 页码
     * @param pageSize 页尺寸
     * @return 分页货物数据
     */
    IPage<DyGoodsType> findByPage(Integer page, Integer pageSize,String name,String truckTypeId,String truckTypeName);

    /**
     * 获取货物类型列表
     * @param ids 货物类型id
     * @return 货物类型列表
     */
    List<DyGoodsType> findAll(List<String> ids);
}
