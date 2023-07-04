package com.neu.dy.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.order.entitiy.OrderCargo;

import java.util.List;

/**
 * 货物
 */
public interface OrderCargoService extends IService<OrderCargo> {


    /**
     * 保存货物信息
     *
     * @param record 货物信息
     * @return 货物信息
     */
    OrderCargo saveSelective(OrderCargo record);

    /**
     * 获取货物列表
     *
     * @param tranOrderId 运单id
     * @param orderId     订单id
     * @return 货物列表
     */
    List<OrderCargo> findAll(String tranOrderId, String orderId);
}
