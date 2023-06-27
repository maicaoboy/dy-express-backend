package com.neu.dy.base.config;

import com.neu.dy.database.datasource.BaseMybatisConfiguration;
import com.neu.dy.database.properties.DatabaseProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname BaseServiceServerMybatisAutoConfiguration
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/6/26 21:55
 * @Created by maicaoboy
 */

@Configuration
@Slf4j

public class BaseServiceServerMybatisAutoConfiguration extends BaseMybatisConfiguration {
    public BaseServiceServerMybatisAutoConfiguration(DatabaseProperties databaseProperties) {
        super(databaseProperties);
    }
}
