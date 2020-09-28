package com.rc.gmall2020.list;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.rc.gmall2020.util.config")
public class GmallListService1Application {

	public static void main(String[] args) {
		SpringApplication.run(GmallListService1Application.class, args);
	}

}
