package com.neu.dy.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.dy.order.entitiy.Order;
import org.springframework.stereotype.Repository;

/**
 * 订单 Mapper 接口
 */
@Repository
public interface OrderMapper extends BaseMapper<Order> {
}
