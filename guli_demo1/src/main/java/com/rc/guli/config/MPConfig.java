package com.rc.guli.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;

@Configuration
@EnableTransactionManagement
@MapperScan("com.rc.guli.mapper")
public class MPConfig {
	/**
	 * 乐观锁插件
	 */
	
	@Bean
	
	public OptimisticLockerInterceptor optimisticLockerInterceptor() {
		
		return new OptimisticLockerInterceptor();
	}
	
	/**
	 * 分页插件
	 */
	
	@Bean
	
	public PaginationInterceptor paginationInterceptor() {
		
		return new PaginationInterceptor();
	}
	/**
	 * 逻辑删除
	 */
	
	@Bean
	
	public ISqlInjector sqlInjector() {
		return new  DefaultSqlInjector();
			
			
	}
	
	@Bean
	@Profile({"dev","test"}) //设置 dev test 环境开启
	
	public  PerformanceInterceptor performanceInterceptor() {
		PerformanceInterceptor  performanceInterceptor = new PerformanceInterceptor ();
		performanceInterceptor.setMaxTime(5);//ms 超过此处设置的ms则sql不执行
		performanceInterceptor.setFormat(true);
		
		return performanceInterceptor;
		
		
		
		
	}
	
	
	
	
	
	
	

}
