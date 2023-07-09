package com.neu.dy.api;

import com.neu.dy.base.R;
import com.neu.dy.base.dto.angency.AgencyScopeDto;
import com.neu.dy.base.dto.user.CourierScopeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "dy-base-server",path = "/scope")
public interface AgencyScopeFeign {

    @PostMapping("/agency/batch")
    public R batchSaveAgencyScope(@RequestBody List<AgencyScopeDto> dtoList);

    @DeleteMapping("/agency")
    public R deleteAgencyScope(@RequestBody AgencyScopeDto dto);

    @GetMapping("/agency")
    public R findAllAgencyScope(@RequestParam(name = "areaId", required = false) String areaId, @RequestParam(name = "agencyId", required = false) String agencyId, @RequestParam(name = "agencyIds", required = false) List<String> agencyIds, @RequestParam(name = "areaIds", required = false) List<String> areaIds);

    @PostMapping("/courier/batch")
    public R batchSaveCourierScope(@RequestBody List<CourierScopeDto> dtoList);

    @DeleteMapping("/courier")
    public R deleteCourierScope(@RequestBody CourierScopeDto dto);

    @GetMapping("/courier")
    public R findAllCourierScope(@RequestParam(name = "areaId", required = false) String areaId, @RequestParam(name = "userId", required = false) String userId);

}
