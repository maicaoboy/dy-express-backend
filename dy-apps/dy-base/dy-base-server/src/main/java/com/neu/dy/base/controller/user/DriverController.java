package com.neu.dy.base.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.neu.dy.base.biz.service.user.IDyTruckDriverLicenseService;
import com.neu.dy.base.biz.service.user.IDyTruckDriverService;
import com.neu.dy.base.common.PageResponse;
import com.neu.dy.base.dto.user.TruckDriverDto;
import com.neu.dy.base.dto.user.TruckDriverLicenseDto;
import com.neu.dy.base.entity.user.DyTruckDriver;
import com.neu.dy.base.entity.user.DyTruckDriverLicense;
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
public class DriverController {
    @Autowired
    private IDyTruckDriverService truckDriverService;
    @Autowired
    private IDyTruckDriverLicenseService truckDriverLicenseService;

    /**
     * 保存司机基本信息
     *
     * @param dto 司机基本信息
     * @return 返回信息
     */
    @PostMapping("")
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
    public List<TruckDriverDto> findAllDriver(@RequestParam(name = "userIds", required = false) List<String> userIds, @RequestParam(name = "fleetId", required = false) String fleetId) {
        return truckDriverService.findAll(userIds, fleetId).stream().map(pdTruckDriver -> {
            TruckDriverDto dto = new TruckDriverDto();
            BeanUtils.copyProperties(pdTruckDriver, dto);
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
    public TruckDriverDto findOneDriver(@PathVariable(name = "id") String id) {
        DyTruckDriver pdTruckDriver = truckDriverService.findOne(id);
        TruckDriverDto dto = new TruckDriverDto();
        if (pdTruckDriver != null) {
            BeanUtils.copyProperties(pdTruckDriver, dto);
        }
        return dto;
    }

    /**
     * 保存司机驾驶证信息
     *
     * @param dto 司机驾驶证信息
     * @return 返回信息
     */
    @PostMapping("/driverLicense")
    public TruckDriverLicenseDto saveDriverLicense(@RequestBody TruckDriverLicenseDto dto) {
        DyTruckDriverLicense driverLicense = new DyTruckDriverLicense();
        BeanUtils.copyProperties(dto, driverLicense);
        truckDriverLicenseService.saveTruckDriverLicense(driverLicense);
        BeanUtils.copyProperties(driverLicense, dto);
        return dto;
    }

    /**
     * 获取司机驾驶证信息
     *
     * @param id 司机id
     * @return 司机驾驶证信息
     */
    @GetMapping("/{id}/driverLicense")
    public TruckDriverLicenseDto findOneDriverLicense(@PathVariable(name = "id") String id) {
        DyTruckDriverLicense driverLicense = truckDriverLicenseService.findOne(id);
        TruckDriverLicenseDto dto = new TruckDriverLicenseDto();
        if (driverLicense != null) {
            BeanUtils.copyProperties(driverLicense, dto);
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
    public PageResponse<TruckDriverDto> findByPage(@RequestParam(name = "page") Integer page,
                                                   @RequestParam(name = "pageSize") Integer pageSize,
                                                   @RequestParam(name = "fleetId", required = false) String fleetId) {
        IPage<DyTruckDriver> truckPage = truckDriverService.findByPage(page, pageSize, fleetId);
        List<TruckDriverDto> dtoList = new ArrayList<>();
        truckPage.getRecords().forEach(pdTruckDriver -> {
            TruckDriverDto dto = new TruckDriverDto();
            BeanUtils.copyProperties(pdTruckDriver, dto);
            dtoList.add(dto);
        });
        return PageResponse.<TruckDriverDto>builder().items(dtoList).pagesize(pageSize).page(page).counts(truckPage.getTotal())
                .pages(truckPage.getPages()).build();
    }


    @GetMapping("/findAll")
    public List<TruckDriverDto> findAll(@RequestParam(name = "ids", required = false) List<String> ids) {
        LambdaQueryWrapper<DyTruckDriver> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(DyTruckDriver::getId, ids);
        return truckDriverService.list(wrapper).stream().map(pdTruckDriver -> {
            TruckDriverDto dto = new TruckDriverDto();
            BeanUtils.copyProperties(pdTruckDriver, dto);
            return dto;
        }).collect(Collectors.toList());
    }

}
