package com.neu.dy.dispatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neu.dy.dispatch.mapper.ScheduleJobLogMapper;
import com.neu.dy.dispatch.service.ScheduleJobLogService;
import com.neu.dy.dispatch.utils.ConvertUtils;
import com.neu.dy.dto.ScheduleJobLogDTO;
import com.neu.dy.entity.ScheduleJobLogEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogMapper, ScheduleJobLogEntity> implements ScheduleJobLogService {

    @Override
    public IPage<ScheduleJobLogEntity> page(Map<String, Object> params) {
        IPage<ScheduleJobLogEntity> page = new Page<>();
        page.setCurrent(Long.parseLong(params.get("page").toString()));
        page.setSize(Long.parseLong(params.get("pageSize").toString()));

        LambdaQueryWrapper<ScheduleJobLogEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(params.containsKey("jobId"),ScheduleJobLogEntity::getJobId,params.get("jobId"));
        queryWrapper.orderByDesc(ScheduleJobLogEntity::getCreateDate);

        return baseMapper.selectPage(page,queryWrapper);
    }

    @Override
    public ScheduleJobLogDTO get(Long id) {
        ScheduleJobLogEntity logEntity = baseMapper.selectById(id);
        return ConvertUtils.sourceToTarget(logEntity,ScheduleJobLogDTO.class);
    }
}
