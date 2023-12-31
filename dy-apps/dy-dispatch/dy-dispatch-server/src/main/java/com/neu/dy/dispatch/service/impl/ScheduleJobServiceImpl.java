/**
 * Copyright (c) 2019 联智合创 All rights reserved.
 * <p>
 * http://www.witlinked.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.neu.dy.dispatch.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neu.dy.api.OrgApi;
import com.neu.dy.authority.dto.core.OrgTreeDTO;
import com.neu.dy.base.R;
import com.neu.dy.dispatch.mapper.ScheduleJobMapper;
import com.neu.dy.dispatch.service.ScheduleJobService;
import com.neu.dy.dispatch.utils.ConvertUtils;
import com.neu.dy.dispatch.utils.ScheduleUtils;
import com.neu.dy.dto.OrgJobTreeDTO;
import com.neu.dy.dto.ScheduleJobDTO;
import com.neu.dy.entity.ScheduleJobEntity;
import com.neu.dy.enums.ScheduleStatus;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Scheduler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobMapper, ScheduleJobEntity> implements ScheduleJobService {
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private OrgApi orgApi;

    @Override
    public List<OrgJobTreeDTO> page(Map<String, Object> params) {
        R<List<OrgTreeDTO>> orgTree = orgApi.tree("", true);
        List<OrgTreeDTO> orgTreeDTOS = orgTree.getData();
        List<ScheduleJobEntity> schedulerEntity = baseMapper.selectList(null);
        Map<String, ScheduleJobEntity> schedulerMap = schedulerEntity.stream().collect(Collectors.toMap(ScheduleJobEntity::getBusinessId, item -> item));

        List<OrgJobTreeDTO> orgJobTreeDTOS = new ArrayList<>();
        mergeOrgJob(orgJobTreeDTOS, orgTreeDTOS, schedulerMap);

        return orgJobTreeDTOS;
    }

    private void mergeOrgJob(List<OrgJobTreeDTO> orgJobTreeDTOS, List<OrgTreeDTO> orgTreeDTOS, Map<String, ScheduleJobEntity> schedulerMap) {
        orgTreeDTOS.forEach(item -> {
            OrgJobTreeDTO orgJobTreeDTO = new OrgJobTreeDTO();
            BeanUtils.copyProperties(item, orgJobTreeDTO);
            Long orgId = orgJobTreeDTO.getId();
            ScheduleJobEntity schedulerDto = schedulerMap.get(orgId.toString());
            if (schedulerDto != null) {
                orgJobTreeDTO.setCronExpression(schedulerDto.getCronExpression());
                orgJobTreeDTO.setJobId(schedulerDto.getId());
                orgJobTreeDTO.setParams(schedulerDto.getParams());
                orgJobTreeDTO.setRemark(schedulerDto.getRemark());
                orgJobTreeDTO.setJobStatus(schedulerDto.getStatus());
            }
            orgJobTreeDTOS.add(orgJobTreeDTO);
        });
    }


    @Override
    public ScheduleJobDTO get(String id) {
        ScheduleJobEntity entity = baseMapper.selectById(id);
        return ConvertUtils.sourceToTarget(entity, ScheduleJobDTO.class);
    }

    @Override
    public ScheduleJobDTO getByOrgId(String id) {
        LambdaQueryWrapper<ScheduleJobEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScheduleJobEntity::getBusinessId, id);
        ScheduleJobEntity entity = baseMapper.selectOne(wrapper);
        return ConvertUtils.sourceToTarget(entity, ScheduleJobDTO.class);
    }


    private QueryWrapper<ScheduleJobEntity> getWrapper(Map<String, Object> params) {
        String beanName = (String) params.get("beanName");

        QueryWrapper<ScheduleJobEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(beanName), "bean_name", beanName);

        return wrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScheduleJobDTO dto) {
        ScheduleJobEntity entity = ConvertUtils.sourceToTarget(dto, ScheduleJobEntity.class);

        entity.setStatus(ScheduleStatus.NORMAL.getValue());
        entity.setCreateDate(DateTime.now());
        baseMapper.insert(entity);

        ScheduleUtils.createScheduleJob(scheduler, entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScheduleJobDTO dto) {
        ScheduleJobEntity entity = this.getById(dto.getId());

        BeanUtils.copyProperties(dto, entity, ConvertUtils.getNullPropertyNames(dto));


        System.out.println(entity);
        ScheduleUtils.updateScheduleJob(scheduler, entity);

        baseMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(String[] ids) {
        for (String id : ids) {
            ScheduleUtils.deleteScheduleJob(scheduler, id);
        }

        //删除数据
        baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public int updateBatch(String[] ids, int status) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("ids", ids);
        map.put("status", status);

        ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
        scheduleJobEntity.setStatus(status);
        LambdaQueryWrapper<ScheduleJobEntity> wrapper = new LambdaQueryWrapper();
        wrapper.in(ScheduleJobEntity::getId, ids);
        return baseMapper.update(scheduleJobEntity, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(String[] ids) {
        for (String id : ids) {
            ScheduleUtils.run(scheduler, baseMapper.selectById(id));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pause(String[] ids) {
        for (String id : ids) {
            ScheduleUtils.pauseJob(scheduler, id);
        }

        updateBatch(ids, ScheduleStatus.PAUSE.getValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resume(String[] ids) {
        for (String id : ids) {
            ScheduleUtils.resumeJob(scheduler, id);
        }

        updateBatch(ids, ScheduleStatus.NORMAL.getValue());
    }

}