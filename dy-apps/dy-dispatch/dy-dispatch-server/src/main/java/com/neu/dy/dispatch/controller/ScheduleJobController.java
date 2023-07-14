package com.neu.dy.dispatch.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ImmutableList;
import com.neu.dy.api.OrgApi;
import com.neu.dy.authority.entity.core.Org;
import com.neu.dy.base.R;
import com.neu.dy.base.common.Result;
import com.neu.dy.dispatch.service.ScheduleJobService;
import com.neu.dy.dispatch.utils.IdUtils;
import com.neu.dy.dto.OrgJobTreeDTO;
import com.neu.dy.dto.ScheduleJobDTO;
import com.neu.dy.entity.ScheduleJobEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 定时任务
 *
 * @author
 */
@RestController
@RequestMapping("/schedule")
@Api(tags = "定时任务")
public class ScheduleJobController {
//    private static final List<Integer> ORG_TYPE = ImmutableList.of(OrgType.BUSINESS_HALL.getType(), OrgType.TOP_TRANSFER_CENTER.getType(), OrgType.TOP_TRANSFER_CENTER.getType()).asList();

    @Autowired
    private ScheduleJobService scheduleJobService;

    @Autowired
    private OrgApi orgApi;


    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pageSize", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "name", paramType = "query", dataType = "String")
    })
    public R page(@ApiIgnore @RequestParam Map<String, Object> params) {
        Integer page = Integer.valueOf((String) params.get("page"));
        Integer pageSize = Integer.valueOf((String) params.get("pageSize")) ;
        Page<ScheduleJobEntity> pageInfo = new Page<>(page,pageSize);
        String businessId = (String) params.get("businessId");
        LambdaQueryWrapper<ScheduleJobEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScheduleJobEntity::getBusinessId,businessId);
        Page<ScheduleJobEntity> page1 = scheduleJobService.page(pageInfo, queryWrapper);
        return R.success(page1);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public ScheduleJobDTO info(@PathVariable("id") String id) {
        ScheduleJobDTO schedule = scheduleJobService.get(id);
        return schedule;
    }

    @GetMapping("dispatch/{id}")
    @ApiOperation("调度信息")
    public R dispatchInfo(@PathVariable("id") String id) {

        LambdaQueryWrapper<ScheduleJobEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScheduleJobEntity::getBusinessId, id);
        ScheduleJobEntity scheduleJobEntity = scheduleJobService.getOne(wrapper);
        if (scheduleJobEntity == null) {
            return R.fail(404,"机构没有任务信息");
        }

        ScheduleJobDTO schedule = new ScheduleJobDTO();
        BeanUtils.copyProperties(scheduleJobEntity, schedule);
        return R.success(schedule);
    }

    @PostMapping
    @ApiOperation("保存")
    public R save(@RequestBody ScheduleJobDTO dto) {
        scheduleJobService.save(dto);
        return R.success();
    }

//    @PostMapping("dispatch")
//    @ApiOperation("保存或修改")
//    public R dispatch(@RequestBody ScheduleJobDTO dto) {
//
//        String businessId = dto.getBusinessId();
//
//        R<Org> orgR = orgApi.get(Long.valueOf(businessId));
//
//        Integer orgType = orgR.getData().getOrgType();
//        if (!ORG_TYPE.contains(orgType)) {
//            return R.fail(400, "无法给转运中心以上的机构增加调度任务");
//        }
//
//        if (StringUtils.isNotBlank(dto.getId())) {
//            dto.setUpdateDate(new Date());
//            scheduleJobService.update(dto);
//            return R.success();
//        } else {
//            dto.setId(IdUtils.get());
//            dto.setBeanName("dispatchTask");
//            dto.setCreateDate(new Date());
//            scheduleJobService.save(dto);
//
//            return R.success()
//        }
//    }

    @PutMapping
    @ApiOperation("修改")
    public R update(@RequestBody ScheduleJobDTO dto) {

        System.out.println(dto);
        scheduleJobService.update(dto);

        return R.success();
    }

    @DeleteMapping
    @ApiOperation("删除")
    public R delete(@RequestBody String[] ids) {
        scheduleJobService.deleteBatch(ids);

        return R.success();
    }

    @PutMapping("/run/{id}")
    @ApiOperation("立即执行")
    public R run(@PathVariable String id) {
        scheduleJobService.run(new String[]{id});

        return R.success();
    }

    @PutMapping("/run")
    @ApiOperation("立即执行")
    public R run(@RequestBody String[] ids) {
        scheduleJobService.run(ids);

        return R.success();
    }

    @PutMapping("/pause/{id}")
    @ApiOperation("暂停")
    public R pause(@PathVariable String id) {
        scheduleJobService.pause(new String[]{id});

        return R.success();
    }

    @PutMapping("/pause")
    @ApiOperation("暂停")
    public R pause(@RequestBody String[] ids) {
        scheduleJobService.pause(ids);

        return R.success();
    }

    @PutMapping("/resume/{id}")
    @ApiOperation("恢复")
    public R resume(@PathVariable String id) {
        scheduleJobService.resume(new String[]{id});

        return R.success();
    }

    @PutMapping("/resume")
    @ApiOperation("恢复")
    public R resume(@RequestBody String[] ids) {
        scheduleJobService.resume(ids);

        return R.success();
    }

}
