package com.neu.dy.base.controller.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.neu.dy.base.biz.service.base.IDyGoodsTypeService;
import com.neu.dy.base.biz.service.truck.IDyTruckTypeGoodsTypeService;
import com.neu.dy.base.common.Constant;
import com.neu.dy.base.common.PageResponse;
import com.neu.dy.base.common.Result;
import com.neu.dy.base.dto.base.GoodsTypeDto;
import com.neu.dy.base.entity.base.DyGoodsType;
import com.neu.dy.base.entity.truck.DyTruckTypeGoodsType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname GoodsTypeController
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/6/20 21:51
 * @Created by maicaoboy
 */
@RestController
@RequestMapping("base/goodsType")
@Api(tags = "货物类型管理")
public class GoodsTypeController {
    @Autowired
    private IDyGoodsTypeService goodsTypeService;
    @Autowired
    private IDyTruckTypeGoodsTypeService truckTypeGoodsTypeService;

    /**
     * 添加货物类型
     *
     * @param dto 货物类型信息
     * @return 货物类型信息
     */
    @PostMapping("")
    @ApiOperation(value = "添加货物类型")
    public GoodsTypeDto saveGoodsType(@Validated @RequestBody GoodsTypeDto dto) {
        DyGoodsType pdGoodsType = new DyGoodsType();
        BeanUtils.copyProperties(dto, pdGoodsType);
        pdGoodsType = goodsTypeService.saveGoodsType(pdGoodsType);
        String goodsTypeId = pdGoodsType.getId();
        if (dto.getTruckTypeIds() != null) {
            truckTypeGoodsTypeService.batchSave(dto.getTruckTypeIds().stream().map(truckTypeId -> {
                DyTruckTypeGoodsType truckTypeGoodsType = new DyTruckTypeGoodsType();
                truckTypeGoodsType.setTruckTypeId(truckTypeId);
                truckTypeGoodsType.setGoodsTypeId(goodsTypeId);
                return truckTypeGoodsType;
            }).collect(Collectors.toList()));
        }
        BeanUtils.copyProperties(pdGoodsType, dto);
        return dto;
    }

    /**
     * 根据id查询货物类型
     *
     * @param id 货物类型id
     * @return 货物类型信息
     */
    @GetMapping("/{id}")
    public GoodsTypeDto fineById(@PathVariable(name = "id") String id) {
        DyGoodsType pdGoodsType = goodsTypeService.getById(id);
        GoodsTypeDto dto = null;
        if (pdGoodsType != null) {
            dto = new GoodsTypeDto();
            BeanUtils.copyProperties(pdGoodsType, dto);
            dto.setTruckTypeIds(truckTypeGoodsTypeService.findAll(null, dto.getId()).stream().map(truckTypeGoodsType -> truckTypeGoodsType.getTruckTypeId()).collect(Collectors.toList()));
        }
        return dto;
    }

    /**
     * 查询所有货物类型
     * @return
     */
    @GetMapping("/all")
    @ApiOperation(value = "查询所有货物类型")
    public List<GoodsTypeDto> findAll() {
        List<DyGoodsType> goodsType = goodsTypeService.findAll();
        List<GoodsTypeDto> goodsTypeDtoList = goodsType.stream().map(item -> {
            GoodsTypeDto dto = new GoodsTypeDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
        return goodsTypeDtoList;
    }

    /**
     * 获取分页货物类型数据
     *
     * @param page          页码
     * @param pageSize      页尺寸
     * @param name          货物类型名称
     * @param truckTypeId   车辆类型Id
     * @param truckTypeName 车辆类型名称
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "获取分页货物类型数据")
    public PageResponse<GoodsTypeDto> findByPage(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "pageSize") Integer pageSize,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "truckTypeId", required = false) String truckTypeId,
            @RequestParam(name = "truckTypeName", required = false) String truckTypeName) {
        IPage<DyGoodsType> goodsTypePage = goodsTypeService.findByPage(page, pageSize, name, truckTypeId, truckTypeName);
        List<GoodsTypeDto> goodsTypeDtoList = goodsTypePage.getRecords().stream().map(goodsType -> {
            GoodsTypeDto dto = new GoodsTypeDto();
            BeanUtils.copyProperties(goodsType, dto);
            dto.setTruckTypeIds(truckTypeGoodsTypeService.findAll(null, dto.getId()).stream().map(truckTypeGoodsType -> truckTypeGoodsType.getTruckTypeId()).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
        return PageResponse.<GoodsTypeDto>builder().items(goodsTypeDtoList).counts(goodsTypePage.getTotal()).page(page).pages(goodsTypePage.getPages()).pagesize(pageSize).build();
    }

    /**
     * 获取货物类型列表
     *
     * @return 货物类型列表
     */
    @GetMapping("")
    @ApiOperation(value = "获取货物类型列表")
    public List<GoodsTypeDto> findAll(@RequestParam(name = "ids", required = false) List<String> ids) {
        return goodsTypeService.findAll(ids).stream().map(pdGoodsType -> {
            GoodsTypeDto dto = new GoodsTypeDto();
            BeanUtils.copyProperties(pdGoodsType, dto);
            dto.setTruckTypeIds(truckTypeGoodsTypeService.findAll(null, dto.getId()).stream().map(truckTypeGoodsType -> truckTypeGoodsType.getTruckTypeId()).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 删除货物类型
     *
     * @param id 货物类型id
     * @return 返回信息
     */
    @PutMapping("/{id}/disable")
    @ApiOperation(value = "删除货物类型")
    public Result disable(@PathVariable(name = "id") String id) {
        DyGoodsType pdGoodsType = new DyGoodsType();
        pdGoodsType.setId(id);
        pdGoodsType.setStatus(Constant.DATA_DISABLE_STATUS);
        goodsTypeService.updateById(pdGoodsType);
        return Result.ok();
    }
}
