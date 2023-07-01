package com.neu.dy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DyOrderServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DyOrderServerApplication.class, args);
    }

}
