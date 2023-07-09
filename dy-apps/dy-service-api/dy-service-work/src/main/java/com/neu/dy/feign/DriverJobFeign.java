package com.neu.dy.feign;

import com.neu.dy.base.R;
import com.neu.dy.work.dto.DriverJobDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "dy-work-server",path = "/driver-job")
public interface DriverJobFeign {

    @PostMapping("")
    public R save(@RequestBody DriverJobDTO driverJobDTO);
}
