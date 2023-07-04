package com.neu.dy.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.dy.work.entity.TaskTransport;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 运输任务表 Mapper 接口
 * </p>
 *
 * @author jpf
 * @since 2020-01-08
 */
@Repository
public interface TaskTransportMapper extends BaseMapper<TaskTransport> {
}
