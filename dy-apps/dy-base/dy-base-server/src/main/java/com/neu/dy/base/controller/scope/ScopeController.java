package com.neu.dy.base.controller.scope;



import com.neu.dy.base.biz.service.agency.IDyAgencyScopeService;
import com.neu.dy.base.biz.service.user.IDyCourierScopeService;
import com.neu.dy.base.common.Result;
import com.neu.dy.base.dto.angency.AgencyScopeDto;
import com.neu.dy.base.dto.user.CourierScopeDto;
import com.neu.dy.base.entity.agency.DyAgencyScope;
import com.neu.dy.base.entity.user.DyCourierScope;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务范围表 前端控制器
 * </p>
 *
 * @author jpf
 * @since 2019-12-23
 */
@RestController
@RequestMapping("scope")
@Log
public class ScopeController {
    @Autowired
    private IDyAgencyScopeService agencyScopService;
    @Autowired
    private IDyCourierScopeService courierScopeService;

    /**
     * 批量保存机构业务范围
     *
     * @param dtoList 机构业务范围信息
     * @return 返回信息
     */
    @PostMapping("/agency/batch")
    public Result batchSaveAgencyScope(@RequestBody List<AgencyScopeDto> dtoList) {
        agencyScopService.batchSave(dtoList.stream().map(dto -> {
            DyAgencyScope scope = new DyAgencyScope();
            BeanUtils.copyProperties(dto, scope);
            return scope;
        }).collect(Collectors.toList()));
        return Result.ok();
    }

    /**
     * 删除机构业务范围信息
     *
     * @param dto 参数
     * @return 返回信息
     */
    @DeleteMapping("/agency")
    public Result deleteAgencyScope(@RequestBody AgencyScopeDto dto) {
        agencyScopService.delete(dto.getAreaId(), dto.getAgencyId());
        return Result.ok();
    }

    /**
     * 获取机构业务范围列表
     *
     * @param areaId   行政区域id
     * @param agencyId 机构id
     * @return 机构业务范围列表
     */
    @GetMapping("/agency")
    public List<AgencyScopeDto> findAllAgencyScope(@RequestParam(name = "areaId", required = false) String areaId, @RequestParam(name = "agencyId", required = false) String agencyId, @RequestParam(name = "agencyIds", required = false) List<String> agencyIds, @RequestParam(name = "areaIds", required = false) List<String> areaIds) {
        return agencyScopService.findAll(areaId, agencyId, agencyIds, areaIds).stream().map(scope -> {
            AgencyScopeDto dto = new AgencyScopeDto();
            BeanUtils.copyProperties(scope, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 批量保存快递员业务范围
     *
     * @param dtoList 快递员业务范围信息
     * @return 返回信息
     */
    @PostMapping("/courier/batch")
    public Result batchSaveCourierScope(@RequestBody List<CourierScopeDto> dtoList) {
        courierScopeService.batchSave(dtoList.stream().map(dto -> {
            DyCourierScope scope = new DyCourierScope();
            BeanUtils.copyProperties(dto, scope);
            return scope;
        }).collect(Collectors.toList()));
        return Result.ok();
    }

    /**
     * 删除快递员业务范围信息
     *
     * @param dto 参数
     * @return 返回信息
     */
    @DeleteMapping("/courier")
    public Result deleteCourierScope(@RequestBody CourierScopeDto dto) {
        courierScopeService.delete(dto.getAreaId(), dto.getUserId());
        return Result.ok();
    }

    /**
     * 获取快递员业务范围列表
     *
     * @param areaId 行政区域id
     * @param userId 快递员id
     * @return 快递员业务范围列表
     */
    @GetMapping("/courier")
    public List<CourierScopeDto> findAllCourierScope(@RequestParam(name = "areaId", required = false) String areaId, @RequestParam(name = "userId", required = false) String userId) {
        return courierScopeService.findAll(areaId, userId).stream().map(scope -> {
            CourierScopeDto dto = new CourierScopeDto();
            BeanUtils.copyProperties(scope, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
