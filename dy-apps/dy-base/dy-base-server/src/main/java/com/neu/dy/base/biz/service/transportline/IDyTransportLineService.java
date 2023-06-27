package com.neu.dy.base.biz.service.transportline;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.base.entity.transportline.DyTransportLine;


import java.util.List;


public interface IDyTransportLineService extends IService<DyTransportLine> {
    /**
     * 添加线路
     *
     * @param pdTransportLine 线路信息
     * @return 线路信息
     */
    DyTransportLine saveTransportLine(DyTransportLine pdTransportLine);

    /**
     * 获取线路分页数据
     *
     * @param page                页码
     * @param pageSize            页尺寸
     * @param lineNumber          线路编号
     * @param name                线路名称
     * @param transportLineTypeId 机构类型
     * @return 线路类型分页数据
     */
    IPage<DyTransportLine> findByPage(Integer page, Integer pageSize, String lineNumber, String name, String transportLineTypeId);

    /**
     * 获取线路列表
     *
     * @param ids       线路id列表
     * @param agencyId  机构id
     * @param agencyIds 机构id列表
     * @return 线路列表
     */
    List<DyTransportLine> findAll(List<String> ids, String agencyId, List<String> agencyIds);

    /**
     * 删除线路类型
     *
     * @param id
     */
    void disable(String id);
}
