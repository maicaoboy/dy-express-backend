package com.neu.dy.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.dy.work.entity.TransportOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 运单表 Mapper 接口
 * </p>
 *
 * @author jpf
 * @since 2020-01-06
 */
@Repository
public interface TransportOrderMapper extends BaseMapper<TransportOrder> {
}
