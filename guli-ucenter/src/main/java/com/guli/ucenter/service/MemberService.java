package com.guli.ucenter.service;

import com.guli.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-19
 */
public interface MemberService extends IService<Member> {
	
	Integer RegisterNum(String day);
//根据openid判断
	Member getOpenUserInfo(String openid);
	
	String login(Member member);

}
