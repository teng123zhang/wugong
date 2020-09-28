package com.rc.gmall2020.user;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import tk.mybatis.spring.annotation.MapperScan;
@MapperScan(basePackages ="com.rc.gmall2020.user.mapper")
@SpringBootApplication
@ComponentScan(basePackages = "com.rc.gmall2020.util.config")
public class GmallUserManageApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallUserManageApplication.class, args);
	}

}
