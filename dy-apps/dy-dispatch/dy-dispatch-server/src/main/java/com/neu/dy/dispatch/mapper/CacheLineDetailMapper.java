package com.neu.dy.dispatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.dy.entity.CacheLineDetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface CacheLineDetailMapper extends BaseMapper<CacheLineDetailEntity> {
}
