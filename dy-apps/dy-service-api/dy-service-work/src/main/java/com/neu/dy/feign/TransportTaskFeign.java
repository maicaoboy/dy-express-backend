package com.neu.dy.feign;

import com.neu.dy.base.R;
import com.neu.dy.work.dto.TaskTransportDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "dy-work-server",path = "/transport-task")
public interface TransportTaskFeign {
    @PostMapping("")
    public R save(@RequestBody TaskTransportDTO dto);

    @PutMapping("/{id}")
    public R updateById(@PathVariable(name = "id") String id, @RequestBody TaskTransportDTO dto);
}
