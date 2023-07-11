package com.neu.dy.api;

import java.util.List;

import com.neu.dy.authority.entity.common.Area;
import com.neu.dy.base.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "dy-auth-server", path = "/area")
public interface AreaApi {

  @PostMapping("")
  public void save(@RequestBody List<Area> areas);
  @GetMapping({"/{id}"})
  R<Area> get(@PathVariable Long paramLong);
  
  @GetMapping({"/code/{code}"})
  R<Area> getByCode(@PathVariable String code);
  
  @GetMapping
  R<List<Area>> findAll(@RequestParam(value = "parentId", required = false) Long paramLong, @RequestParam(value = "ids", required = false) List<Long> paramList);
}
