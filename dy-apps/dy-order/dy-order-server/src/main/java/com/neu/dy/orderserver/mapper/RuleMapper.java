package com.neu.dy.orderserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.dy.order.entitiy.Rule;
import org.apache.ibatis.annotations.Mapper;

/**
 * 规则
 */
@Mapper
public interface RuleMapper extends BaseMapper<Rule> {
}