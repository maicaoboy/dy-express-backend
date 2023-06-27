package com.neu.dy.base.controller.truck;


import com.neu.dy.base.biz.service.truck.IDyTruckLicenseService;
import com.neu.dy.base.biz.service.truck.IDyTruckService;
import com.neu.dy.base.dto.truck.TruckLicenseDto;
import com.neu.dy.base.entity.truck.DyTruck;
import com.neu.dy.base.entity.truck.DyTruckLicense;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * TruckLicenseController
 */
@RestController
@RequestMapping("base/truck/license")
public class TruckLicenseController {
    @Autowired
    private IDyTruckLicenseService truckLicenseService;
    @Autowired
    private IDyTruckService truckService;

    /**
     * 保存车辆行驶证信息
     *
     * @param dto 车辆行驶证信息
     * @return 车辆行驶证信息
     */
    @PostMapping("")
    public TruckLicenseDto saveTruckLicense(@RequestBody TruckLicenseDto dto) {
        DyTruckLicense pdTruckLicense = new DyTruckLicense();
        BeanUtils.copyProperties(dto, pdTruckLicense);
        pdTruckLicense = truckLicenseService.saveTruckLicense(pdTruckLicense);
        if (dto.getId() == null) {
            DyTruck truck = new DyTruck();
            truck.setId(dto.getId());
            truck.setTruckLicenseId(pdTruckLicense.getId());
            truckService.saveTruck(truck);
        }
        BeanUtils.copyProperties(pdTruckLicense, dto);
        return dto;
    }

    /**
     * 根据id获取车辆行驶证详情
     *
     * @param id 车辆行驶证id
     * @return 车辆行驶证信息
     */
    @GetMapping("/{id}")
    public TruckLicenseDto fineById(@PathVariable(name = "id") String id) {
        DyTruckLicense pdTruckLicense = truckLicenseService.getById(id);
        TruckLicenseDto dto = new TruckLicenseDto();
        BeanUtils.copyProperties(pdTruckLicense, dto);
        return dto;
    }
}