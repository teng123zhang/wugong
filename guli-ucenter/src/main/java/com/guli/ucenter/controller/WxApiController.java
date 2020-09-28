 package com.guli.ucenter.controller;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.guli.ucenter.entity.Member;
import com.guli.ucenter.service.MemberService;
import com.guli.ucenter.utils.ConstantPropertiesUtil;
import com.guli.ucenter.utils.HttpClientUtils;
import com.guli.ucenter.utils.JwtUtils;

@Controller
@CrossOrigin
@RequestMapping("/api/ucenter/wx")
public class WxApiController {
	@Autowired
	private MemberService memberService;
	@GetMapping("callback")
	
	public String callback(String code,String state, HttpSession session) {
		
		
		//得到授权的临时票据code
//		
//		System.out.println("code="+code);
//		System.out.println("state="+state);
		
		String bassAccessTokenUrl="https://api.weixin.qq.com/sns/oauth2/access_token"+
		                           "?appid=%s"+
				                   "&secret=%s"+
		                           "&code=%s"+
				                   "&grant_type=authorization_code";
		
		//设置参数
		bassAccessTokenUrl=String.format(bassAccessTokenUrl, 
				ConstantPropertiesUtil.WX_OPEN_APP_ID,
				ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
				code);
		
		//使用httpclient请求拼接之后的路径，获取里面的路径
		try {
			String resultAccessTokenUrl = HttpClientUtils.get(bassAccessTokenUrl);
			Gson gson = new Gson();
			
			Map<String, Object> map=gson.fromJson(resultAccessTokenUrl, HashMap.class);
			String access_token=(String)map.get("access_token");//获取数据凭证
			String openid=(String)map.get("openid");//微信唯一id
			
			
			//3拿着access_token,openid 去请求固定地址
			String baseUserInfoUrl= "https://api.weixin.qq.com/sns/userinfo"+
			                        "?access_token=%s"+
					                 "&openid=%s";
			baseUserInfoUrl=String.format(baseUserInfoUrl, access_token,openid);
			
			//http请求地址
			String resultUserInfo = HttpClientUtils.get(baseUserInfoUrl);
			//System.out.println("*******************"+resultUserInfo);
			Map<String,Object> resultMap = gson.fromJson(resultUserInfo, HashMap.class);
			String nickname=(String)resultMap.get("nickname"); // 昵称
			String headimgurl=(String)resultMap.get("headimgurl");  //头像
			String mobile= (String)resultMap.get("mobile");
			//判断用户表是否已经存在添加用户信息,如果不存在,添加;
			Member member=memberService.getOpenUserInfo(openid);
			if(member==null) {
				//添加
				member= new Member();
				member.setNickname(nickname);
				member.setAvatar(headimgurl);
				member.setOpenid(openid);
				member.setMobile(mobile);
				memberService.save(member);
			}
			
			//根据member对象生成jwt
			String token = JwtUtils.genJsonWebToken(member);
			
			return "redirect:http://localhost:3000?token="+token;
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	
	@GetMapping("login")
	
	public String genQrConnect() {
		
		
		
		
		try {
			//微信开放平台授权baseUrl
			//%s表示?占位符
			String baseUrl="https://open.weixin.qq.com/connect/qrconnect"+
			               "?appid=%s"+
					       "&redirect_uri=%s"+
			               "&response_type=code"+
					       "&scope=snsapi_login"+
			               "&state=%s"+
					       "#wechat_redirect";
			//生成二维码的方法
			String redirectUrl =ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
			redirectUrl=URLEncoder.encode(redirectUrl, "utf-8");
			String state= "7uvjtwq5"; //内网穿透设置的域名
			//把参数设置到路径里面
			baseUrl=String.format(baseUrl, ConstantPropertiesUtil.WX_OPEN_APP_ID,
					redirectUrl, state);
			return "redirect:"+baseUrl;
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
			return null;
			
		}
	}
	

}
