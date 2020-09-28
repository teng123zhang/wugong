package com.guli.order.service.impl;


import javax.management.loading.PrivateClassLoader;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.order.client.CourseClient;
import com.guli.order.client.UcenterClient;
import com.guli.order.entity.CourseWebVoOrder;
import com.guli.order.entity.EduOrder;
import com.guli.order.entity.UcenterMapperOrder;

import com.guli.order.mapper.EduOrderMapper;
import com.guli.order.service.EduOrderService;
import com.guli.order.utils.OrderNoUtil;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-28
 */
@Service
public class EduOrderServiceImpl extends ServiceImpl<EduOrderMapper, EduOrder> implements EduOrderService {
	
	
	@Autowired
	private CourseClient courseClient;
	
	@Autowired
	private UcenterClient ucenterClient;
	public String createOrders(String courseId, String memberId) {
		
   System.out.println(memberId+"-->"+courseId);
	    
	        //通过远程调用根据用户id获取用户信息
		UcenterMapperOrder userInfoOrder = ucenterClient.getUserInfoOrder(memberId);
	        
	    

		        //通过远程调用根据课程id获取课信息
		CourseWebVoOrder courseInfoOrder =courseClient.getCourseInfoOrder(courseId);
		
	        //创建Order对象，向order对象里面设置需要数据
	        EduOrder order = new EduOrder();
	        order.setOrderNo(OrderNoUtil.getOrderNo());//订单号
	        
	        order.setCourseId(courseId); //课程id
	        order.setCourseTitle(courseInfoOrder.getTitle());
	        order.setCourseCover(courseInfoOrder.getCover());
	        order.setTeacherAme(courseInfoOrder.getTeacherName());
	       
	        order.setTotalFee(courseInfoOrder.getPrice());
	      
	        order.setMemberId(memberId);
	        order.setMobile(userInfoOrder.getMobile());
	        order.setNickname(userInfoOrder.getNickname());
	        order.setStatus(0);  //订单状态（0：未支付 1：已支付）
	        order.setPayType(1);  //支付类型 ，微信1
	        baseMapper.insert(order);
	       
//返回订单号
	        
	        return order.getOrderNo();
	}        
	
	       
}
	
	
	
	        
	


