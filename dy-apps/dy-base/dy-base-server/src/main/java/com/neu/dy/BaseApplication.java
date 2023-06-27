package com.neu.dy;

import com.neu.dy.validator.config.EnableFormValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Classname BaseApplication
 * @Description 基础数据服务
 * @Version 1.0.0
 * @Date 2023/6/26 23:14
 * @Created by maicaoboy
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(value = {
        "com.neu.dy",
})
@Slf4j
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableFormValidator
public class BaseApplication {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application =
        SpringApplication.run(BaseApplication.class, args);
        Environment env = application.getEnvironment();
        log.info("应用 '{}' 运行成功!  Swagger文档: http://{}:{}/doc.html",
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
    }
}
