package com.rc.gmall2020.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import tk.mybatis.spring.annotation.MapperScan;
@EnableTransactionManagement
@MapperScan(basePackages ="com.rc.gmall2020.manage.mapper")
@SpringBootApplication
@ComponentScan(basePackages ="com.rc.gmall2020.util.config" )
public class GmallManageServiceApplication {

	public static void main(String[] args) {
		
			SpringApplication.run(GmallManageServiceApplication.class, args);
		
		
	}

}
