package com.neu.dy.dispatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.dy.entity.ScheduleJobEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * 定时任务  Mapper 接口
 */
@Component
@Repository
public interface ScheduleJobMapper extends BaseMapper<ScheduleJobEntity> {

}
