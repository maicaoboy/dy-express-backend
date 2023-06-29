package com.neu.dy;

import com.neu.dy.auth.client.EnableAuthClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@EnableAsync
@Slf4j
@EnableAuthClient//开启授权客户端，开启后就可以使用dy-tools-jwt提供的工具类进行jwt token解析了
public class DyGatewayproApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(DyGatewayproApplication.class, args);
        Environment env = application.getEnvironment();
        log.info("应用 '{}' 运行成功!  Swagger文档: http://{}:{}/doc.html",
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
    }

}
