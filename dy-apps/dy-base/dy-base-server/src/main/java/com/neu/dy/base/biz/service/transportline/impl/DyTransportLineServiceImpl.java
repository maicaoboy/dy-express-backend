package com.neu.dy.base.biz.service.transportline.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.neu.dy.base.biz.dao.transportline.DyTransportLineMapper;
import com.neu.dy.base.biz.service.transportline.IDyTransportLineService;
import com.neu.dy.base.common.Constant;
import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.base.entity.transportline.DyTransportLine;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DyTransportLineServiceImpl extends ServiceImpl<DyTransportLineMapper, DyTransportLine>
        implements IDyTransportLineService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public DyTransportLine saveTransportLine(DyTransportLine pdTransportLine) {
        pdTransportLine.setId(idGenerator.nextId(pdTransportLine) + "");
        baseMapper.insert(pdTransportLine);
        return pdTransportLine;
    }

    @Override
    public IPage<DyTransportLine> findByPage(Integer page, Integer pageSize, String lineNumber, String name,
                                             String transportLineTypeId) {
        Page<DyTransportLine> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<DyTransportLine> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            lambdaQueryWrapper.like(DyTransportLine::getName, name);
        }
        if (StringUtils.isNotEmpty(lineNumber)) {
            lambdaQueryWrapper.like(DyTransportLine::getLineNumber, lineNumber);
        }
        if (StringUtils.isNotEmpty(transportLineTypeId)) {
            lambdaQueryWrapper.eq(DyTransportLine::getTransportLineTypeId, transportLineTypeId);

        }
        lambdaQueryWrapper.eq(DyTransportLine::getStatus, Constant.DATA_DEFAULT_STATUS);
        lambdaQueryWrapper.orderBy(true, false, DyTransportLine::getId);
        return baseMapper.selectPage(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<DyTransportLine> findAll(List<String> ids, String agencyId, List<String> agencyIds) {
        LambdaQueryWrapper<DyTransportLine> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(DyTransportLine::getId, ids);
        }
        if (StringUtils.isNotEmpty(agencyId)) {
            lambdaQueryWrapper.eq(DyTransportLine::getAgencyId, agencyId);
        }
        if (agencyIds != null && agencyIds.size() > 0) {
            lambdaQueryWrapper.in(DyTransportLine::getAgencyId, agencyIds);
        }
        lambdaQueryWrapper.eq(DyTransportLine::getStatus, Constant.DATA_DEFAULT_STATUS);
        lambdaQueryWrapper.orderBy(true, false, DyTransportLine::getId);
        return baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void disable(String id) {
        DyTransportLine pdTransportLine = new DyTransportLine();
        pdTransportLine.setId(id);
        pdTransportLine.setStatus(Constant.DATA_DISABLE_STATUS);
        baseMapper.updateById(pdTransportLine);
    }

}
