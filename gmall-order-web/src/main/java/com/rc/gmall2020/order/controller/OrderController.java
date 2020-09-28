package com.rc.gmall2020.order.controller;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.rc.gmall2020.bean.CartInfo;
import com.rc.gmall2020.bean.OrderDetail;
import com.rc.gmall2020.bean.OrderInfo;
import com.rc.gmall2020.bean.SkuInfo;
import com.rc.gmall2020.bean.UserAddress;
import com.rc.gmall2020.config.LoginRequire;
import com.rc.gmall2020.service.CartService;
import com.rc.gmall2020.service.ManageService;
import com.rc.gmall2020.service.OrderService;
import com.rc.gmall2020.service.UserService;

@Controller
public class OrderController {
//	
//	@RequestMapping("trade")
//	
//	public String trade() {
//		
//		return "index";
//		
//		
//		
//	}
	
	
	//@Autowired
	@Reference
	
	private UserService userService;
	
	@Reference
	
	private CartService cartService;
	
	
	@Reference
	
	private OrderService orderService;
	
	
	@Reference
	
	private  ManageService manageService;
	
	
	@RequestMapping("trade")
	
	@LoginRequire
	//@ResponseBody // 第一个返回json字符串 ，fastJson。jar
	public String trade(HttpServletRequest req) {
		String userId = (String)req.getAttribute("userId");
		
		List<UserAddress> userAddressList = userService.getUserAddressList(userId);
		req.setAttribute("userAddressList", userAddressList);
		
		//展示送货清单
		//数据来源 勾选的购物车
		/**
		 * 根据用户Id查询购物车列表
		 */
		List<CartInfo> cartInfoList = cartService.getCartCheckedList(userId);
		//声明一个集合存储订单明细
		List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
			
			
			
			
			
		
		//将集合赋值h给OrderDetail
		for (CartInfo cartInfo : cartInfoList) {
			
			 OrderDetail orderDetail = new OrderDetail();
			 orderDetail.setSkuId(cartInfo.getSkuId());
			 orderDetail.setSkuName(cartInfo.getSkuName()); 
			 orderDetail.setImgUrl(cartInfo.getImgUrl());
			 orderDetail.setSkuNum(cartInfo.getSkuNum());
			 orderDetail.setOrderPrice(cartInfo.getCartPrice());
			 
			 
			 orderDetailList.add(orderDetail);
			
		}
		
		//总金额
		OrderInfo orderInfo = new OrderInfo();
		
		orderInfo.setOrderDetailList(orderDetailList);
		
		orderInfo.sumTotalAmount();
		
		
		
		String tradeNo = orderService.getTradeNo(userId);
		
		req.setAttribute("tradeNo", tradeNo);
		
		
		//保存总金额
		req.setAttribute("totalAmount", orderInfo.getTotalAmount());
		
		
		
		//保存送货清单集合
		req.setAttribute("orderDetailList",orderDetailList);
		return	"trade";
		
		
		
		
	}
	
	
	
	@RequestMapping("submitOrder")
	@LoginRequire
	
	public String submitOrder(HttpServletRequest  req ,OrderInfo orderInfo ) {
		
		String userId = (String)req.getAttribute("userId");
		//调用服务层
		orderInfo.setUserId(userId);
		
		//判断是否重复提交
		//先获取页面的流水号
		
   String tradeNo = req.getParameter("tradeNo");

  boolean result=orderService.checkTradeCode(userId, tradeNo);
  //是重复提交
  if(!result) {
	  req.setAttribute("errMsg", "订单已经提交,不能重复提交!");
	  return "tradeFail";
	  
  }
  
  //验证库存
  List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();
  for (OrderDetail orderDetail : orderDetailList) {
	  
	  boolean flag = orderService.checkStock(orderDetail.getSkuId(), orderDetail.getSkuNum());
	  if(!flag) {
		  req.setAttribute("errMsg", orderDetail.getSkuName()+"商品库存不足!");
		  return "tradeFail"; 
		
	  }
	
	 //获取呢skuInfo对象
	  SkuInfo skuInfo=manageService.getSkuInfo(orderDetail.getSkuId());
	  
	  int res = skuInfo.getPrice().compareTo(orderDetail.getOrderPrice());
	  
	  if(res!=0) {
		  req.setAttribute("errMsg", orderDetail.getSkuName()+"商品价格不匹配!");
		  cartService.loadCartCache(userId);
	    
		  return "tradeFail"; 
		  
	  }
	  
}
  
  
		
		
		String orderId=orderService.saveOrder(orderInfo);
		
		//删除流水号
		
		orderService.delTradeCode(userId);
		
		//支付
		return "redirect://payment.gmall.com/index?orderId="+orderId;
		
	}
	
	//拆单
	@RequestMapping("orderSplit")
	@ResponseBody
	public String orderSplit(HttpServletRequest req ) {
		
		String orderId =req.getParameter("orderId");
		String wareSkuMap=req.getParameter("wareSkuMap");

		
	//返回子菜单的集合	
	List<OrderInfo> orderInfoList=orderService.orderSplit(orderId,wareSkuMap);
	//创建一个集合存储map
	ArrayList<Map> arrayList = new ArrayList<>();
	for (OrderInfo orderInfo : orderInfoList) {
		Map map = orderService.initWareOrder(orderInfo);
		
		arrayList.add(map);
		
		
	}
		
		
		return JSON.toJSONString(arrayList);
		
	}
	

}
