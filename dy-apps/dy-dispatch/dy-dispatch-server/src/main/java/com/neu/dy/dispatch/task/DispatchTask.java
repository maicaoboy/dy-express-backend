package com.neu.dy.dispatch.task;

import com.neu.dy.dispatch.service.BusinessOperationService;
import com.neu.dy.dispatch.service.TaskOrderClassifyService;
import com.neu.dy.dispatch.service.TaskRoutePlanningService;
import com.neu.dy.dispatch.service.TaskTripsSchedulingService;
import com.neu.dy.dto.OrderClassifyGroupDTO;
import com.neu.dy.dto.OrderLineSimpleDTO;
import com.neu.dy.dto.OrderLineTripsTruckDriverDTO;
import com.neu.dy.work.dto.TaskTransportDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component("dispatchTask")
public class DispatchTask {
    @Autowired
    private TaskOrderClassifyService orderClassifyService;

    @Autowired
    private TaskRoutePlanningService routePlanningService;

    @Autowired
    private BusinessOperationService businessOperationService;

    @Autowired
    private TaskTripsSchedulingService truckSchedulingService;

    public void run(String businessId, String params, String jobId, String logId){
        String LOGID = businessId + System.currentTimeMillis();
        log.info("智能调度正在进行，参数为：{},{},{},{}",businessId,params,jobId,logId);

        //订单分类
        List<OrderClassifyGroupDTO> classifyGroupDTOS = orderClassifyService.execute(businessId, jobId, logId);
        log.info("[{}] 订单分类完成:{}", LOGID, classifyGroupDTOS);
        // 路线规划
        List<OrderLineSimpleDTO> orderLineSimpleDTOS = routePlanningService.execute(classifyGroupDTOS, businessId, jobId, logId, params);
        log.info("[{}] 路线规划完成:{}", LOGID, orderLineSimpleDTOS);

        //  创建运输任务； 关联运单
        Map<String, TaskTransportDTO> transportTaskMap = businessOperationService.createTransportOrderTask(orderLineSimpleDTOS);
        log.info("[{}] 运输任务创建完成:{}", LOGID, transportTaskMap);

        // 车次 车辆 司机
        List<OrderLineTripsTruckDriverDTO> orderLineTripsTruckDriverDTOS = truckSchedulingService.execute(orderLineSimpleDTOS, businessId, jobId, logId, params);
        log.info("[{}] 规划车次车辆司机:{}", LOGID, orderLineTripsTruckDriverDTOS);

        //  完善运输任务信息； 创建司机作业任务；
        businessOperationService.updateTransportTask(orderLineTripsTruckDriverDTOS, transportTaskMap);
        log.info("[{}] 安排车次车辆司机,当前机构调度结束！！！！！", LOGID);
    }
}
