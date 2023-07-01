package com.neu.dy.orderserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.dy.order.entitiy.OrderCargo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 货物Mapper 接口
 */
@Mapper
public interface OrderCargoMapper extends BaseMapper<OrderCargo> {

  int deleteByPrimaryKey(String id);

  int insertSelective(OrderCargo record);

  OrderCargo selectByPrimaryKey(String id);

  int updateByPrimaryKeySelective(OrderCargo record);

  int updateByPrimaryKeyWithBLOBs(OrderCargo record);

  int updateByPrimaryKey(OrderCargo record);
}
