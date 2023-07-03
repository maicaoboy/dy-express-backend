package com.neu.dy.work.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neu.dy.base.R;
import com.neu.dy.work.dto.TaskPickupDispatchDTO;
import com.neu.dy.work.entity.TaskPickupDispatch;
import com.neu.dy.work.enums.pickuptask.PickupDispatchTaskAssignedStatus;
import com.neu.dy.work.service.TaskPickupDispatchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 取件、派件任务
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/pickup-dispatch-task")
public class PickupDispatchTaskController {
    @Autowired
    private TaskPickupDispatchService taskPickupDispatchService;

    /**
     * 新增取派件任务
     *
     * @param dto 取派件任务信息
     * @return 取派件任务信息
     */
    @PostMapping("")
    public R save(@RequestBody TaskPickupDispatchDTO dto) {
        TaskPickupDispatch dispatch = new TaskPickupDispatch();
        BeanUtils.copyProperties(dto, dispatch);
        log.info("新增取派件任务:{}    {}", dto, dispatch);
        taskPickupDispatchService.saveTaskPickupDispatch(dispatch);
        TaskPickupDispatchDTO result = new TaskPickupDispatchDTO();
        BeanUtils.copyProperties(dispatch, result);
        return R.success(result);
    }

    /**
     * 修改取派件任务信息
     *
     * @param id  取派件任务id
     * @param dto 取派件任务信息
     * @return 取派件任务信息
     */
    @PutMapping("/{id}")
    public R updateById(@PathVariable(name = "id") String id, @RequestBody TaskPickupDispatchDTO dto) {
        dto.setId(id);
        TaskPickupDispatch dispatch = new TaskPickupDispatch();
        BeanUtils.copyProperties(dto, dispatch);
        if (StringUtils.isNotEmpty(dispatch.getCourierId())) {
            dispatch.setAssignedStatus(PickupDispatchTaskAssignedStatus.DISTRIBUTED.getCode());
        }
        taskPickupDispatchService.updateById(dispatch);
        return R.success(dto);
    }

    /**
     * 获取取派件任务分页数据
     *
     * @param dto 查询条件
     * @return 取派件分页数据
     */
    @PostMapping("/page")
    public R findByPage(@RequestBody TaskPickupDispatchDTO dto) {
        if (dto.getPage() == null) {
            dto.setPage(1);
        }
        if (dto.getPageSize() == null) {
            dto.setPageSize(10);
        }
        TaskPickupDispatch queryTask = new TaskPickupDispatch();
        BeanUtils.copyProperties(dto, queryTask);
        IPage<TaskPickupDispatch> orderIPage = taskPickupDispatchService.findByPage(dto.getPage(), dto.getPageSize(), queryTask);
        return R.success(orderIPage);
    }

    /**
     * 获取取派件任务列表
     *
     * @param dto 查询条件
     * @return 取派件任务列表
     */
    @PostMapping("/list")
    public R findAll(@RequestBody TaskPickupDispatchDTO dto) {
        TaskPickupDispatch queryTask = new TaskPickupDispatch();
        BeanUtils.copyProperties(dto, queryTask);
        List<TaskPickupDispatchDTO> taskPickupDispatchDTOS = taskPickupDispatchService.findAll(dto.getIds(), dto.getOrderIds(), queryTask).stream().map(taskPickupDispatch -> {
            TaskPickupDispatchDTO resultDto = new TaskPickupDispatchDTO();
            BeanUtils.copyProperties(taskPickupDispatch, resultDto);
            return resultDto;
        }).collect(Collectors.toList());
        return R.success(taskPickupDispatchDTOS);
    }

    /**
     * 根据id获取取派件任务信息
     *
     * @param id 任务Id
     * @return 任务详情
     */
    @GetMapping("/{id}")
    public R findById(@PathVariable(name = "id") String id) {
        TaskPickupDispatchDTO dto = new TaskPickupDispatchDTO();
        TaskPickupDispatch dispatch = taskPickupDispatchService.getById(id);
        if (dispatch != null) {
            BeanUtils.copyProperties(dispatch, dto);
        } else {
            dto = null;
        }
        return R.success(dto);
    }

    /**
     * 根据订单id获取取派件任务信息
     *
     * @param orderId 订单Id
     * @return 任务详情
     */
    @GetMapping("/orderId/{orderId}/{taskType}")
    public R findByOrderId(@PathVariable("orderId") String orderId, @PathVariable("taskType") Integer taskType) {
        TaskPickupDispatchDTO dto = new TaskPickupDispatchDTO();

        LambdaQueryWrapper<TaskPickupDispatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TaskPickupDispatch::getOrderId, orderId);
        wrapper.eq(TaskPickupDispatch::getTaskType, taskType);
        TaskPickupDispatch dispatch = taskPickupDispatchService.getOne(wrapper);
        if (dispatch != null) {
            BeanUtils.copyProperties(dispatch, dto);
        } else {
            dto = null;
        }
        return R.success(dto);
    }

}
