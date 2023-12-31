package com.neu.dy.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.work.entity.TaskPickupDispatch;
import com.neu.dy.work.enums.pickuptask.PickupDispatchTaskAssignedStatus;
import com.neu.dy.work.enums.pickuptask.PickupDispatchTaskStatus;
import com.neu.dy.work.mapper.TaskPickupDispatchMapper;
import com.neu.dy.work.service.TaskPickupDispatchService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 取件、派件任务信息表 服务实现类
 * </p>
 *
 * @author jpf
 * @since 2019-12-30
 */
@Service
public class TaskPickupDispatchServiceImpl extends ServiceImpl<TaskPickupDispatchMapper, TaskPickupDispatch> implements TaskPickupDispatchService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public TaskPickupDispatch saveTaskPickupDispatch(TaskPickupDispatch taskPickupDispatch) {
        taskPickupDispatch.setId(idGenerator.nextId(taskPickupDispatch) + "");
        taskPickupDispatch.setCreateTime(LocalDateTime.now());
        taskPickupDispatch.setStatus(PickupDispatchTaskStatus.PENDING.getCode());
        taskPickupDispatch.setAssignedStatus(PickupDispatchTaskAssignedStatus.TO_BE_DISTRIBUTED.getCode());
        save(taskPickupDispatch);
        return taskPickupDispatch;
    }

    @Override
    public IPage<TaskPickupDispatch> findByPage(Integer page, Integer pageSize, TaskPickupDispatch dispatch) {
        Page<TaskPickupDispatch> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<TaskPickupDispatch> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(dispatch.getCourierId())) {
            lambdaQueryWrapper.like(TaskPickupDispatch::getCourierId, dispatch.getCourierId());
        }
        if (dispatch.getAssignedStatus() != null) {
            lambdaQueryWrapper.eq(TaskPickupDispatch::getAssignedStatus, dispatch.getAssignedStatus());
        }
        if (dispatch.getTaskType() != null) {
            lambdaQueryWrapper.eq(TaskPickupDispatch::getTaskType, dispatch.getTaskType());
        }
        if (dispatch.getStatus() != null) {
            lambdaQueryWrapper.eq(TaskPickupDispatch::getStatus, dispatch.getStatus());
        }
        if (dispatch.getAssignedStatus() != null){
            lambdaQueryWrapper.eq(TaskPickupDispatch::getAssignedStatus, dispatch.getAssignedStatus());
        }
        lambdaQueryWrapper.orderByDesc(TaskPickupDispatch::getCreateTime);
        return page(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<TaskPickupDispatch> findAll(List<String> ids, List<String> orderIds, TaskPickupDispatch dispatch) {
        LambdaQueryWrapper<TaskPickupDispatch> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(TaskPickupDispatch::getId, ids);
        }
        if (orderIds != null && orderIds.size() > 0) {
            lambdaQueryWrapper.in(TaskPickupDispatch::getOrderId, orderIds);
        }
        if (dispatch.getAssignedStatus() != null) {
            lambdaQueryWrapper.eq(TaskPickupDispatch::getAssignedStatus, dispatch.getAssignedStatus());
        }
        if (dispatch.getTaskType() != null) {
            lambdaQueryWrapper.eq(TaskPickupDispatch::getTaskType, dispatch.getTaskType());
        }
        if (dispatch.getStatus() != null) {
            lambdaQueryWrapper.eq(TaskPickupDispatch::getStatus, dispatch.getStatus());
        }
        if (StringUtils.isNotEmpty(dispatch.getOrderId())) {
            lambdaQueryWrapper.like(TaskPickupDispatch::getOrderId, dispatch.getOrderId());
        }
        lambdaQueryWrapper.orderBy(true, false, TaskPickupDispatch::getId);
        return list(lambdaQueryWrapper);
    }
}
