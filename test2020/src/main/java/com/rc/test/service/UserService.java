package com.rc.test.service;

import java.util.List;

import com.rc.test.bean.UserInfo;

//业务逻辑层
public interface UserService {
	/**
	 * 查询所有数据
	 * @return
	 */
	
	List<UserInfo> findAll();
	
	/**
	 * 添加
	 * @param userInfo
	 */
	
	void addUser(UserInfo userInfo);
	/**
	 * 根据id修改name
	 * @param userInfo
	 */
	
	void updateUser(UserInfo userInfo);
	
	/**
	 * 根据name 修改Loginname
	 * 
	 */
	
	
	void updByName(UserInfo userInfo);
	
	/**
	 * 
	 * @param id
	 */
	void delById(String id);
	
	/**
	 * 
	 * @param name
	 */
	
	void delByName(String name);
	
	

}
