package com.neu.dy;

import com.neu.dy.auth.client.EnableAuthClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"com.neu.dy"})
@EnableAuthClient//开启授权客户端，开启后就可以使用dy-tools-jwt提供的工具类进行jwt token解析了
public class DyGatewayproApplication {

    public static void main(String[] args) {
        SpringApplication.run(DyGatewayproApplication.class, args);
    }

}
