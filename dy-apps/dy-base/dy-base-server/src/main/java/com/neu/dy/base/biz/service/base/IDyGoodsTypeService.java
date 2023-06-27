package com.neu.dy.base.biz.service.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.base.entity.base.DyGoodsType;


import java.util.List;

/**
 * @Classname IDyGoodsTypeService
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/6/20 21:52
 * @Created by maicaoboy
 */
public interface IDyGoodsTypeService extends IService<DyGoodsType>{
    /**
     * 添加货物类型
     *
     * @param pdGoodsType 货物类型信息
     * @return 货物类型信息
     */
    DyGoodsType saveGoodsType(DyGoodsType pdGoodsType);

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
