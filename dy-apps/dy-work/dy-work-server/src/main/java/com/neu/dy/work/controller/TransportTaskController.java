package com.neu.dy.work.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.neu.dy.base.R;
import com.neu.dy.base.common.PageResponse;
import com.neu.dy.base.dto.base.GoodsTypeDto;
import com.neu.dy.base.entity.base.DyGoodsType;
import com.neu.dy.work.dto.TaskTransportDTO;
import com.neu.dy.work.entity.TaskTransport;
import com.neu.dy.work.entity.TransportOrderTask;
import com.neu.dy.work.service.TaskTransportService;
import com.neu.dy.work.service.TransportOrderTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 运输任务
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/transport-task")
public class TransportTaskController {
    @Autowired
    private TaskTransportService taskTransportService;
    @Autowired
    private TransportOrderTaskService transportOrderTaskService;

    /**
     * 新增运输任务
     *
     * @param dto 运输任务信息
     * @return 运输任务信息
     */
    @PostMapping("")
    public R save(@RequestBody TaskTransportDTO dto) {
        TaskTransport transportOrder = new TaskTransport();
        BeanUtils.copyProperties(dto, transportOrder);
        taskTransportService.saveTaskTransport(transportOrder);
        //保存与运单的关联关系,运输任务单与运输单为多对多关系
        if (dto.getTransportOrderIds() != null && dto.getTransportOrderIds().size() > 0) {
            List<TransportOrderTask> transportOrderTaskList = dto.getTransportOrderIds().stream().map(transportOrderId -> {
                TransportOrderTask transportOrderTask = new TransportOrderTask();
                transportOrderTask.setTransportOrderId(transportOrderId);
                transportOrderTask.setTransportTaskId(transportOrder.getId());
                return transportOrderTask;
            }).collect(Collectors.toList());
            transportOrderTaskService.batchSaveTransportOrder(transportOrderTaskList);
        }
        TaskTransportDTO result = new TaskTransportDTO();
        BeanUtils.copyProperties(transportOrder, result);
        result.setTransportOrderIds(dto.getTransportOrderIds());
        return R.success(result);
    }

    /**
     * 修改运输任务信息
     *
     * @param id  运输任务id
     * @param dto 运输任务信息
     * @return 运输任务信息
     */
    @PutMapping("/{id}")
    public R updateById(@PathVariable(name = "id") String id, @RequestBody TaskTransportDTO dto) {
        dto.setId(id);
        TaskTransport taskTransport = new TaskTransport();
        BeanUtils.copyProperties(dto, taskTransport);
        taskTransportService.updateById(taskTransport);
        transportOrderTaskService.del(null, id);
        //保存与运单的关联关系
        if (dto.getTransportOrderIds() != null && dto.getTransportOrderIds().size() > 0) {
            List<TransportOrderTask> transportOrderTaskList = dto.getTransportOrderIds().stream().map(transportOrderId -> {
                TransportOrderTask transportOrderTask = new TransportOrderTask();
                transportOrderTask.setTransportOrderId(transportOrderId);
                transportOrderTask.setTransportTaskId(id);
                return transportOrderTask;
            }).collect(Collectors.toList());
            transportOrderTaskService.batchSaveTransportOrder(transportOrderTaskList);
        }
        return R.success(dto);
    }

    /**
     * 获取运输任务分页数据
     *
     * @param dto 查询参数
     * @return 运输任务分页数据
     */
    @PostMapping("/page")
    public PageResponse<TaskTransportDTO> findByPage(@RequestBody TaskTransportDTO dto) {
        Integer page = 1;
        Integer pageSize = 10;
        List<TaskTransportDTO> dtoList = new ArrayList<>();
        Long total = 0L;
        Long pages = 0L;
        if (dto != null) {
            if (dto.getPage() != null) {
                page = dto.getPage();
            }
            if (dto.getPageSize() != null) {
                pageSize = dto.getPageSize();
            }
            IPage<TaskTransport> taskTransportIPage = taskTransportService.findByPage(page, pageSize, dto.getId(), dto.getStatus(), dto.getAssignedStatus());
            taskTransportIPage.getRecords().forEach(taskTransport -> {
                TaskTransportDTO resultDto = new TaskTransportDTO();
                BeanUtils.copyProperties(taskTransport, resultDto);
                List<String> transportOrderIds = new ArrayList<>();
                //查询运输任务与运单关系
                List<TransportOrderTask> transportOrderTaskList = transportOrderTaskService.findAll(null, taskTransport.getId());
                transportOrderIds.addAll(transportOrderTaskList.stream().map(transportOrderTask -> transportOrderTask.getTransportOrderId()).collect(Collectors.toList()));
                resultDto.setTransportOrderIds(transportOrderIds);
                resultDto.setTransportOrderCount(resultDto.getTransportOrderIds().size());
                dtoList.add(resultDto);
            });
            total = taskTransportIPage.getTotal();
            pages = taskTransportIPage.getPages();
        }
        return PageResponse.<TaskTransportDTO>builder().items(dtoList).pagesize(pageSize).page(page).counts(total)
                .pages(pages).build();
    }
    
    /**
     * 获取运单列表
     *
     * @param dto 查询条件
     * @return 运单列表
     */
    @PostMapping("/list")
    public R findAll(@RequestBody TaskTransportDTO dto) {
        List<TaskTransportDTO> collect = taskTransportService.findAll(dto.getIds(), dto.getId(), dto.getStatus(), dto).stream().map(taskTransport -> {
            TaskTransportDTO resultDto = new TaskTransportDTO();
            BeanUtils.copyProperties(taskTransport, resultDto);
            return resultDto;
        }).collect(Collectors.toList());
        return R.success(collect);
    }




    /**
     * 根据运单id或运输任务id获取运输任务列表
     *
     * @return 运输任务列表
     */
    @GetMapping("/listByOrderIdOrTaskId")
    public R findAllByOrderIdOrTaskId(@RequestParam(name = "transportOrderId", required = false) String transportOrderId,
                                                           @RequestParam(name = "taskTransportId", required = false) String taskTransportId) {
        List<TransportOrderTask> transportOrderTaskList = transportOrderTaskService.findAll(transportOrderId, taskTransportId);
        List<String> transportTaskIds = transportOrderTaskList.stream().map(transportOrderTask -> transportOrderTask.getTransportTaskId()).collect(Collectors.toList());
        List<TaskTransportDTO> dtoList = new ArrayList<>();
        if (transportTaskIds != null && transportTaskIds.size() > 0) {
            dtoList.addAll(taskTransportService.findAll(transportTaskIds, null, null, null).stream().map(taskTransport -> {
                TaskTransportDTO resultDto = new TaskTransportDTO();
                BeanUtils.copyProperties(taskTransport, resultDto);
                return resultDto;
            }).collect(Collectors.toList()));
        }
        return R.success(dtoList);
    }

    /**
     * 根据id获取运输任务信息
     *
     * @param id 运输任务id
     * @return 运输任务信息
     */
    @GetMapping("/{id}")
    public R findById(@PathVariable(name = "id") String id) {
        TaskTransportDTO dto = new TaskTransportDTO();
        TaskTransport taskTransport = taskTransportService.getById(id);
        if (taskTransport != null) {
            BeanUtils.copyProperties(taskTransport, dto);
            List<String> transportOrderIds = new ArrayList<>();
            List<TransportOrderTask> transportOrderTaskList = transportOrderTaskService.findAll(null, taskTransport.getId());
            transportOrderIds.addAll(transportOrderTaskList.stream().map(transportOrderTask -> transportOrderTask.getTransportOrderId()).collect(Collectors.toList()));
            dto.setTransportOrderIds(transportOrderIds);
            dto.setTransportOrderCount(dto.getTransportOrderIds().size());
        } else {
            dto = null;
        }
        return R.success(dto);
    }
}
