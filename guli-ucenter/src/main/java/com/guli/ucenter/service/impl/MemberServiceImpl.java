package com.guli.ucenter.service.impl;

import com.guli.ucenter.entity.Member;
import com.guli.ucenter.exception.OrderException;
import com.guli.ucenter.mapper.MemberMapper;
import com.guli.ucenter.service.MemberService;
import com.guli.ucenter.utils.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-19
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {
  
   
	@Override
	public Integer RegisterNum(String day) {
		Integer countRegisterNum = baseMapper.countRegisterNum(day);
		return countRegisterNum;
	}

	@Override
	public Member getOpenUserInfo(String openid) {
		QueryWrapper<Member>	wrapper=new QueryWrapper<Member>();
		wrapper.eq("openid", openid);
		Member  member = baseMapper.selectOne(wrapper);
		
		return member;
	}

	
		
		//登录的方法
	    
	    public String login(Member member) {
	        //获取登录手机号和密码
	        String mobile = member.getMobile();
	       // String password = member.getPassword();

	        //手机号和密码非空判断
	        if(StringUtils.isEmpty(mobile)) {
	            throw new OrderException(20001,"登录失败，输入有空");
	        }

	        //判断手机号是否正确
	        QueryWrapper<Member> wrapper = new QueryWrapper<>();
	        wrapper.eq("mobile",mobile);
	        Member mobileMember = baseMapper.selectOne(wrapper);
	        System.out.println(mobileMember.toString());
	        //判断查询对象是否为空
	        if(mobileMember == null) {//没有这个手机号
	            throw new OrderException(20001,"登录失败，没有这个手机号");
	        }

	        //判断密码
	        //因为存储到数据库密码肯定加密的
	        //把输入的密码进行加密，再和数据库密码进行比较
	        //加密方式 MD5
//	        if(!MD5.encrypt(password).equals(mobileMember.getPassword())) {
//	            throw new OrderException(20001,"登录失败,密码错误");
//	        }
//
//	        //判断用户是否禁用
//	        if(mobileMember.getIsDisabled()) {
//	            throw new OrderException(20001,"登录失败，用户禁用");
//	        }

	        //登录成功
	        //生成token字符串，使用jwt工具类
	        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());
	        
	        System.out.println("token==>"+jwtToken);
	        return jwtToken;
	 
		
		
	
}
}
