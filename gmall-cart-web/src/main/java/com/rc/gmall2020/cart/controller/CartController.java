package com.rc.gmall2020.cart.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rc.gmall2020.bean.CartInfo;
import com.rc.gmall2020.bean.SkuInfo;
import com.rc.gmall2020.config.CookieUtil;
import com.rc.gmall2020.config.LoginRequire;
import com.rc.gmall2020.service.CartService;
import com.rc.gmall2020.service.ManageService;

import tk.mybatis.mapper.generator.FalseMethodPlugin;

@Controller
public class CartController {
	@Reference
	private CartService cartService;
	
	@Reference
	
	private ManageService manageService;
	
	@Autowired
	
	private CartCookieHandler  cartCookieHandler; 
	@LoginRequire(autoRedirect = false) //不需要登陆
	@RequestMapping("addToCart")
	public String  addToCart(HttpServletRequest request,HttpServletResponse response) {
		//获取商品数量
		String skuNum = request.getParameter("skuNum");
		String skuId = request.getParameter("skuId");
		System.out.println(skuNum+"skuNum");
		//获取用户 userId
		String userId=(String) request.getAttribute("userId");
		String nickName=(String)request.getAttribute("nickName");
		System.out.println("userId:"+userId);
		if(userId!=null) {
			//调用登陆添加购物车
			cartService.addToCart(skuId, userId, Integer.parseInt(skuNum));
			
		}else {
			//调用未登陆添加购物车
			cartCookieHandler.addToCart(request,response,skuId,userId,Integer.parseInt(skuNum));
			
			
		}
		//根据skuId 查询skuInfo
		SkuInfo skuInfo = manageService.getSkuInfo(skuId);
		request.setAttribute("skuNum", skuNum);
		request.setAttribute("skuInfo", skuInfo);
		return "success";
	}
	
	
	
	
	
	
	
	@RequestMapping("cartList")
	@LoginRequire(autoRedirect = false)
	public String carList(HttpServletRequest request,HttpServletResponse response) {
		
		//获取用户 userId
				String userId=(String) request.getAttribute("userId");
				System.out.println("userId:"+userId);
				List<CartInfo> cartInfoList=null;
				if(userId!=null) {
					List<CartInfo> cartListCK = cartCookieHandler.getCartList(request);
					System.out.println(cartListCK+"===cartListCK " );
					if(cartListCK!=null) {
						//合并购物车
						cartInfoList=cartService.mergeToCarList(cartListCK,userId);
						//删除未登陆的购物车
						cartCookieHandler.deleteCartCookie(request,response);
					}else {
						
						//登陆状态下查询购物车
						cartInfoList=cartService.getCartList(userId);
					}
					
				}else {
					
					//调用未登陆添加购物车
					
				cartInfoList=	cartCookieHandler.getCartList(request);
					
					
					
				}
		
		//保存购物车集合
				request.setAttribute("cartInfoList", cartInfoList);
		return "cartList";
	}
	
	
	
	
	@RequestMapping("checkCart")
	@LoginRequire(autoRedirect = false)
	@ResponseBody
	
	
	public void checkCart(HttpServletRequest req ,HttpServletResponse resp) {
		
		//获取页面传递的数据
		
		String isChecked = req.getParameter("isChecked");
		
		String skuId = req.getParameter("skuId");
		
		String userId = (String)req.getAttribute("userId");
		
		if(userId!=null) {
			//登陆状态
			
			cartService.checkCart(skuId,isChecked,userId);
		}else {
			
			//未登陆状态
			
			cartCookieHandler.checkCart(req,resp,skuId,isChecked);
			
		}
		
		
		
		
		
	}
	
	@RequestMapping("toTrade")
	
	@LoginRequire
	
	public String toTrade(HttpServletRequest req,HttpServletResponse resp) {
		
		//合并勾选的商品 未登陆和登陆
		
		List<CartInfo> cartListCK = cartCookieHandler.getCartList(req);
		
		String userId = (String)req.getAttribute("userId");
		
		if(cartListCK!=null && cartListCK.size()>0) {
			//合并
			
			cartService.mergeToCarList(cartListCK, userId);
			
			//删除未登陆
			
			cartCookieHandler.deleteCartCookie(req, resp);
			
			
		}
		
		
		return "redirect://order.gmall.com/trade";
		
		
		
	}
	
	

}
