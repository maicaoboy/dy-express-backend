package com.neu.dy.work.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neu.dy.base.R;
import com.neu.dy.work.dto.DriverJobDTO;
import com.neu.dy.work.entity.DriverJob;
import com.neu.dy.work.entity.TransportOrder;
import com.neu.dy.work.entity.TransportOrderTask;
import com.neu.dy.work.service.DriverJobService;
import com.neu.dy.work.service.TransportOrderService;
import com.neu.dy.work.service.TransportOrderTaskService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/driver-job")
public class DriverJobController {

    @Autowired
    private DriverJobService driverJobService;
    @Autowired
    private TransportOrderTaskService transportOrderTaskService;
    @Autowired
    private TransportOrderService transportOrderService;

    /**
     * 根据司机作业单获取对应的运单实体集合
     */
    @PostMapping("/getorderids")
    public R getOrderIds(@RequestBody DriverJobDTO driverJobDTO){
        //查询出司机作业单对应的运单id集合
        List<TransportOrderTask> transportOrderTaskList = transportOrderTaskService.findAll(null, driverJobDTO.getTaskTransportId());
        List<String> orderIds = transportOrderTaskList.stream().map(TransportOrderTask::getTransportOrderId).collect(Collectors.toList());
        //创建一个集合用于存放运单
        List<TransportOrder> transportOrders= new ArrayList<>();
        for (String orderId : orderIds) {
            //根据运单id查询出运单信息
            TransportOrder transportOrder = transportOrderService.getById(orderId);
            //将订单id放入集合中
            transportOrders.add(transportOrder);
        }
        return R.success(transportOrders);
    }

    /**
     * 新增司机作业单
     * @param driverJobDTO
     * @return
     */
    @PostMapping("")
    public R save(@RequestBody DriverJobDTO driverJobDTO){
        DriverJob driverJob = new DriverJob();
        BeanUtils.copyProperties(driverJobDTO, driverJob);
        driverJobService.saveDriverJob(driverJob);
        driverJobDTO = new DriverJobDTO();
        BeanUtils.copyProperties(driverJob, driverJobDTO);
        return R.success(driverJobDTO);
    }

    /**
     * 修改司机作业单信息
     * @param id
     * @param driverJobDTO
     * @return
     */
    @PutMapping("/{id}")
    public R updateById(@PathVariable(name = "id")String id, @RequestBody DriverJobDTO driverJobDTO){
        driverJobDTO.setId(id);
        if (driverJobDTO.getActualDepartureTime() == null&& driverJobDTO.getStatus() == 2){
            driverJobDTO.setActualDepartureTime(LocalDateTime.now());
        }
        if (driverJobDTO.getActualArrivalTime() != null&& driverJobDTO.getStatus() == 3){
            driverJobDTO.setActualArrivalTime(LocalDateTime.now());
        }
        DriverJob driverJob = new DriverJob();
        BeanUtils.copyProperties(driverJobDTO,driverJob);
        driverJobService.updateById(driverJob);
        return R.success(driverJobDTO);
    }

    /**
     * 获取司机作业单分页数据
     * @param driverJobDTO
     * @return
     */
    @PostMapping("/page")
    public R findByPage(@RequestBody DriverJobDTO driverJobDTO){
        Integer page = 1;
        Integer pageSize = 10;
        List<DriverJobDTO> dtoList = new ArrayList<>();
        Long total = 0L;
        Long pages = 0L;
        IPage<DriverJob> driverJobIPage = null;
        if (driverJobDTO != null) {
            if (driverJobDTO.getPage() != null) {
                page = driverJobDTO.getPage();
            }
            if (driverJobDTO.getPageSize() != null) {
                pageSize = driverJobDTO.getPageSize();
            }
            driverJobIPage = driverJobService.findByPage(page, pageSize, driverJobDTO.getId(), driverJobDTO.getDriverId(), driverJobDTO.getStatus(), driverJobDTO.getTaskTransportId());
        }
        return R.success(driverJobIPage);
    }

    /**
     * 根据id获取司机作业单信息
     *
     * @param id 司机作业单id
     * @return 司机作业单信息
     */
    @GetMapping("/{id}")
    public R findById(@PathVariable(name = "id") String id) {
        DriverJobDTO driverJobDTO = new DriverJobDTO();
        DriverJob driverJob = driverJobService.getById(id);
        if (driverJob != null) {
            BeanUtils.copyProperties(driverJob, driverJobDTO);
        } else {
            driverJobDTO = null;
        }
        return R.success(driverJobDTO);
    }

    /**
     * 获取所有司机作业单信息
     * @param dto
     * @return
     */
    @PostMapping("/findAll")
    public R findAll(@RequestBody DriverJobDTO dto) {
        List<DriverJobDTO> driverJobDTOS = new ArrayList<>();
        List<DriverJob> driverJobs = driverJobService.findAll(null, dto.getId(), dto.getDriverId(), dto.getStatus(), dto.getTaskTransportId());
        if (driverJobs != null) {
            for (DriverJob driverJob : driverJobs) {
                dto = new DriverJobDTO();
                BeanUtils.copyProperties(driverJob, dto);
                driverJobDTOS.add(dto);
            }
        }
        return R.success(driverJobDTOS);
    }

}
