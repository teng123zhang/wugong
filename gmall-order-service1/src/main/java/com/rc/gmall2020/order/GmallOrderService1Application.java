package com.rc.gmall2020.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.rc.gmall2020")
@MapperScan(basePackages = "com.rc.gmall2020.order.mapper")
@EnableTransactionManagement //保存两个以上切记开启事务 一个失败立马回滚 切记切记!!!!!!!
public class GmallOrderService1Application {

	public static void main(String[] args) {
		SpringApplication.run(GmallOrderService1Application.class, args);
	}

}
