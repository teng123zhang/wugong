package com.rc.gmall2020.order.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rc.gmall2020.bean.OrderInfo;
import com.rc.gmall2020.service.OrderService;
@EnableScheduling
@Component
public class OrderTask {
	
	@Autowired
	private OrderService  orderService;
	
	//cron 表示表示任务启动规则
	//每分钟的第五秒执行该方法
	@Scheduled(cron="5 * * * * ?")
	public void test01() {
		
		System.out.println(Thread.currentThread().getName()+"-------------------------------0001----------------------------");
	}
	
	//每隔五秒执行一次
	@Scheduled(cron="0/5 * * * * ?")
	
public void test02() {
		
		System.out.println(Thread.currentThread().getName()+"-------------------------------0002----------------------------");
	}
	
	
	//每隔20执行一次
	@Scheduled(cron="0/20 * * * * ?")
	
public void checkOrder() {
		/**
		 * 1查询有多少订单过期
		 * 什么样的订单是过期: 当前系统时间过期时间and当前状态是未支付
		 * 2循环过期订单列表，进行处理
		 * orderInfo
		 * paymentInfo
		 * 
		 */
		
		List<OrderInfo> orderInfoList=orderService.getExpireOrderList();
		
		for (OrderInfo orderInfo : orderInfoList) {
			//关闭过期订单
			orderService.exexExpiredOrder(orderInfo);
			
		}
	}
	

}
