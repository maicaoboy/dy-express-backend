package com.neu.dy.base.biz.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.dy.base.entity.user.DyCourierScope;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 快递员业务范围表  Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Repository
public interface DyCourierScopMapper extends BaseMapper<DyCourierScope> {

}
