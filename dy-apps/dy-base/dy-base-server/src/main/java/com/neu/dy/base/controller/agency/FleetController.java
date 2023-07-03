package com.neu.dy.base.controller.agency;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.neu.dy.base.R;
import com.neu.dy.base.biz.service.agency.IDyFleetService;
import com.neu.dy.base.common.PageResponse;
import com.neu.dy.base.common.Result;
import com.neu.dy.base.dto.angency.FleetDto;
import com.neu.dy.base.entity.agency.DyFleet;
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
    public R saveAgencyType(@RequestBody FleetDto dto) {
        DyFleet pdFleet = new DyFleet();
        BeanUtils.copyProperties(dto, pdFleet);
        pdFleet = fleetService.saveFleet(pdFleet);
        BeanUtils.copyProperties(pdFleet, dto);
        return R.success(dto);
    }

    /**
     * 根据id获取车队详情
     *
     * @param id 车队id
     * @return 车队信息
     */
    @GetMapping("/{id}")
    public R fineById(@PathVariable(name = "id") String id) {
        DyFleet pdFleet = fleetService.getById(id);
        FleetDto dto = new FleetDto();
        BeanUtils.copyProperties(pdFleet, dto);
        return R.success(dto);
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
    public R findAll(@RequestParam(value = "ids", required = false) List<String> ids, @RequestParam(value = "agencyId", required = false) String agencyId) {
        List<FleetDto> fleetDtoList = fleetService.findAll(ids, agencyId).stream().map(pdFleet -> {
            FleetDto dto = new FleetDto();
            BeanUtils.copyProperties(pdFleet, dto);
            return dto;
        }).collect(Collectors.toList());
        return R.success(fleetDtoList);

    }

    /**
     * 更新车队信息
     *
     * @param dto 车队信息
     * @return 车队信息
     */
    @PutMapping("/{id}")
    public R update(@PathVariable(name = "id") String id, @RequestBody FleetDto dto) {
        dto.setId(id);
        DyFleet pdFleet = new DyFleet();
        BeanUtils.copyProperties(dto, pdFleet);
        fleetService.updateById(pdFleet);
        return R.success(dto);
    }

    /**
     * 删除车队
     *
     * @param id 车队id
     * @return 返回信息
     */
    @PutMapping("/{id}/disable")
    public R disable(@PathVariable(name = "id") String id) {
        fleetService.disableById(id);
        return R.success();
    }
}