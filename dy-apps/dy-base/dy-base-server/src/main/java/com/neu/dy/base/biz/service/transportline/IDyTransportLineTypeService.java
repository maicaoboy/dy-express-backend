package com.neu.dy.base.biz.service.transportline;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.base.entity.transportline.DyTransportLineType;


import java.util.List;


public interface IDyTransportLineTypeService extends IService<DyTransportLineType> {
    /**
     * 添加线路类型
     *
     * @param dyTransportLineType 线路类型信息
     * @return 线路类型信息
     */
    DyTransportLineType saveTransportLineType(DyTransportLineType dyTransportLineType);

    /**
     * 获取线路类型分页数据
     *
     * @param page         页码
     * @param pageSize     页尺寸
     * @param typeNumber   类型编号
     * @param name         类型名称
     * @param agencyType 机构类型
     * @return 线路类型分页数据
     */
    IPage<DyTransportLineType> findByPage(Integer page, Integer pageSize, String typeNumber, String name, Integer agencyType);

    /**
     * 获取线路类型列表
     *
     * @param ids 线路类型id列表
     * @return 线路类型列表
     */
    List<DyTransportLineType> findAll(List<String> ids);

    /**
     * 删除线路类型
     *
     * @param id
     */
    void disableById(String id);
}
