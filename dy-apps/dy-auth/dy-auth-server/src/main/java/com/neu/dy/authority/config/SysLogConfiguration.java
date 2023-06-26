package com.neu.dy.authority.config;

import com.neu.dy.authority.biz.service.common.OptLogService;
import com.neu.dy.log.entity.OptLogDTO;
import com.neu.dy.log.event.SysLogListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.function.Consumer;

/**
 * @Classname SysLogConfiguration
 * @Description 日志配置
 * @Version 1.0.0
 * @Date 2023/6/25 14:29
 * @Created by maicaoboy
 */
@EnableAsync
@Configuration
public class SysLogConfiguration {
    //日志记录监听器
    @Bean
    public SysLogListener sysLogListener(OptLogService optLogService) {
        Consumer<OptLogDTO> consumer = optLogService::save;
        return new SysLogListener(consumer);
    }
}
