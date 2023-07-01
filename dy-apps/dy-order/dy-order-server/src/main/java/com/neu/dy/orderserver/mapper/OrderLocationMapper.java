package com.neu.dy.orderserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.dy.order.entitiy.OrderLocation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 位置信息
 */
@Mapper
public interface OrderLocationMapper extends BaseMapper<OrderLocation> {
}
