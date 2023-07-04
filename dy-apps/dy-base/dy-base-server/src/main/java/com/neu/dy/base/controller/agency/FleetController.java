package com.neu.dy.base.controller.agency;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.neu.dy.base.biz.service.agency.IDyFleetService;
import com.neu.dy.base.common.PageResponse;
import com.neu.dy.base.common.Result;
import com.neu.dy.base.dto.angency.FleetDto;
import com.neu.dy.base.entity.agency.DyFleet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FleetController
 */
@RestController
@RequestMapping("sys/agency/fleet")
@Api(tags = "车队管理")
public class FleetController {
    @Autowired
    private IDyFleetService fleetService;

    /**
     * 添加车队
     *
     * @param dto 车队信息
     * @return 车队信息
     */
    @PostMapping("")
    @ApiOperation(value = "添加车队")
    public FleetDto saveAgencyType(@RequestBody FleetDto dto) {
        DyFleet dyFleet = new DyFleet();
        BeanUtils.copyProperties(dto, dyFleet);
        dyFleet = fleetService.saveFleet(dyFleet);
        BeanUtils.copyProperties(dyFleet, dto);
        return dto;
    }

    /**
     * 根据id获取车队详情
     *
     * @param id 车队id
     * @return 车队信息
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取车队详情")
    public FleetDto fineById(@PathVariable(name = "id") String id) {
        DyFleet dyFleet = fleetService.getById(id);
        FleetDto dto = new FleetDto();
        BeanUtils.copyProperties(dyFleet, dto);
        return dto;
    }

    /**
     * 获取车队分页数据
     *
     * @param page        页码
     * @param pageSize    页尺寸
     * @param name        车队名称
     * @param fleetNumber 车队编号
     * @param manager     负责人id
     * @return 车队分页数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "获取车队分页数据")
    public PageResponse<FleetDto> findByPage(@RequestParam(name = "page") Integer page,
                                             @RequestParam(name = "pageSize") Integer pageSize,
                                             @RequestParam(name = "name", required = false) String name,
                                             @RequestParam(name = "fleetNumber", required = false) String fleetNumber,
                                             @RequestParam(name = "manager", required = false) String manager) {
        IPage<DyFleet> fleetPage = fleetService.findByPage(page, pageSize, name, fleetNumber, manager);
        List<FleetDto> dtoList = new ArrayList<>();
        fleetPage.getRecords().forEach(DyFleet -> {
            FleetDto dto = new FleetDto();
            BeanUtils.copyProperties(DyFleet, dto);
            dtoList.add(dto);
        });
        return PageResponse.<FleetDto>builder().items(dtoList).pagesize(pageSize).page(page).counts(fleetPage.getTotal())
                .pages(fleetPage.getPages()).build();
    }

    /**
     * 获取车队列表
     *
     * @param ids 车队Id列表
     * @return 车队列表
     */
    @GetMapping("")
    @ApiOperation(value = "获取车队列表")
    public List<FleetDto> findAll(@RequestParam(value = "ids", required = false) List<String> ids, @RequestParam(value = "agencyId", required = false) String agencyId) {
        return fleetService.findAll(ids, agencyId).stream().map(dyFleet -> {
            FleetDto dto = new FleetDto();
            BeanUtils.copyProperties(dyFleet, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 更新车队信息
     *
     * @param dto 车队信息
     * @return 车队信息
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "更新车队信息")
    public FleetDto update(@PathVariable(name = "id") String id, @RequestBody FleetDto dto) {
        dto.setId(id);
        DyFleet dyFleet = new DyFleet();
        BeanUtils.copyProperties(dto, dyFleet);
        fleetService.updateById(dyFleet);
        return dto;
    }

    /**
     * 删除车队
     *
     * @param id 车队id
     * @return 返回信息
     */
    @PutMapping("/{id}/disable")
    @ApiOperation(value = "删除车队")
    public Result disable(@PathVariable(name = "id") String id) {
        fleetService.disableById(id);
        return Result.ok();
    }
}