package com.neu.dy.dispatch.service;

import com.neu.dy.dto.OrderLineSimpleDTO;
import com.neu.dy.dto.OrderLineTripsTruckDriverDTO;

import java.util.List;

/**
 * 车辆调度
 */
public interface TaskTripsSchedulingService {
    /**
     * 执行
     *
     * @param orderLineSimpleDTOS
     * @param businessId
     * @param jobId
     * @param logId
     * @return
     */
    List<OrderLineTripsTruckDriverDTO> execute(List<OrderLineSimpleDTO> orderLineSimpleDTOS, String businessId, String jobId, String logId, String agencyId);
}
