package com.neu.dy.work.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neu.dy.base.R;
import com.neu.dy.work.dto.DriverJobDTO;
import com.neu.dy.work.entity.DriverJob;
import com.neu.dy.work.service.DriverJobService;
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
