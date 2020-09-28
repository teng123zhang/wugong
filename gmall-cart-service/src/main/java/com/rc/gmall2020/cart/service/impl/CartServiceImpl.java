package com.rc.gmall2020.cart.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.annotations.JsonAdapter;
import com.rc.gmall2020.bean.CartInfo;
import com.rc.gmall2020.bean.SkuInfo;
import com.rc.gmall2020.cart.service.constant.CartConst;
import com.rc.gmall2020.cart.service.mapper.CartMapper;
import com.rc.gmall2020.service.CartService;
import com.rc.gmall2020.service.ManageService;
import com.rc.gmall2020.util.config.RedisUtil;

import redis.clients.jedis.Jedis;
@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private CartMapper cartMapper; 
	@Autowired
	private RedisUtil redisUtil; 
	@Reference
	private ManageService  manageService;
    //添加登陆还是未登陆 控制器判断
	//登陆时添加购物车!
	@Override
	public void addToCart(String skuId, String userId, Integer skuNum) {
		/**
		 * 1先查询一下购物车中是否有同样的商品，如果有则数量想加
		 * 2如果没有，直接添加到数据库
		 * 3更新缓存
		 */
		
		//获取jedis
		Jedis jedis = redisUtil.getJedis();
		//定义购物车的key
		String cartKey= CartConst.USER_KEY_PREFIX+userId+CartConst.USER_CART_KEY_SUFFIX;
		//同步缓存
		//采用那种数据类型来存储 hash
		//setex 是String 类型
		//hset 是map类型
		//key= user:userId:cart field =skuId value= CartInfo的字符串
		
		
		//根据skuId userId 查询一下 是否有该商品
		
		//select * from cartInfo userId=？ and skuId =?
	CartInfo cartInfo = new	CartInfo();
	cartInfo.setUserId(userId);
	cartInfo.setSkuId(skuId);
		CartInfo cartInfoBxist = cartMapper.selectOne(cartInfo);
		System.out.println(cartInfoBxist);
		//有相同的商品
		if(cartInfoBxist!=null) {
			//数量相加
			cartInfoBxist.setSkuNum(cartInfoBxist.getSkuNum()+skuNum);
			//给skuPrice 一个初始化的值 skuPrice= cartPrice
			cartInfoBxist.setSkuPrice(cartInfoBxist.getCartPrice());
			//System.out.println("cartInfoBxist.getSkuPrice()==="+cartInfoBxist.getSkuPrice());
			//更新数据库
			cartMapper.updateByPrimaryKeySelective(cartInfoBxist);
			
			
		}else {
			//没有相同的商品
			CartInfo cartInfo2=new CartInfo();
			SkuInfo skuInfo = manageService.getSkuInfo(skuId);
			//属性赋值
			cartInfo2.setSkuId(skuId);
			cartInfo2.setCartPrice(skuInfo.getPrice());
			cartInfo2.setSkuPrice(skuInfo.getPrice());
			cartInfo2.setImgUrl(skuInfo.getSkuDefaultImg());
			cartInfo2.setSkuName(skuInfo.getSkuName());
			cartInfo2.setUserId(userId);
			cartInfo2.setSkuNum(skuNum);
			//添加到数据库
			cartMapper.insertSelective(cartInfo2);
			cartInfoBxist=cartInfo2;
			
		}
		//同步缓存
		jedis.hset(cartKey, skuId, JSON.toJSONString(cartInfoBxist));
	   
	    
	    //缓存的过期时间
	    //购物车需要过期时间吗?不去设置失效时间
	    
	    //设置失效时间 与用户的过期事件一致
	    
	    String userKey = CartConst.USER_KEY_PREFIX+userId+CartConst.USERINFOKEY_SUFFIX;
	    
	   //如何计算userKey的过期时间
	    Long ttl = jedis.ttl(userKey);
	    
	    //给购物车设置过期时间
	    jedis.expire(cartKey, ttl.intValue());
	    
	    //关闭jedis
	    jedis.close();
	    
		
	}
	@Override
	public List<CartInfo> getCartList(String userId) {
		List<CartInfo> cartInfoList = new ArrayList<CartInfo>();
		
		    Jedis jedis = redisUtil.getJedis();
		   //定义购物车的key
				String cartKey= CartConst.USER_KEY_PREFIX+userId+CartConst.USER_CART_KEY_SUFFIX;
				//从key中获取值
				//jedis.hgetAll(cartKey) 返回map key = field value=cartInfo 字符串
				 List<String> cartList = jedis.hvals(cartKey);//返回List集合 String =cartInfo 字符串
				 //从缓存中获取数据
				 if(cartList!=null&&cartList.size()>0) {
					 //循环遍历
					 for (String cartInfoStr : cartList) {
						// cartInfoStr 转换为对象 CartInfo 并添加到集合
						 cartInfoList.add(JSON.parseObject(cartInfoStr, CartInfo.class));
					}
					 //查看的时候应该做排序真实项目按照时间排序  更新时间 模拟按照id排序
					 cartInfoList.sort(new Comparator<CartInfo>() {

						@Override
						public int compare(CartInfo o1, CartInfo o2) {
							//定义比较规则
							//compareTo 比较规则：s1=abc s2=abcd
							
							return o1.getId().compareTo(o2.getId());
						}
					});
					 
					 return  cartInfoList;
				 }else {
					 //从数据库获取数据
					 
					 cartInfoList=loadCartCache(userId);
					 return cartInfoList;
					 
				 }
		
	}
	//根据用户id查询购物车(skuPrice 实时价格)
	public  List<CartInfo> loadCartCache(String userId) {
		//cartInfo skuInfo 从这两张表中查询
		List<CartInfo> cartInfoList = cartMapper.selectCartListWithCurPrice(userId);
		if(cartInfoList==null || cartInfoList.size()==0) {
			return null;
		}
		//获取redis
		Jedis jedis = redisUtil.getJedis();
		 //定义购物车的key
		String cartKey= CartConst.USER_KEY_PREFIX+userId+CartConst.USER_CART_KEY_SUFFIX;
		//cartInfoList从数据中查处的数据放入redis
//		for (CartInfo cartInfo : cartInfoList) {
//			
//			jedis.hset(cartKey, cartInfo.getSkuId(), JSON.toJSONString(cartInfoList));
//			
		
		
//		}
		HashMap<String, String> map = new HashMap();
		for (CartInfo cartInfo : cartInfoList) {
			
			
			map.put(cartInfo.getSkuId(),JSON.toJSONString(cartInfo));
		}
		
		//一次放入多条数据
		
		jedis.hmset(cartKey,map);
//		
		jedis.close();
		return cartInfoList;
	}
	@Override
	public List<CartInfo> mergeToCarList(List<CartInfo> cartListCK, String userId) {
		//根据userId获取购物车数据
		List<CartInfo> cartInfoListDB = cartMapper.selectCartListWithCurPrice(userId);
		//合并 合并条件 Skuid相同才能合并
    for (CartInfo cartInfoCk : cartListCK) {
	  //定义一个boolean变量 默认值给false
    	 boolean isMatch = false;
	for (CartInfo cartInfoDB : cartInfoListDB) {
		if(cartInfoDB.getSkuId().equals(cartInfoCk.getSkuId())) {
			//将数量进行相加
			cartInfoDB.setSkuNum(cartInfoCk.getSkuNum()+cartInfoDB.getSkuNum());
			//修改数据库
			cartMapper.updateByPrimaryKeySelective(cartInfoDB);
			isMatch = true;
		}
		
	}
	 //没有匹配上
		if(!isMatch) {
			//未登录的对象添加到数据库
			//将用户id赋值给未登陆对象
			cartInfoCk.setUserId(userId);
			cartMapper.insertSelective(cartInfoCk);
			
		}	
		}
		//最终将合并后的数据返回
		List<CartInfo> cartInfoList = loadCartCache(userId);
		
		//与未登录的合并
		
		for (CartInfo cartInfoDB : cartInfoList) {
			
			for (CartInfo cartInfoCk : cartListCK) {
				
				if(cartInfoDB.getSkuId().equals(cartInfoCk.getSkuId())) {
				
				if("1".equals(cartInfoCk.getIsChecked())) {
					//修改数据库的状态
					cartInfoDB.setIsChecked(cartInfoCk.getIsChecked());
					checkCart(cartInfoDB.getSkuId(),"1",userId);
					
				}
				}
			}
			
		}
		
		return cartInfoList;
	}
	@Override
	public void checkCart(String skuId, String isChecked, String userId) {
		/**
		 * 1获取jedis客户端
		 * 2获取购物车集合
		 * 3直接修改skuId商品勾选状态，isChecked
		 * 4写回购物车
		 * 5新建购物车来存储勾选的商品
		 */
		
		Jedis jedis = redisUtil.getJedis();
		String cartKey= CartConst.USER_KEY_PREFIX+userId+CartConst.USER_CART_KEY_SUFFIX;
		//为什么用hget，根据skuId 因为就返回一个对象
		String cartInfoJson = jedis.hget(cartKey,skuId);
		CartInfo cartInfo = JSON.parseObject(cartInfoJson, CartInfo.class);
		cartInfo.setIsChecked(isChecked);
		//写会购物车
		String jsonString = JSON.toJSONString(cartInfo);
		jedis.hset(cartKey, skuId, jsonString);
		
		//新建购物车的key user:userId:checked
		String cartKeyCkecked =CartConst.USER_KEY_PREFIX+userId+CartConst.USER_CHECKED_KEY_SUFFIX;
		
		if("1".equals(isChecked)) {
			jedis.hset(cartKeyCkecked, skuId, jsonString);
		}else {
			
			//删除被勾选的商品
			jedis.hdel(cartKeyCkecked, skuId);
		}
		
		jedis.close();
		
	}
	@Override
	public List<CartInfo> getCartCheckedList(String userId) {
		
		List<CartInfo> cartInfoList = new ArrayList<CartInfo>();
		/**
		 * 1获取jedis
		 * 2定义key
		 * 3返回数据
		 */
		//1获取jedis
		Jedis jedis = redisUtil.getJedis();
		//2定义key
		String cartKeyCkecked =CartConst.USER_KEY_PREFIX+userId+CartConst.USER_CHECKED_KEY_SUFFIX;
		//3返回数据
		//获取所有的数据 ：jedis.hvals
		List<String> stringList = jedis.hvals(cartKeyCkecked);
		if( stringList!=null &&  stringList.size()>0) {
		for (String cartJson : stringList) {
			
			cartInfoList.add(JSON.parseObject(cartJson,CartInfo.class));
			
			
		}
		}
		
		jedis.close();
		return cartInfoList;
	}

}
