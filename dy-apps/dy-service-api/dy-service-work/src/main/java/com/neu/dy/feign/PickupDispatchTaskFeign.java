package com.neu.dy.feign;

import com.neu.dy.base.R;
import com.neu.dy.work.dto.TaskPickupDispatchDTO;
import com.neu.dy.work.dto.TaskTransportDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Classname PickupDispatchTaskFeign
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/7/19 14:38
 * @Created by maicaoboy
 */
@FeignClient(name = "dy-work-server",path = "/pickup-dispatch-task")
public interface PickupDispatchTaskFeign {
    @PostMapping
    R<TaskPickupDispatchDTO> save(@RequestBody TaskPickupDispatchDTO dto);
}

