package com.neu.dy.base.controller.truck;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.neu.dy.base.biz.service.truck.IDyTruckTypeGoodsTypeService;
import com.neu.dy.base.biz.service.truck.IDyTruckTypeService;
import com.neu.dy.base.common.Constant;
import com.neu.dy.base.common.PageResponse;
import com.neu.dy.base.common.Result;
import com.neu.dy.base.dto.truck.TruckTypeDto;
import com.neu.dy.base.entity.truck.DyTruckType;
import com.neu.dy.base.entity.truck.DyTruckTypeGoodsType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TruckTypeController
 */
@RestController
@RequestMapping("base/truck/type")
public class TruckTypeController {
    @Autowired
    private IDyTruckTypeService truckTypeService;
    @Autowired
    private IDyTruckTypeGoodsTypeService truckTypeGoodsTypeService;

    /**
     * 添加车辆类型
     *
     * @param dto 车辆类型信息
     * @return 车辆类型信息
     */
    @PostMapping("")
    public TruckTypeDto saveTruckType(@RequestBody TruckTypeDto dto) {
        DyTruckType pdTruckType = new DyTruckType();
        BeanUtils.copyProperties(dto, pdTruckType);
        pdTruckType = truckTypeService.saveTruckType(pdTruckType);
        String truckTypeId = pdTruckType.getId();
        //处理与货物类型的关联
        if (dto.getGoodsTypeIds() != null) {
            truckTypeGoodsTypeService.batchSave(dto.getGoodsTypeIds().stream().map(id -> {
                DyTruckTypeGoodsType truckTypeGoodsType = new DyTruckTypeGoodsType();
                truckTypeGoodsType.setGoodsTypeId(id);
                truckTypeGoodsType.setTruckTypeId(truckTypeId);
                return truckTypeGoodsType;
            }).collect(Collectors.toList()));
        }
        BeanUtils.copyProperties(pdTruckType, dto);
        return dto;
    }

    /**
     * 根据id获取车辆类型详情
     *
     * @param id 车辆类型id
     * @return 车辆类型信息
     */
    @GetMapping("/{id}")
    public TruckTypeDto fineById(@PathVariable(name = "id") String id) {
        DyTruckType pdTruckType = truckTypeService.getById(id);
        TruckTypeDto dto = new TruckTypeDto();
        BeanUtils.copyProperties(pdTruckType, dto);
        dto.setGoodsTypeIds(truckTypeGoodsTypeService.findAll(dto.getId(), null).stream().map(pdTruckTypeGoodsType -> pdTruckTypeGoodsType.getGoodsTypeId()).collect(Collectors.toList()));
        return dto;
    }

    /**
     * 获取车辆类型分页数据
     *
     * @param page            页码
     * @param pageSize        页尺寸
     * @param name            车辆类型名称
     * @param allowableLoad   车辆载重
     * @param allowableVolume 车辆体积
     * @return 车辆类型分页数据
     */
    @GetMapping("/page")
    public PageResponse<TruckTypeDto> findByPage(@RequestParam(name = "page") Integer page,
                                                 @RequestParam(name = "pageSize") Integer pageSize,
                                                 @RequestParam(name = "name", required = false) String name,
                                                 @RequestParam(name = "allowableLoad", required = false) BigDecimal allowableLoad,
                                                 @RequestParam(name = "allowableVolume", required = false) BigDecimal allowableVolume) {
        IPage<DyTruckType> pdTruckTypePage = truckTypeService.findByPage(page, pageSize, name, allowableLoad,
                allowableVolume);
        List<TruckTypeDto> dtoList = new ArrayList<>();
        pdTruckTypePage.getRecords().forEach(pdTruckType -> {
            TruckTypeDto dto = new TruckTypeDto();
            BeanUtils.copyProperties(pdTruckType, dto);
            dto.setGoodsTypeIds(truckTypeGoodsTypeService.findAll(dto.getId(), null).stream().map(pdTruckTypeGoodsType -> pdTruckTypeGoodsType.getGoodsTypeId()).collect(Collectors.toList()));
            dtoList.add(dto);
        });
        return PageResponse.<TruckTypeDto>builder().items(dtoList).pagesize(pageSize).page(page)
                .counts(pdTruckTypePage.getTotal()).pages(pdTruckTypePage.getPages()).build();
    }

    /**
     * 获取车辆类型列表
     *
     * @param ids 车辆类型id
     * @return 车辆类型列表
     */
    @GetMapping("")
    public List<TruckTypeDto> findAll(@RequestParam(name = "ids",required = false) List<String> ids) {
        return truckTypeService.findAll(ids).stream().map(truckType -> {
            TruckTypeDto dto = new TruckTypeDto();
            BeanUtils.copyProperties(truckType, dto);
            dto.setGoodsTypeIds(truckTypeGoodsTypeService.findAll(dto.getId(), null).stream().map(pdTruckTypeGoodsType -> pdTruckTypeGoodsType.getGoodsTypeId()).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 更新车辆类型信息
     *
     * @param id  车辆类型id
     * @param dto 车辆类型信息
     * @return 车辆类型信息
     */
    @PutMapping("/{id}")
    public TruckTypeDto update(@PathVariable(name = "id") String id, @RequestBody TruckTypeDto dto) {
        dto.setId(id);
        DyTruckType truckType = new DyTruckType();
        BeanUtils.copyProperties(dto, truckType);
        truckTypeService.updateById(truckType);
        //处理与货物类型的关联
        if (dto.getGoodsTypeIds() != null) {
            truckTypeGoodsTypeService.delete(id, null);
            //绑定新的关系
            truckTypeGoodsTypeService.batchSave(dto.getGoodsTypeIds().stream().map(goodsTypeId -> {
                DyTruckTypeGoodsType truckTypeGoodsType = new DyTruckTypeGoodsType();
                truckTypeGoodsType.setGoodsTypeId(goodsTypeId);
                truckTypeGoodsType.setTruckTypeId(id);
                return truckTypeGoodsType;
            }).collect(Collectors.toList()));
        }
        return dto;
    }

    /**
     * 删除车辆类型
     *
     * @param id 车辆类型Id
     * @return 返回信息
     */
    @PutMapping("/{id}/disable")
    public Result disable(@PathVariable(name = "id") String id) {
        // TODO: 2020/1/8 待实现，是否关联数据
        DyTruckType truckType = new DyTruckType();
        truckType.setId(id);
        truckType.setStatus(Constant.DATA_DISABLE_STATUS);
        truckTypeService.updateById(truckType);
        return Result.ok();
    }

}