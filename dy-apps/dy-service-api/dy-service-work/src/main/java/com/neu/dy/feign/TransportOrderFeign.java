package com.neu.dy.feign;

import com.neu.dy.base.R;
import com.neu.dy.work.dto.TaskTransportDTO;
import com.neu.dy.work.dto.TransportOrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "dy-work-server",path = "/transport-order")
public interface TransportOrderFeign {

    @GetMapping("orderIds")
    public R findByOrderIds(@RequestParam(name = "ids") List<String> ids);

    /**
     * 新增运单
     *
     * @param dto 运单信息
     * @return 运单信息
     */
    @PostMapping("")
    public TransportOrderDTO save(@RequestBody TransportOrderDTO dto);

}
