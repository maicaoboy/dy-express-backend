package com.neu.dy.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neu.dy.order.entitiy.OrderLocation;
import com.neu.dy.order.mapper.OrderLocationMapper;
import com.neu.dy.order.service.OrderLocationService;
import org.springframework.stereotype.Service;

/**
 * 位置信息服务实现
 */
@Service
public class OrderLocationServiceImpl extends ServiceImpl<OrderLocationMapper, OrderLocation> implements OrderLocationService {

}