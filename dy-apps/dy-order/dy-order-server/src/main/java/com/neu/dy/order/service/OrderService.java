package com.neu.dy.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.base.R;
import com.neu.dy.base.dto.angency.AgencyScopeDto;
import com.neu.dy.order.dto.OrderDTO;
import com.neu.dy.order.dto.OrderSearchDTO;
import com.neu.dy.order.entitiy.Order;
import com.neu.dy.utils.EntCoordSyncJob;
import lombok.SneakyThrows;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * 订单
 */
public interface OrderService extends IService<Order> {
    /**
     * 新增订单
     *
     * @param order 订单信息
     * @return 订单信息
     */
    Order saveOrder(Order order);

    /**
     * 获取订单分页数据
     *
     * @param page     页码
     * @param pageSize 页尺寸
     * @param order    查询条件
     * @return 订单分页数据
     */
    IPage<Order> findByPage(Integer page, Integer pageSize, Order order);

    /**
     * 获取订单列表
     *
     * @param ids 订单id列表
     * @return 订单列表
     */
    List<Order> findAll(List<String> ids);

    /**
     * 获取订单分页数据 客户端使用
     *
     * @return 订单分页数据
     */
    IPage<Order> pageLikeForCustomer(OrderSearchDTO orderSearchDTO);

    /**
     * 计算订单价格
     * @param orderDTO
     * @return
     */
    public Map calculateAmount(OrderDTO orderDTO);

    Integer calculatetime(OrderDTO orderDTO);

    String caculateAgencyId(Order order);
}
