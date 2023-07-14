package com.neu.dy.base.controller.transportline;

import com.neu.dy.base.R;
import com.neu.dy.base.biz.service.transportline.IDyTransportTripsService;
import com.neu.dy.base.biz.service.transportline.IDyTransportTripsTruckDriverService;
import com.neu.dy.base.common.Result;
import com.neu.dy.base.dto.transportline.TransportTripsDto;
import com.neu.dy.base.dto.transportline.TransportTripsTruckDriverDto;
import com.neu.dy.base.entity.transportline.DyTransportTrips;
import com.neu.dy.base.entity.transportline.DyTransportTripsTruckDriver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TransportTripsController
 */
@RestController
@RequestMapping("base/transportLine/trips")
@Api(tags = "车次管理")
public class TransportTripsController {
    @Autowired
    private IDyTransportTripsService transportTripsService;
    @Autowired
    private IDyTransportTripsTruckDriverService transportTripsTruckDriverService;

    /**
     * 添加车次
     *
     * @param dto 车次信息
     * @return 车次信息
     */
    @PostMapping("")
    @ApiOperation(value = "添加车次")
    public R save(@RequestBody TransportTripsDto dto) {
        DyTransportTrips pdTransportTrips = new DyTransportTrips();
        BeanUtils.copyProperties(dto, pdTransportTrips);
        pdTransportTrips = transportTripsService.saveTransportTrips(pdTransportTrips);
        BeanUtils.copyProperties(pdTransportTrips, dto);
        return R.success();
    }

    /**
     * 根据id获取车次详情
     *
     * @param id 车次id
     * @return 车次信息
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取车次详情")
    public TransportTripsDto fineById(@PathVariable(name = "id") String id) {
        DyTransportTrips pdTransportTrips = transportTripsService.getById(id);
        TransportTripsDto dto = new TransportTripsDto();
        if (pdTransportTrips != null) {
            BeanUtils.copyProperties(pdTransportTrips, dto);
        }else{
            dto.setId(id);
        }
        return dto;
    }

    /**
     * 获取车次列表
     *
     * @param transportLineId 线路id
     * @param ids             车次id列表
     * @return 车次列表
     */
    @GetMapping("")
    @ApiOperation("根据线路id和车次id集合获取车次列表")
    public List<TransportTripsDto> findAll(@RequestParam(name = "transportLineId", required = false) String transportLineId, @RequestParam(name = "ids", required = false) List<String> ids) {
        return transportTripsService.findAll(transportLineId, ids).stream().map(pdTransportTrips -> {
            TransportTripsDto dto = new TransportTripsDto();
            BeanUtils.copyProperties(pdTransportTrips, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 更新车次信息
     *
     * @param id  车次id
     * @param dto 车次信息
     * @return 车次信息
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "更新车次信息")
    public R update(@PathVariable(name = "id") String id, @RequestBody TransportTripsDto dto) {
        dto.setId(id);
        DyTransportTrips pdTransportTrips = new DyTransportTrips();
        BeanUtils.copyProperties(dto, pdTransportTrips);
        transportTripsService.updateById(pdTransportTrips);
        return R.success();
    }

    /**
     * 删除车次信息
     *
     * @param id 车次信息
     * @return 返回信息
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除车次信息")
    public R disable(@PathVariable(name = "id") String id) {
        transportTripsService.disable(id);
        return R.success();
    }

    /**
     * 批量保存车次与车辆和司机关联关系
     *
     * @param dtoList 车次与车辆和司机关联关系
     * @return 返回信息
     */
    @PostMapping("{id}/truckDriver")
    @ApiOperation(value = "批量保存车次与车辆和司机关联关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "id", value = "车次id", required = true, dataType = "String"),
            @ApiImplicitParam(name= "dtoList", value = "车次与车辆和司机关联关系", required = true, dataType = "List")
    })
    public R batchSaveTruckDriver(@PathVariable("id") String transportTripsId, @RequestBody List<TransportTripsTruckDriverDto> dtoList) {
        transportTripsTruckDriverService.batchSave(transportTripsId, dtoList.stream().map(dto -> {
            dto.setTransportTripsId(transportTripsId);
            DyTransportTripsTruckDriver truckTransportTripsTruckDriver = new DyTransportTripsTruckDriver();
            BeanUtils.copyProperties(dto, truckTransportTripsTruckDriver);
            return truckTransportTripsTruckDriver;
        }).collect(Collectors.toList()));
        return R.success();
    }

    /**
     * 获取车次与车辆和司机关联关系列表
     *
     * @param transportTripsId 车次id
     * @param truckId          车辆id
     * @param userId           司机id
     * @return 车次与车辆和司机关联关系列表
     */
    @GetMapping("/truckDriver")
    @ApiOperation("获取车次与车辆和司机关联关系列表")
    public List<TransportTripsTruckDriverDto> findAllTruckDriverTransportTrips(@RequestParam(name = "transportTripsId", required = false) String transportTripsId, @RequestParam(name = "truckId", required = false) String truckId, @RequestParam(name = "userId", required = false) String userId) {
        return transportTripsTruckDriverService.findAll(transportTripsId, truckId, userId).stream().map(pdTransportTripsTruck -> {
            TransportTripsTruckDriverDto dto = new TransportTripsTruckDriverDto();
            BeanUtils.copyProperties(pdTransportTripsTruck, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}