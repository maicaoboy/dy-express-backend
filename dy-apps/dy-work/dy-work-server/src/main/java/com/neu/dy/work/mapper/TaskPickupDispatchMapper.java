package com.neu.dy.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.dy.work.entity.TaskPickupDispatch;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 取件、派件任务信息表 Mapper 接口
 * </p>
 *
 * @author jpf
 * @since 2019-12-30
 */
@Repository
public interface TaskPickupDispatchMapper extends BaseMapper<TaskPickupDispatch> {
}
