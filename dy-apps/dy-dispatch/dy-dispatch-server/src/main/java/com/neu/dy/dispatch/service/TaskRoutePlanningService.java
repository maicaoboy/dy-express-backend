package com.neu.dy.dispatch.service;



import com.neu.dy.dto.OrderClassifyGroupDTO;
import com.neu.dy.dto.OrderLineSimpleDTO;

import java.util.List;

/**
 * 路线规划
 */
public interface TaskRoutePlanningService {
    /**
     * 执行
     * @return
     */
    List<OrderLineSimpleDTO> execute(List<OrderClassifyGroupDTO> orderClassifyGroupDTOS, String agencyId, String jobId, String logId, String params);
}
