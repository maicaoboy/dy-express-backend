package com.neu.dy.orderserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.dy.order.entitiy.OrderCargo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 货物Mapper 接口
 */
@Repository
public interface OrderCargoMapper extends BaseMapper<OrderCargo> {

  int deleteByPrimaryKey(String id);

  int insertSelective(OrderCargo record);

//  OrderCargo selectByPrimaryKey(String id);

  int updateByPrimaryKeySelective(OrderCargo record);

  int updateByPrimaryKeyWithBLOBs(OrderCargo record);

  int updateByPrimaryKey(OrderCargo record);
}
