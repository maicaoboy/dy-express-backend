package com.neu.dy.dispatch.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.entity.OrderClassifyOrderEntity;

/**
 * 订单分类关联订单
 */
public interface OrderClassifyOrderService extends IService<OrderClassifyOrderEntity> {
    String getClassifyId(String orderId);
}