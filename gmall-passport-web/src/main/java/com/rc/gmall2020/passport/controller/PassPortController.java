package com.rc.gmall2020.passport.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rc.gmall2020.bean.UserInfo;
import com.rc.gmall2020.passport.config.JwtUtil;
import com.rc.gmall2020.service.UserService;

@Controller
public class PassPortController {
	
	@Value("${token.key}")
	
	private String key;
	@Reference
	private UserService userService; 
	
	@RequestMapping("index")
	
	public String index(HttpServletRequest request ) {
		String originUrl = request.getParameter("originUrl");
		
		request.setAttribute("originUrl", originUrl);
		
		
		
		return "index";
	}
	//在控制器中获取页面的数据
	@RequestMapping("login")
	@ResponseBody
	
	public String login(UserInfo userInfo,HttpServletRequest request) {
		
		//salt 服务器的Ip地址
		//在生成token时需要获取服务器的ip地址
		//在nginx.conf中配置
		/**
		 *      upstream passport.atguli.com{
       server 192.168.134.1:8087;


}
   server {
      listen        80;
      server_name  passport.atguli.com;
      location / {
      proxy_pass http://passport.atguli.com;
  proxy_set_header X-forwarded-for $proxy_add_x_forwarded_for;
}

}

		 */
		String salt = request.getHeader("X-forwarded-for");
		//调用服务层
	UserInfo info= 	userService.login(userInfo);
	if(userInfo!=null) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userId",info.getId());
		map.put("nickName",info.getNickName());
		//生成token
		String token = JwtUtil.encode(key, map , salt);
		System.out.println("token"+token);
		return token;
	}else {
		return "fail";
		
	}
		
		
	}
	
	
	@RequestMapping("verify")
	
	@ResponseBody
	
	
	/**
	 * 1获取服务器 的ip和token
	 * 2key+ip揭秘token 得到用户信息 userId 和nickName
	 * 3判断用户是否登陆  key=user：userId：info value=userInfo
	 * 4userInfo！=null true :success false:fail
	 * 
	 * 
	 */
	
	
	public String verify(HttpServletRequest request) {
		
		//String salt= request.getHeader("X-forwarded-for");
		
		String token = request.getParameter("token");
		String salt = request.getParameter("salt");
		
		Map<String, Object> map = JwtUtil.decode(token, key, salt);
		if(map!=null && map.size()>0) {
			//获取到userId
			String userId = (String)map.get("userId");
			String nickName =(String)map.get("nickName");
			//调用用户查询用户是否已经登陆
			UserInfo userInfo=userService.verify(userId);
			
			if(userInfo!=null) {
				return "success";
			
			}
		}
		
		return "fail";
	}
	

}
