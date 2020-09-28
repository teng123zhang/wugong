package com.rc.test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rc.test.bean.UserInfo;
import com.rc.test.mapper.UserMapper;
import com.rc.test.service.UserService;

import tk.mybatis.mapper.entity.Example;

@Service
public class UserServiceImpl implements UserService {
   //表示当前UserInfoMapper在容器中
	@Autowired
	private UserMapper userMapper; 
	public List<UserInfo> findAll() {
		List<UserInfo> selectAll = userMapper.selectAll();
		return selectAll;
	}
	@Override
	public void addUser(UserInfo userInfo) {
		
		userMapper.insertSelective(userInfo);
	}
	@Override
	public void updateUser(UserInfo userInfo) {
		//UserInfo userInfo1=  new UserInfo();
		userInfo.setName("龙少爷");
		userInfo.setId("5");
		userMapper.updateByPrimaryKeySelective(userInfo);
		
	}
	@Override
	public void updByName(UserInfo userInfo) {
		//userInfo.setName("龙少爷");
		Example example = new Example(UserInfo.class); 
		example.createCriteria().andEqualTo("name", userInfo.getName());
		userMapper.updateByExampleSelective(userInfo,example );
	}
	@Override
	public void delById(String id) {
		
		userMapper.deleteByPrimaryKey(id);	
	}
	@Override
	public void delByName(String name) {
		Example example = new Example(UserInfo.class);
		example.createCriteria().andEqualTo("name",name);
		userMapper.deleteByExample(example);
	}
	

}
