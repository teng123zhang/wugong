package com.guli.order.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;



@EnableTransactionManagement
@Configuration()
@MapperScan("com.guli.order.mapper")
public class OrderConfig {
	
	@Bean
	@Profile({"dev","test"}) //设置 dev test 环境开启
	
	public  PerformanceInterceptor performanceInterceptor() {
		PerformanceInterceptor  performanceInterceptor = new PerformanceInterceptor ();
		performanceInterceptor.setMaxTime(1000);//ms 超过此处设置的ms则sql不执行
		performanceInterceptor.setFormat(true);
		
	return performanceInterceptor;
		
		
		
		
}
	
	/**
	 * 逻辑删除:第一步，在数据库里加上isdeleted字段并设置状态码为0，1;
	 *       第二步：在实体类加上  @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
                            @TableLogic
                            @TableField(fill = FieldFill.INSERT)
                                第三步:在handler里加上this.setFieldValByName("isDeleted", 0, metaObject);
                              第四部 :在配置文件加上：mybatis-plus.global-config.db-config.logic-delete-value=1
                              mybatis-plus.global-config.db-config.logic-not-delete-value=0                
                                
              第五步在config里加上一个   ：
	        public ISqlInjector sqlInjector() {
		                  return new  DefaultSqlInjector();  
		                  
		                               }
                                
	 */
	
	@Bean
	
	public ISqlInjector sqlInjector() {
		return new  DefaultSqlInjector();
			
			
	}
	
	
	/**
	 * 分页插件
	 */
	
	@Bean
	
	public PaginationInterceptor paginationInterceptor() {
		
		return new PaginationInterceptor();
	}

}
