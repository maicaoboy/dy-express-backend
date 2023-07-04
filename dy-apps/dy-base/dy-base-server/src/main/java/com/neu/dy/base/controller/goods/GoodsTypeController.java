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
        //保存货物类型到数据库
        DyGoodsType dyGoodsType = new DyGoodsType();
        /*使用org.apache.commons.beanutils.BeanUtils进行copy对象时，被copy的对象（source/orig）中包含的字段目标对象（target/dest）
        必须包含与之对应的字段，否则会报错，使用org.springframework.beans.BeanUtils.copyProperties则不会报错，只是不会copy对应的字段
         */
        BeanUtils.copyProperties(dto, dyGoodsType);
        dyGoodsType = goodsTypeService.saveGoodsType(dyGoodsType);
        String goodsTypeId = dyGoodsType.getId();
        //通常情况下，map()方法用于将一个流中的元素映射为另一种类型
        if (dto.getTruckTypeIds() != null) {
            List<DyTruckTypeGoodsType> list = dto.getTruckTypeIds().stream().map(truckTypeId -> {
                DyTruckTypeGoodsType truckTypeGoodsType = new DyTruckTypeGoodsType();
                truckTypeGoodsType.setTruckTypeId(truckTypeId);
                truckTypeGoodsType.setGoodsTypeId(goodsTypeId);
                return truckTypeGoodsType;
            }).collect(Collectors.toList());
            truckTypeGoodsTypeService.batchSave(list);
        }
        //相较于之前添加了id
        BeanUtils.copyProperties(dyGoodsType, dto);
        return dto;
    }

    /**
     * 根据id查询货物类型
     *
     * @param id 货物类型id
     * @return 货物类型信息
     */
    /*
    @PathVariable是Spring框架中的一个注解标签，用于处理RESTful风格的URL中的路径参数。
    它用于从URL中提取参数值并将其绑定到方法的参数上。
    在Spring MVC中，当使用@RequestMapping注解定义Controller的方法时，
    可以在方法参数上使用@PathVariable注解来指定路径参数。
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "通过id查询商品")
    public GoodsTypeDto fineById(@PathVariable(name = "id") String id) {
        DyGoodsType dyGoodsType = goodsTypeService.getById(id);
        GoodsTypeDto dto = null;
        if (dyGoodsType != null) {
            dto = new GoodsTypeDto();
            BeanUtils.copyProperties(dyGoodsType, dto);
            dto.setTruckTypeIds(truckTypeGoodsTypeService.findAll(null, dto.getId()).stream().map(truckTypeGoodsType ->
                    truckTypeGoodsType.getTruckTypeId()).collect(Collectors.toList()));
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
        //mybatis-plus实现分页
        IPage<DyGoodsType> goodsTypePage = goodsTypeService.findByPage(page, pageSize, name, truckTypeId, truckTypeName);
        //类型转换与关联信息查询
        List<GoodsTypeDto> goodsTypeDtoList = goodsTypePage.getRecords().stream().map(goodsType -> {
            GoodsTypeDto dto = new GoodsTypeDto();
            BeanUtils.copyProperties(goodsType, dto);
            dto.setTruckTypeIds(truckTypeGoodsTypeService.findAll(null, dto.getId()).stream().map(truckTypeGoodsType ->
                    truckTypeGoodsType.getTruckTypeId()).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
        return PageResponse.<GoodsTypeDto>builder().items(goodsTypeDtoList).counts(goodsTypePage.getTotal()).page(page).pages(goodsTypePage.getPages()).pagesize(pageSize).build();
    }

    /**
     * 获取货物类型列表，多个id的集合查询
     *
     * @return 货物类型列表
     */
    @GetMapping("")
    @ApiOperation(value = "货物id集合查询货物类型")
    public List<GoodsTypeDto> findAll(@RequestParam(name = "ids", required = false) List<String> ids) {
        List<DyGoodsType> dyGoodsTypeList = goodsTypeService.findAll(ids);
        if(dyGoodsTypeList!=null&&dyGoodsTypeList.size()>0){
            //list内DyGoodsType转成dto
           return dyGoodsTypeList.stream().map(dyGoodsType -> {
                List<DyTruckTypeGoodsType> dyTruckTypeGoodsTypeList = truckTypeGoodsTypeService.findAll(null, dyGoodsType.getId());
                //将dyTruckTypeGoodsType转成其id
                List<String> truckTypeIds = dyTruckTypeGoodsTypeList.stream().map(dyTruckTypeGoodsType -> dyTruckTypeGoodsType.getTruckTypeId()).collect(Collectors.toList());
                GoodsTypeDto goodsTypeDto = new GoodsTypeDto();
                BeanUtils.copyProperties(dyGoodsType,goodsTypeDto);
                goodsTypeDto.setTruckTypeIds(truckTypeIds);
                return goodsTypeDto;
            }).collect(Collectors.toList());
        }
        return null;
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
        DyGoodsType dyGoodsType = new DyGoodsType();
        dyGoodsType.setId(id);
        dyGoodsType.setStatus(Constant.DATA_DISABLE_STATUS);
        goodsTypeService.updateById(dyGoodsType);
        return Result.ok();
    }
}
