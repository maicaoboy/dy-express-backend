package com.neu.dy.authority.config;

import com.neu.dy.database.datasource.BaseMybatisConfiguration;
import com.neu.dy.database.properties.DatabaseProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname AuthorityMybatisAutoConfiguration
 * @Description Mybatis配置
 * @Version 1.0.0
 * @Date 2023/6/23 15:27
 * @Created by maicaoboy
 */
@Configuration
@Slf4j
public class AuthorityMybatisAutoConfiguration extends BaseMybatisConfiguration {
    public AuthorityMybatisAutoConfiguration(DatabaseProperties databaseProperties) {
        super(databaseProperties);
    }
}
