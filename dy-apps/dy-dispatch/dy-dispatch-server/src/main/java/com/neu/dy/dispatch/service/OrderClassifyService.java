package com.neu.dy.dispatch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.entity.OrderClassifyEntity;

import java.util.List;

/**
 * 订单分类
 */
public interface OrderClassifyService extends IService<OrderClassifyEntity> {
    List<OrderClassifyEntity> findByJobLogId(String id);
}