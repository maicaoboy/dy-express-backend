package com.neu.dy.feign;

import com.neu.dy.base.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "dy-work-server",path = "/transport-order")
public interface TransportOrderFeign {

    @GetMapping("orderIds")
    public R findByOrderIds(@RequestParam(name = "ids") List<String> ids);
}
