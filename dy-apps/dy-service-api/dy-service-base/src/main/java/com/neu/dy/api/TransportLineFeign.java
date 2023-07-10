package com.neu.dy.api;

import com.neu.dy.base.dto.transportline.TransportLineDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "dy-base-server",path = "/base/transportLine")
public interface TransportLineFeign {

    /**
     * 获取线路列表
     *
     * @return 线路列表
     */
    @PostMapping("list")
    public List<TransportLineDto> list(@RequestBody TransportLineDto transportLineDto);

    @GetMapping("/{id}")
    public TransportLineDto fineById(@PathVariable(name = "id") String id);
}
