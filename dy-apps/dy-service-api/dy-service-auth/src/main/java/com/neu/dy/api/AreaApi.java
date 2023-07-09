package com.neu.dy.api;

import java.util.List;

import com.neu.dy.authority.entity.common.Area;
import com.neu.dy.base.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "dy-auth-server", path = "/area")
public interface AreaApi {
  @GetMapping({"/{id}"})
  R<Area> get(@PathVariable Long paramLong);
  
  @GetMapping({"/code/{code}"})
  R<Area> getByCode(@PathVariable String paramString);
  
  @GetMapping
  R<List<Area>> findAll(@RequestParam(value = "parentId", required = false) Long paramLong, @RequestParam(value = "ids", required = false) List<Long> paramList);
}
