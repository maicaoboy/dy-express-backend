package com.neu.dy.base.controller.truck;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neu.dy.base.biz.service.truck.IDyTruckService;
import com.neu.dy.base.common.PageResponse;

import com.neu.dy.base.common.Result;
import com.neu.dy.base.dto.truck.TruckDto;
import com.neu.dy.base.entity.truck.DyTruck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TruckController
 */
@RestController
@RequestMapping("base/truck")
@Api(tags = "车辆管理")
public class TruckController {
    @Autowired
    private IDyTruckService truckService;

    /**
     * 添加车辆
     *
     * @param dto 车辆信息
     * @return 车辆信息
     */
    @PostMapping("")
    @ApiOperation(value = "添加车辆")
    public TruckDto saveTruck(@RequestBody TruckDto dto) {
        DyTruck dyTruck = new DyTruck();
        BeanUtils.copyProperties(dto, dyTruck);
        dyTruck = truckService.saveTruck(dyTruck);
        BeanUtils.copyProperties(dyTruck, dto);
        return dto;
    }

    /**
     * 根据id获取车辆详情
     *
     * @param id 车辆id
     * @return 车辆信息
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取车辆详情")
    public TruckDto fineById(@PathVariable(name = "id") String id) {
        DyTruck dyTruck = truckService.getById(id);
        if (ObjectUtils.isEmpty(dyTruck)) {
            return null;
        }
        TruckDto dto = new TruckDto();
        BeanUtils.copyProperties(dyTruck, dto);
        return dto;
    }

    /**
     * 获取车辆分页数据
     *
     * @param page         页码
     * @param pageSize     页尺寸
     * @param truckTypeId  车辆类型id
     * @param licensePlate 车牌号码
     * @return 车辆分页数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "获取车辆分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "truckTypeId", value = "车辆类型id",
                    required = false, type = "String"),
            @ApiImplicitParam(name = "licensePlate", value = "车辆牌照",
                    required = false, type = "String"),
            @ApiImplicitParam(name = "fleetId", value = "车队id",
                    required = false, type = "String"),
    })
    public PageResponse<TruckDto> findByPage(@RequestParam(name = "page") Integer page,
                                             @RequestParam(name = "pageSize") Integer pageSize,
                                             @RequestParam(name = "truckTypeId", required = false) String truckTypeId,
                                             @RequestParam(name = "licensePlate", required = false) String licensePlate,
                                             @RequestParam(name = "fleetId", required = false) String fleetId) {
        // TODO: 2020/1/9 通过车队名称查询待实现
        IPage<DyTruck> truckPage = truckService.findByPage(page, pageSize, truckTypeId, licensePlate, fleetId);
        List<TruckDto> dtoList = new ArrayList<>();
        truckPage.getRecords().forEach(dyTruck -> {
            TruckDto dto = new TruckDto();
            BeanUtils.copyProperties(dyTruck, dto);
            dtoList.add(dto);
        });
        return PageResponse.<TruckDto>builder().items(dtoList).pagesize(pageSize).page(page).counts(truckPage.getTotal())
                .pages(truckPage.getPages()).build();
    }

    /**
     * 统计车辆数量
     *
     * @param fleetId 车队id
     * @return 车辆数量
     */
    @GetMapping("/count")
    @ApiOperation(value = "根据车队id统计车辆数量")
    public Integer count(@RequestParam(name = "fleetId", required = false) String fleetId) {
        return truckService.count(fleetId);
    }

    /**
     * 获取车辆列表
     *
     * @param ids 车辆id列表
     * @return 车辆列表
     */
    @GetMapping("")
    @ApiOperation(value = "车辆id集合查询车辆列表")
    public List<TruckDto> findAll(@RequestParam(name = "ids", required = false) List<String> ids, @RequestParam(name = "fleetId", required = false) String fleetId) {
        return truckService.findAll(ids, fleetId).stream().map(dyTruck -> {
            TruckDto dto = new TruckDto();
            BeanUtils.copyProperties(dyTruck, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 更新车辆信息
     *
     * @param id  车辆id
     * @param dto 车辆信息
     * @return 车辆信息
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "更新车辆信息")
    public TruckDto update(@PathVariable(name = "id") String id, @RequestBody TruckDto dto) {
        dto.setId(id);
        DyTruck dyTruck = new DyTruck();
        BeanUtils.copyProperties(dto, dyTruck);
        truckService.updateById(dyTruck);
        return dto;
    }

    /**
     * 删除车辆
     *
     * @param id 车辆id
     * @return 返回信息
     */
    @PutMapping("/{id}/disable")
    @ApiOperation(value = "删除车辆")
    public Result disable(@PathVariable(name = "id") String id) {
        //TODO 检查车辆当前状态，如处于非空闲状态，则不允许删除
        truckService.disableById(id);
        return Result.ok();
    }
}