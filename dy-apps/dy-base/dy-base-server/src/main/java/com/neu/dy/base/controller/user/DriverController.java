package com.neu.dy.base.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.neu.dy.base.biz.service.user.IDyTruckDriverService;
import com.neu.dy.base.common.PageResponse;
import com.neu.dy.base.dto.user.TruckDriverDto;
import com.neu.dy.base.entity.user.DyTruckDriver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 司机相关
 */
@RestController
@RequestMapping("sys/driver")
@Api(tags = "司机管理")
public class DriverController {
    @Autowired
    private IDyTruckDriverService truckDriverService;

    /**
     * 保存司机基本信息
     *
     * @param dto 司机基本信息
     * @return 返回信息
     */
    @PostMapping("")
    @ApiOperation(value = "添加司机基本信息")
    public TruckDriverDto saveDriver(@RequestBody TruckDriverDto dto) {
        DyTruckDriver driver = new DyTruckDriver();
        BeanUtils.copyProperties(dto, driver);
        truckDriverService.saveTruckDriver(driver);
        BeanUtils.copyProperties(driver, dto);
        return dto;
    }

    /**
     * 获取司机基本信息列表
     *
     * @param userIds 司机id列表
     * @return 司机基本信息列表
     */
    @GetMapping("")
    @ApiOperation(value = "司机id集合、车队id集合查询所有司机")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userIds", value = "司机id集合（非主键id）",
                    required = false, type = "List<String>"),
            @ApiImplicitParam(name = "fleetId", value = "车队id",
                    required = false, type = "String"),
    })
    public List<TruckDriverDto> findAllDriver(@RequestParam(name = "userIds", required = false) List<String> userIds, @RequestParam(name = "fleetId", required = false) String fleetId) {
        return truckDriverService.findAll(userIds, fleetId).stream().map(dyTruckDriver -> {
            TruckDriverDto dto = new TruckDriverDto();
            BeanUtils.copyProperties(dyTruckDriver, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 获取司机基本信息
     *
     * @param id 司机id
     * @return 司机基本信息
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询司机基本信息")
    public TruckDriverDto findOneDriver(@PathVariable(name = "id") String id) {
        DyTruckDriver dyTruckDriver = truckDriverService.findOne(id);
        TruckDriverDto dto = new TruckDriverDto();
        if (dyTruckDriver != null) {
            BeanUtils.copyProperties(dyTruckDriver, dto);
        }
        return dto;
    }

    /**
     * 统计司机数量
     *
     * @param fleetId 车队id
     * @return 司机数量
     */
    @GetMapping("/count")
    @ApiOperation(value = "统计司机数量")
    public Integer count(@RequestParam(name = "fleetId", required = false) String fleetId) {
        return truckDriverService.count(fleetId);
    }

    /**
     * 获取司机分页数据
     *
     * @param page     页码
     * @param pageSize 页尺寸
     * @param fleetId  车队id
     * @return 司机分页数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "获取司机分页数据")
    public PageResponse<TruckDriverDto> findByPage(@RequestParam(name = "page") Integer page,
                                                   @RequestParam(name = "pageSize") Integer pageSize,
                                                   @RequestParam(name = "fleetId", required = false) String fleetId) {
        IPage<DyTruckDriver> truckPage = truckDriverService.findByPage(page, pageSize, fleetId);
        List<TruckDriverDto> dtoList = new ArrayList<>();
        truckPage.getRecords().forEach(dyTruckDriver -> {
            TruckDriverDto dto = new TruckDriverDto();
            BeanUtils.copyProperties(dyTruckDriver, dto);
            dtoList.add(dto);
        });
        return PageResponse.<TruckDriverDto>builder().items(dtoList).pagesize(pageSize).page(page).counts(truckPage.getTotal())
                .pages(truckPage.getPages()).build();
    }


    @GetMapping("/findAll")
    @ApiOperation(value = "司机id集合查询所有司机")
    public List<TruckDriverDto> findAll(@RequestParam(name = "ids", required = false) List<String> ids) {
        LambdaQueryWrapper<DyTruckDriver> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(DyTruckDriver::getId, ids);
        return truckDriverService.list(wrapper).stream().map(dyTruckDriver -> {
            TruckDriverDto dto = new TruckDriverDto();
            BeanUtils.copyProperties(dyTruckDriver, dto);
            return dto;
        }).collect(Collectors.toList());
    }

}
