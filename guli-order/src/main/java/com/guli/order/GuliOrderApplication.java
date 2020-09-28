package com.guli.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;



@EnableFeignClients
@ComponentScan(basePackages = {"com.guli.order"})
@MapperScan("com.guli.order.mapper")
@EnableDiscoveryClient
@SpringBootApplication
public class GuliOrderApplication {

	public static void main(String[] args) {
		try {
			
			SpringApplication.run(GuliOrderApplication.class, args);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
