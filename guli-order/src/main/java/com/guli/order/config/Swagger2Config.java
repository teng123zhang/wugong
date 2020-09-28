package com.guli.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2

public class Swagger2Config {
	@Bean
	
	public  Docket webApiConfig() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("webApi")
				.apiInfo(webApiInfo())
				.select()
				.build();
		
		
	}

	private ApiInfo webApiInfo() {
		
		return new ApiInfoBuilder()
				.title("微信支付文档")
				.description("本文档描述了微信支付文档接口")
				.version("1.0")
				.contact(new Contact("华安", "http://baidu.com", "55551733@qq.com"))
		        .build();
	}
	

}
