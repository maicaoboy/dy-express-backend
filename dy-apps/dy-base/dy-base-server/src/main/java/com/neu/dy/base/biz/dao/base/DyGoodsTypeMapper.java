package com.neu.dy.base.biz.dao.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neu.dy.base.entity.base.DyGoodsType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository

public interface DyGoodsTypeMapper extends BaseMapper<DyGoodsType> {
    List<DyGoodsType> findByPage(Page<DyGoodsType> iPage, String name, String truckTypeId, String truckTypeName);
}
