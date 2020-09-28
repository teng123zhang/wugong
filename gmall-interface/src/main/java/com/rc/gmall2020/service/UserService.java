package com.rc.gmall2020.service;

import java.util.List;

import com.rc.gmall2020.bean.UserAddress;
import com.rc.gmall2020.bean.UserInfo;

public interface UserService {
	/**
	 * 查询素有数据
	 * @return
	 */
	List<UserInfo> findAll();
	
	/**
	 * 根据用户id查询地址列表
	 * 
	 */
	
	List<UserAddress> getUserAddressList(String userId);
/**
 * 单点登陆
 * @param userInfo
 * @return
 */
	UserInfo login(UserInfo userInfo);
/**
 * 根据用户id查询数据
 * @param userId
 * @return
 */
UserInfo verify(String userId);

}
