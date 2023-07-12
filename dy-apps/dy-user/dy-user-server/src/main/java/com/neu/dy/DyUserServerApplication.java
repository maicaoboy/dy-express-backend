package com.neu.dy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.InetAddress;
import java.net.UnknownHostException;


@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
@EnableFeignClients(value = {
		"com.neu.dy",
})
@EnableAsync
public class DyUserServerApplication {

	public static void main(String[] args) throws UnknownHostException {

		ConfigurableApplicationContext application = SpringApplication.run(DyUserServerApplication.class, args);
		Environment env = application.getEnvironment();
		log.info("应用 '{}' 运行成功!  Swagger文档: http://{}:{}/doc.html",
				env.getProperty("spring.application.name"),
				InetAddress.getLocalHost().getHostAddress(),
				env.getProperty("server.port"));
	}

}
