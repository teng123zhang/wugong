package com.guli.order.controller;


import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.guli.common.result.Result;
import com.guli.order.entity.EduOrder;
import com.guli.order.service.EduOrderService;
import com.guli.order.utils.JwtUtils;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-28
 */
@RestController
@RequestMapping("/order/edu-order")
public class EduOrderController {
	
	
	

	    @Autowired
	    private EduOrderService orderService;

	    //1 生成订单的方法
	    @PostMapping("createOrder/{courseId}")
	    public Result saveOrder(@PathVariable String courseId, HttpServletRequest request) {
	        //创建订单，返回订单号
	        String orderNo =
	                orderService.createOrders(courseId, JwtUtils.getMemberIdByJwtToken(request));
	        
	        return Result.ok().data("orderId", orderNo);
	    }

	    //2 根据订单id查询订单信息
	    @GetMapping("getOrderInfo/{orderId}")
	    public Result getOrderInfo(@PathVariable String orderId) {
	        QueryWrapper<EduOrder> wrapper = new QueryWrapper<>();
	        wrapper.eq("order_no", orderId);
	        EduOrder order = orderService.getOne(wrapper);
	        return Result.ok().data("item", order);
	    }

	    //根据课程id和用户id查询订单表中订单状态
	    @GetMapping("isBuyCourse/{courseId}/{memberId}")
	    public boolean isBuyCourse(@PathVariable String courseId, @PathVariable String memberId) {
	        QueryWrapper<EduOrder> wrapper = new QueryWrapper<>();
	        wrapper.eq("course_id", courseId);
	        wrapper.eq("member_id", memberId);
	        wrapper.eq("status", 1);//支付状态 1代表已经支付
	        int count = orderService.count(wrapper);
	        System.out.println("count: " + count);
	        if (count > 0) { //已经支付
	            return true;
	        } else {
	            return false;
	        }
	    }
	}



