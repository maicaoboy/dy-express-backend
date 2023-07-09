package com.neu.dy.api;

import com.neu.dy.base.dto.transportline.TransportTripsDto;
import com.neu.dy.base.dto.transportline.TransportTripsTruckDriverDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "dy-base-server",path = "/base/transportLine/trips")
public interface TransportTripsFeign {

    @GetMapping("/{id}")
    public TransportTripsDto fineById(@PathVariable(name = "id") String id);

    @GetMapping("")
    public List<TransportTripsDto> findAll(@RequestParam(name = "transportLineId", required = false) String transportLineId, @RequestParam(name = "ids", required = false) List<String> ids);

    /**
     * 获取车次与车辆和司机关联关系列表
     *
     * @param transportTripsId 车次id
     * @param truckId          车辆id
     * @param userId           司机id
     * @return 车次与车辆和司机关联关系列表
     */
    @GetMapping("/truckDriver")
    public List<TransportTripsTruckDriverDto> findAllTruckDriverTransportTrips(@RequestParam(name = "transportTripsId", required = false) String transportTripsId, @RequestParam(name = "truckId", required = false) String truckId, @RequestParam(name = "userId", required = false) String userId);
}
