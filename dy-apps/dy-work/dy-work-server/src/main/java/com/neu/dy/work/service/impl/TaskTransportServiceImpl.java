package com.neu.dy.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.work.dto.TaskTransportDTO;
import com.neu.dy.work.entity.TaskTransport;
import com.neu.dy.work.entity.TransportOrder;
import com.neu.dy.work.enums.transporttask.TransportTaskAssignedStatus;
import com.neu.dy.work.enums.transporttask.TransportTaskLoadingStatus;
import com.neu.dy.work.enums.transporttask.TransportTaskStatus;
import com.neu.dy.work.mapper.TaskTransportMapper;
import com.neu.dy.work.service.TaskTransportService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 运输任务表 服务实现类
 * </p>
 *
 * @author jpf
 * @since 2019-12-29
 */
@Service
public class TaskTransportServiceImpl extends ServiceImpl<TaskTransportMapper, TaskTransport> implements TaskTransportService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public TaskTransport saveTaskTransport(TaskTransport taskTransport) {
        taskTransport.setId(idGenerator.nextId(taskTransport) + "");
        taskTransport.setCreateTime(LocalDateTime.now());
        taskTransport.setStatus(TransportTaskStatus.PENDING.getCode());
        taskTransport.setAssignedStatus(TransportTaskAssignedStatus.TO_BE_DISTRIBUTED.getCode());
        taskTransport.setLoadingStatus(TransportTaskLoadingStatus.EMPTY.getCode());
        save(taskTransport);
        return taskTransport;
    }

    @Override
    public IPage<TaskTransport> findByPage(Integer page, Integer pageSize, String id, Integer status, Integer assignedStatus) {
        Page<TaskTransport> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<TaskTransport> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(id)) {
            lambdaQueryWrapper.like(TaskTransport::getId, id);
        }
        if (status != null) {
            lambdaQueryWrapper.eq(TaskTransport::getStatus, status);
        }
        if (assignedStatus != null) {
        lambdaQueryWrapper.eq(TaskTransport::getAssignedStatus, assignedStatus);
    }
        return page(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<TaskTransport> findAll(List<String> ids, String id, Integer status, TaskTransportDTO dto) {
        LambdaQueryWrapper<TaskTransport> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(TaskTransport::getId, ids);
        }
        if (StringUtils.isNotEmpty(id)) {
            lambdaQueryWrapper.like(TaskTransport::getId, id);
        }
        if (status != null) {
            lambdaQueryWrapper.eq(TaskTransport::getStatus, status);
        }
        if (dto != null) {
            lambdaQueryWrapper.eq(StringUtils.isNotBlank(dto.getTruckId()), TaskTransport::getTruckId, dto.getTruckId());
        }
        return list(lambdaQueryWrapper);
    }
}
