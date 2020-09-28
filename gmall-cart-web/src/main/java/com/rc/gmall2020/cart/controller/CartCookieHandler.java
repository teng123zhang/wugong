package com.rc.gmall2020.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.rc.gmall2020.bean.CartInfo;
import com.rc.gmall2020.bean.SkuInfo;
import com.rc.gmall2020.config.CookieUtil;
import com.rc.gmall2020.service.ManageService;

import tk.mybatis.mapper.util.StringUtil;

@Component
public class CartCookieHandler {
	
	@Reference
	
	private ManageService  manageService; 
	
	//定义购物车名称
	private String cookieCarName ="CART";
	
	//设置cookie过期时间
	private int COOKIE_CART_MAXAGB=7*24*3600;
/**
 * 添加购物车
 * @param request
 * @param response
 * @param skuId
 * @param userId
 * @param skuNum
 */
	public void addToCart(HttpServletRequest request, HttpServletResponse response, String skuId, String userId,
			int skuNum) {
		/**
		 * 1先要去查看购物车中是否有商品
		 * 2true 数量想加
		 * 3false 直接添加
		 * 
		 */
		
		//从cookie中获取购物车数据
		String cookieValue = CookieUtil.getCookieValue(request, cookieCarName , true);
		//String token=CookieUtil.getCookieValue(request, "token", false);
		System.out.println("cookieValue"+cookieValue);
		List<CartInfo> cartInfoList= new ArrayList<>();
		//如果没有则直接添加到集合！借助一个boolean类型的处理
		boolean ifExist =false;
		//判断cookieValue不能为空
		if(StringUtils.isNotEmpty(cookieValue)) {
			
			
		
		//该字符中包含很多个cartInfo实体类 List<CartInfo>
		 cartInfoList = JSON.parseArray(cookieValue,CartInfo.class);
		//判断是否有该商品
		for (CartInfo cartInfo : cartInfoList) {
			//比较条件是商品的id
			//if(cartInfo.getSkuId().equals(skuId)) {
				//有商品
				//数量想加
				cartInfo.setSkuNum(cartInfo.getSkuNum()+skuNum);
				
				//实时价格初始化
				 cartInfo.setCartPrice(cartInfo.getSkuPrice());;
				
				//将变量更改为true
				ifExist=true;
				
			//}
			
		}
		
		}
		//在购物车中没有该商品
		if(!ifExist) {
			
			SkuInfo skuInfo = manageService.getSkuInfo(skuId);
			//将该商品添加到集合中
			CartInfo cartInfo = new CartInfo();
			
			//属性赋值
			cartInfo.setSkuId(skuId);
			cartInfo.setCartPrice(skuInfo.getPrice());
			cartInfo.setSkuPrice(skuInfo.getPrice());
			cartInfo.setSkuName(skuInfo.getSkuName());
			cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
			cartInfo.setUserId(userId);
			cartInfo.setSkuNum(skuNum);
			//添加集合
			cartInfoList.add(cartInfo);
			
			
			
			
			
		}
		
		//将最终的集合放入cookie中,
		CookieUtil.setCookie(request, response, cookieCarName, JSON.toJSONString(cartInfoList), COOKIE_CART_MAXAGB,true);
		
		
	}
public List<CartInfo> getCartList(HttpServletRequest request) {

//未登陆数据集合
	String cookieValue = CookieUtil.getCookieValue(request, cookieCarName, true);
	if(StringUtils.isNotEmpty(cookieValue)) {
		
		List<CartInfo> cartInfoList = JSON.parseArray(cookieValue,CartInfo.class);
		return cartInfoList;
	}
	return null;
	
}
//删除未登陆购物车
public void deleteCartCookie(HttpServletRequest request, HttpServletResponse response) {
	CookieUtil.deleteCookie(request, response, cookieCarName);
	
}
/**
 * 
 * @param req>0
 * @param resp
 * @param skuId
 * @param isChecked
 */
public void checkCart(HttpServletRequest req, HttpServletResponse resp, String skuId, String isChecked) {
	
	//直接将isChecked值赋值给购物车集合
	
	List<CartInfo> cartList = getCartList(req);
	
	if(cartList!=null && cartList.size()>0) {
		for (CartInfo cartInfo : cartList) {
			
			if(cartInfo.getSkuId().equals(skuId)) {
				cartInfo.setIsChecked(isChecked);
			}
			
		}
	
	
	
	
}
	//购物车集合写会cookie
	
	CookieUtil.setCookie(req, resp, cookieCarName, JSON.toJSONString(cartList), COOKIE_CART_MAXAGB, true);

}
}