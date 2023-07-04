package com.neu.dy.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.dy.work.entity.TransportOrderTask;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 运单与运输任务表 Mapper 接口
 * </p>
 */
@Repository
public interface TransportOrderTaskMapper extends BaseMapper<TransportOrderTask> {
}
