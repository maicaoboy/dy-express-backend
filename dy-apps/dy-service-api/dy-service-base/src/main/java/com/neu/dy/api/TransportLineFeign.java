package com.neu.dy.api;

import com.neu.dy.base.dto.transportline.TransportLineDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("")
    @ApiOperation(value = "根据id、机构id、机构ids获取线路列表")
    public List<TransportLineDto> findAll(@RequestParam(name = "ids", required = false) List<String> ids,
                                          @RequestParam(name = "agencyId", required = false) String agencyId,
                                          @RequestParam(name = "agencyIds", required = false) List<String> agencyIds);
}
