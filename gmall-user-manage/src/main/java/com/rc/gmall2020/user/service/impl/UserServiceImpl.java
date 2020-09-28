package com.rc.gmall2020.user.service.impl;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rc.gmall2020.bean.UserAddress;
import com.rc.gmall2020.bean.UserInfo;
import com.rc.gmall2020.service.UserService;
import com.rc.gmall2020.user.mapper.UserAddressMapper;
import com.rc.gmall2020.user.mapper.UserInfoMapper;
import com.rc.gmall2020.util.config.RedisUtil;

import redis.clients.jedis.Jedis;
@Service
public class UserServiceImpl implements UserService {
	
	
	public String userKey_prefix="user:";
	public String userinfoKey_suffix=":info";
	public int userKey_timeout=60*60*24;
  @Autowired
  
  private UserInfoMapper userInfoMapper;
  

  
  @Autowired
   
  private RedisUtil  redisUtil;
  @Autowired
  private UserAddressMapper userAddressMapper;
	@Override
	public List<UserInfo> findAll() {
		List<UserInfo> selectAll = userInfoMapper.selectAll();
		return selectAll;
	}
	@Override
	public List<UserAddress> getUserAddressList(String userId) {
		UserAddress userAddress= new UserAddress();
		userAddress.setId(userId);
		return userAddressMapper.select(userAddress);
		
	}
	@Override
	public UserInfo login(UserInfo userInfo) {
		String passwd = userInfo.getPasswd();
		//对用户密码进行加密
		String newPwd = DigestUtils.md5Hex(passwd.getBytes());
		//将加密后的密码赋值给当前对象
		userInfo.setPasswd(newPwd);
		UserInfo info = userInfoMapper.selectOne(userInfo);
		if(info!=null) {
			
			//获取jedis
			Jedis jedis = redisUtil.getJedis();
			//放入redis必须起一个key
			
			String userKey= userKey_prefix+info.getId()+userinfoKey_suffix;
			//数据类型?
			jedis.setex(userKey,userKey_timeout,JSON.toJSONString(info));
			//关闭jedis
			jedis.close();
			
			return info;
			
		}
		return null;
	}
	@Override
	public UserInfo verify(String userId) {
		Jedis jedis=null;
		try {
			 jedis = redisUtil.getJedis();
			String userKey=userKey_prefix+userId+userinfoKey_suffix;
			String userJson = jedis.get(userKey);
			if(!StringUtils.isEmpty(userJson)) {
				UserInfo userInfo = JSON.parseObject(userJson,UserInfo.class);
				
				return userInfo;
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally {
			if(jedis!=null) {
				jedis.close();
			}
			
			
		}
		return null;
	}
	
	

}
