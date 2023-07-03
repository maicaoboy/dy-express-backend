package com.neu.dy.orderserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.dy.order.entitiy.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * 订单 Mapper 接口
 */
@Repository
public interface OrderMapper extends BaseMapper<Order> {
}
