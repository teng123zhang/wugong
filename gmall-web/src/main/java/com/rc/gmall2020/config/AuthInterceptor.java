package com.rc.gmall2020.config;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.rc.gmall2020.util.HttpClientUtil;

import io.jsonwebtoken.impl.Base64UrlCodec;
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter{
	  // 多个拦截器执行的顺序？
    // 跟配置文件中，配置拦截器的顺序有关系 1 ，2
    // 用户进入控制器之前！
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 如何获取到token ？
        // 用户在登录完成之后 会返回一个 url
        // https://www.jd.com/?newToken=eyJhbGciOiJIUzI1NiJ9.eyJuaWNrTmFtZSI6IkF0Z3VpZ3UiLCJ1c2VySWQiOiIxIn0.XzRrXwDhYywUAFn-ICLJ9t3Xwz7RHo1VVwZZGNdKaaQ

		
		String token = request.getParameter("newToken");
		System.out.println(token+"token");
		
	       // 将token 放入cookie 中！
//      Cookie cookie = new Cookie("token",token);
//      response.addCookie(cookie);
      // 当token 不为null 时候放cookie
		if(token!=null) {
			
			CookieUtil.setCookie(request, response, "token", token, WebConst.COOKIE_MAXAGE, false);
		}
		//当用户访问非登陆页面，登录名之后，继续访问其他业务模块的时候，url并没有newToken，但是后台可能将token存入了cookie中
		
		if(token==null) {
			token=CookieUtil.getCookieValue(request, "token", false);
		
			
			
		}
		
		//从cookie中获取token，揭秘token
		if(token!=null) {
			//开始揭秘token 获取nickName
			Map map =getUserMapByToken(token);
			String nickName=(String) map.get("nickName");
			//保存到作用域
			request.setAttribute("nickName", nickName);
		}
		
		//在拦截器上获取方法的注解!
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		LoginRequire methodAnnotation = handlerMethod.getMethodAnnotation(LoginRequire.class);
		if(methodAnnotation!=null) {
			//此时有注解，判断用户是否登陆?调用verify
			//http://passport.atguli.com/verify?token=xxx&sal=x
			//获取服务器的id地址
			String salt = request.getHeader("X-forwarded-for");
			//调用verify()认证
			String result = HttpClientUtil.doGet(WebConst.VERIFY_ADDRESS+"?token="+token+"&salt="+salt);
			if("success".equals(result)) {
				//登陆，认证成功
				//保存一下userId
				
				//开始揭秘token 获取nickName
				Map map =getUserMapByToken(token);
				String userId=(String) map.get("userId");
				//保存到作用域
				
				request.setAttribute("userId", userId);
				//String id =(String) request.getAttribute("userId");
				
				//System.out.println(id+"nimabicao");
				
				return true;
			}else {
				//认证失败并且methodAnnotation=true必须登陆
				if(methodAnnotation.autoRedirect()) {
					//必须登陆！跳转到页面
					//http://passport.atguli.com/originUrl=http....45.html
					//先获取到url
					String requestUrl= request.getRequestURL().toString();
					//http://item.gmall.com/45.html
					System.out.println(requestUrl+"==>requestUrl");
					//将url进行转换
					//http...45.html
					String encodeURL = URLEncoder.encode(requestUrl,"UTF-8");
					//http://转码后的html
					System.out.println("encodeURL==>"+encodeURL);
					response.sendRedirect(WebConst.LOGIN_ADDRESS+"?originUrl="+encodeURL);
					return false;
					
					
					
				}
				
				
			}
		}
		return true;
	}

	private Map getUserMapByToken(String token) {
		//获取中间部分 
		String tokenUserInfo = StringUtils.substringBetween(token, ".");
		System.out.println(tokenUserInfo+":tokenUserInfo");
		//将tokenUserInfo 进行base64解码
		Base64UrlCodec base64UrlCodec = new  Base64UrlCodec();
		//解码之后得到的byte
		byte[] decode = base64UrlCodec.decode(tokenUserInfo);
		//需要将decode装成String
		String mapJson=null;
		try {
			 mapJson = new String(decode,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
		//将字符串转换为map之间返回
		return JSON.parseObject(mapJson,Map.class);
		
		
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void afterCompletion(
			HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	
	

}
