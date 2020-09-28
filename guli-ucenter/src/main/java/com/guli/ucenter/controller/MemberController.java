package com.guli.ucenter.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.guli.common.result.Result;
import com.guli.ucenter.entity.Member;
import com.guli.ucenter.entity.UcenterMapperOrder;

import com.guli.ucenter.service.MemberService;
import com.guli.ucenter.utils.JwtUtils;

import io.jsonwebtoken.Claims;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-19
 */
@RestController
@RequestMapping("/ucenter")
@CrossOrigin
public class MemberController {
	@Autowired
	private MemberService  memberService; 
	@GetMapping(value = "/member/{day}")
	public Result getRegisterNum(@PathVariable String day) {
		Integer count = memberService.RegisterNum(day);
		
		return Result.ok().data("RegisterNum",count);
		
		
	} 
	
	 //登录
    @PostMapping("login")
    public Result loginUser(@RequestBody Member member) {
        //member对象封装手机号和密码
        //调用service方法实现登录
        //返回token值，使用jwt生成
        String token = memberService.login(member);
        return Result.ok().data("token", token);
    }
	
	/**
	 * 根据token值获取用户信息
	 */
	
	@PostMapping("getUserInfo/{token}")
	
	public Result getUserInfoToken(@PathVariable String token) {
		
		Claims cs = JwtUtils.checkJWT(token);
		String id=(String)cs.get("id");
		String nickname = (String)cs.get("nickname");
		String avatar =(String)cs.get("avatar");
		Member member = new Member();
		member.setId(id);
		member.setNickname(nickname);
		member.setAvatar(avatar);
		return Result.ok().data("member",member);
		
		
	}
	
	//根据会员id获取用户信息
    @GetMapping("getInfoUc/{id}")
    public Result getMemberInfoById(@PathVariable String id,  UcenterMapperOrder m) {
        Member ucenterMember = memberService.getById(id);
        //根据用户id获取用户信息
        UcenterMapperOrder member = new  UcenterMapperOrder();
        BeanUtils.copyProperties(ucenterMember, member);
        return Result.ok().data("member", JSONObject.toJSONString(member));

    }
    
    
    //根据用户id获取用户信息
    @PostMapping(value="getUserInfoOrder/{id}")
    public  UcenterMapperOrder  getUserInfoOrder(@PathVariable String id) {
        Member member = memberService.getById(id);
        //把member对象里面值复制给UcenterMemberOrder对象
        UcenterMapperOrder  ucenterMemberOrder = new  UcenterMapperOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);
        
        System.out.println(member.toString());
        return ucenterMemberOrder;
    }
    
    
   
//
//    //注册
//    @PostMapping("register")
//    public Result registerUser(@RequestBody RegisterVo registerVo) {
//        memberService.register(registerVo);
//        return Result.ok();
//    }

    //根据token获取用户信息
    @GetMapping("getMemberInfo")
    public Result getMemberInfo(HttpServletRequest request) {
        //调用jwt工具类的方法。根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据用户id获取用户信息
       Member member = memberService.getById(memberId);
        return Result.ok().data("userInfo", member);
    }

   


}

