package com.neu.dy.dispatch.service;


import com.neu.dy.dto.OrderLineSimpleDTO;
import com.neu.dy.dto.OrderLineTripsTruckDriverDTO;
import com.neu.dy.work.dto.TaskTransportDTO;

import java.util.List;
import java.util.Map;

public interface BusinessOperationService {
    /**
     * 创建运输任务 更新运单
     *
     * @param orderLineSimpleDTOS
     * @return 运输任务id
     */
    Map<String, TaskTransportDTO> createTransportOrderTask(List<OrderLineSimpleDTO> orderLineSimpleDTOS);

    /**
     * 更新运输任务 创建司机作业单
     *
     * @param orderLineTripsTruckDriverDTOS
     * @param transportTaskMap
     */
    void updateTransportTask(List<OrderLineTripsTruckDriverDTO> orderLineTripsTruckDriverDTOS, Map<String, TaskTransportDTO> transportTaskMap);
}
