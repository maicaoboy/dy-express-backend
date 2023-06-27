package com.neu.dy.base.biz.dao.truck;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.dy.base.entity.truck.DyTruckType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 车辆类型表  Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Repository
public interface DyTruckTypeMapper extends BaseMapper<DyTruckType> {

}
