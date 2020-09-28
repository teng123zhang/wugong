package com.rc.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rc.test.bean.UserInfo;
import com.rc.test.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	
	private UserService userService;
	@RequestMapping("findAll")
	
	public List<UserInfo> findAll(){
		
		 List<UserInfo> findAll = userService.findAll();
		return findAll;
		
		
	}
	
	@RequestMapping("addUser")
	
	public void addUser(UserInfo userInfo) {
		userInfo.setLoginName("adminStr");
		userInfo.setPasswd("66666");
		userService.addUser(userInfo);
		
		System.out.println("主键："+userInfo.getId());
	}
	
	@RequestMapping("updById")
	
	public String updById(UserInfo userInfo) {
		userInfo.setName("龙少爷");
		userInfo.setId("5");
		userService.updateUser(userInfo);
		return "OK";
	}
	
	
@RequestMapping("updByName")
	
	public String updByName(UserInfo userInfo) {
	userInfo.setName("龙少爷");
	userInfo.setLoginName("longshaoye123");
		userService.updByName(userInfo);;
		return "OK";
	}
	

@RequestMapping("delById")

public String delById(UserInfo userInfo) {
	userService.delById(userInfo.getId());
	return "OK";
}


@RequestMapping("delByName")

public String delByName(UserInfo userInfo) {
	userService.delByName(userInfo.getName());
	return "OK";
}

	

}
