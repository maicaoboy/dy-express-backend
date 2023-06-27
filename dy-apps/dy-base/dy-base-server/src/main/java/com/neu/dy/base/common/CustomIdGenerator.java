package com.neu.dy.base.common;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Classname CustomIdGenerator
 * @Description 自定义ID生成器模式
 * @Version 1.0.0
 * @Date 2023/6/20 21:37
 * @Created by maicaoboy
 */
@Component
public class CustomIdGenerator implements IdentifierGenerator {
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1, 1);
    }

    @Autowired
    private IdWorker idWorker;

    @Override
    public Long nextId(Object entity) {
        return idWorker.nextId();
    }
}
