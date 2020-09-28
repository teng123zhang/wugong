package com.rc.test;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tk.mybatis.spring.annotation.MapperScan;
@MapperScan(basePackages = "com.rc.test.mapper")
@SpringBootApplication
public class Test2020Application {

	public static void main(String[] args) {
		SpringApplication.run(Test2020Application.class, args);
	}

}
