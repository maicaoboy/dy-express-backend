package com.neu.dy.base.biz.service.transportline.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.neu.dy.base.biz.dao.transportline.DyTransportLineTypeMapper;
import com.neu.dy.base.biz.service.transportline.IDyTransportLineTypeService;
import com.neu.dy.base.common.Constant;
import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.base.entity.transportline.DyTransportLineType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class DyTransportLineTypeServiceImpl extends ServiceImpl<DyTransportLineTypeMapper, DyTransportLineType>
        implements IDyTransportLineTypeService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public DyTransportLineType saveTransportLineType(DyTransportLineType dyTransportLineType) {
        dyTransportLineType.setId(idGenerator.nextId(dyTransportLineType) + "");
        dyTransportLineType.setLastUpdateTime(LocalDateTime.now());
        baseMapper.insert(dyTransportLineType);
        return dyTransportLineType;
    }

    @Override
    public IPage<DyTransportLineType> findByPage(Integer page, Integer pageSize, String typeNumber, String name,
                                                 Integer agencyType) {
        Page<DyTransportLineType> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<DyTransportLineType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            lambdaQueryWrapper.like(DyTransportLineType::getName, name);
        }
        if (StringUtils.isNotEmpty(typeNumber)) {
            lambdaQueryWrapper.like(DyTransportLineType::getTypeNumber, typeNumber);
        }
        if (agencyType != null) {
            lambdaQueryWrapper.and(i -> i.eq(DyTransportLineType::getStartAgencyType, agencyType).or()
                    .eq(DyTransportLineType::getEndAgencyType, agencyType));

        }
        lambdaQueryWrapper.eq(DyTransportLineType::getStatus, Constant.DATA_DEFAULT_STATUS);
        lambdaQueryWrapper.orderBy(true, true, DyTransportLineType::getId);
        return baseMapper.selectPage(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<DyTransportLineType> findAll(List<String> ids) {
        LambdaQueryWrapper<DyTransportLineType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(DyTransportLineType::getId, ids);
        }
        lambdaQueryWrapper.orderBy(true, true, DyTransportLineType::getId);
        return baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void disableById(String id) {
        DyTransportLineType dyTransportLineType = new DyTransportLineType();
        dyTransportLineType.setId(id);
        dyTransportLineType.setStatus(Constant.DATA_DISABLE_STATUS);
        baseMapper.updateById(dyTransportLineType);
    }

}
