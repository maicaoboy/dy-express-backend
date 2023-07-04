package com.neu.dy.base.biz.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.neu.dy.base.biz.dao.base.DyGoodsTypeMapper;
import com.neu.dy.base.biz.service.base.IDyGoodsTypeService;
import com.neu.dy.base.biz.service.truck.IDyTruckTypeGoodsTypeService;
import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.base.dto.base.GoodsTypeDto;
import com.neu.dy.base.entity.base.DyGoodsType;
import com.neu.dy.base.entity.truck.DyTruckTypeGoodsType;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname DyGoodsTypeServiceImpl
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/6/20 22:11
 * @Created by maicaoboy
 */
@Service
public class DyGoodsTypeServiceImpl extends ServiceImpl<DyGoodsTypeMapper, DyGoodsType> implements IDyGoodsTypeService {
    //id自增组件
    @Autowired
    private CustomIdGenerator idGenerator;

    @Autowired
    private IDyGoodsTypeService goodsTypeService;

    @Autowired
    private IDyTruckTypeGoodsTypeService truckTypeGoodsTypeService;

    @Override
    public DyGoodsType saveGoodsType(DyGoodsType pdGoodsType) {
        pdGoodsType.setId(idGenerator.nextId(pdGoodsType) + "");
        baseMapper.insert(pdGoodsType);
        return pdGoodsType;
    }

    @Override
    public List<DyGoodsType> findAll() {
        QueryWrapper<DyGoodsType> wrapper = new QueryWrapper<>();
        wrapper.eq("status",1);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public IPage<DyGoodsType> findByPage(Integer page, Integer pageSize, String name, String truckTypeId, String truckTypeName) {
        Page<DyGoodsType> iPage = new Page(page, pageSize);
        iPage.addOrder(OrderItem.asc("id"));
        iPage.setRecords(baseMapper.findByPage(iPage, name, truckTypeId, truckTypeName));
        return iPage;
    }

    @Override
    public List<DyGoodsType> findAll(List<String> ids) {
        LambdaQueryWrapper<DyGoodsType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(DyGoodsType::getId, ids);
        }
        return baseMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 更新货物类型信息
     *
     * @param dto 货物类型信息
     * @return 货物类型信息
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "更新货物类型信息")
    public GoodsTypeDto update(@PathVariable(name = "id") String id, @RequestBody GoodsTypeDto dto) {
        dto.setId(id);
        DyGoodsType pdGoodsType = new DyGoodsType();
        BeanUtils.copyProperties(dto, pdGoodsType);
        goodsTypeService.updateById(pdGoodsType);
        if (dto.getTruckTypeIds() != null) {
            truckTypeGoodsTypeService.delete(null, id);
            truckTypeGoodsTypeService.batchSave(dto.getTruckTypeIds().stream().map(truckTypeId -> {
                DyTruckTypeGoodsType truckTypeGoodsType = new DyTruckTypeGoodsType();
                truckTypeGoodsType.setTruckTypeId(truckTypeId);
                truckTypeGoodsType.setGoodsTypeId(id);
                return truckTypeGoodsType;
            }).collect(Collectors.toList()));
        }
        return dto;
    }
}
