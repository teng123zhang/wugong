package com.rc.gmall2020.manage.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;
import com.rc.gmall2020.bean.BaseAttrInfo;
import com.rc.gmall2020.bean.BaseAttrValue;
import com.rc.gmall2020.bean.BaseCatalog1;
import com.rc.gmall2020.bean.BaseCatalog2;
import com.rc.gmall2020.bean.BaseCatalog3;
import com.rc.gmall2020.bean.BaseSaleAttr;
import com.rc.gmall2020.bean.SkuAttrValue;
import com.rc.gmall2020.bean.SkuImage;
import com.rc.gmall2020.bean.SkuInfo;
import com.rc.gmall2020.bean.SkuSaleAttrValue;
import com.rc.gmall2020.bean.SpuImage;
import com.rc.gmall2020.bean.SpuInfo;
import com.rc.gmall2020.bean.SpuSaleAttr;
import com.rc.gmall2020.bean.SpuSaleAttrValue;
import com.rc.gmall2020.manage.constant.ManageConst;
import com.rc.gmall2020.manage.mapper.BaseAttrInfoMapper;
import com.rc.gmall2020.manage.mapper.BaseAttrValueMapper;
import com.rc.gmall2020.manage.mapper.BaseCatalog1Mapper;
import com.rc.gmall2020.manage.mapper.BaseCatalog2Mapper;
import com.rc.gmall2020.manage.mapper.BaseCatalog3Mapper;
import com.rc.gmall2020.manage.mapper.BaseSaleAttrMapper;
import com.rc.gmall2020.manage.mapper.SkuAttrValueMapper;
import com.rc.gmall2020.manage.mapper.SkuImageMapper;
import com.rc.gmall2020.manage.mapper.SkuInfoMapper;
import com.rc.gmall2020.manage.mapper.SkuSaleAttrValueMapper;
import com.rc.gmall2020.manage.mapper.SpuImageMapper;
import com.rc.gmall2020.manage.mapper.SpuInfoMapper;
import com.rc.gmall2020.manage.mapper.SpuSaleAttrMapper;
import com.rc.gmall2020.manage.mapper.SpuSaleAttrValueMapper;
import com.rc.gmall2020.service.ManageService;
import com.rc.gmall2020.util.config.RedisUtil;

import redis.clients.jedis.Jedis;

@Service
@Transactional
public class ManageServiceImpl implements ManageService {
	
	@Autowired 
  private BaseCatalog1Mapper  baseCatalog1Mapper;
	@Autowired 
	  private BaseCatalog2Mapper  baseCatalog2Mapper;
	@Autowired 
	  private BaseCatalog3Mapper  baseCatalog3Mapper;
	@Autowired 
	
	private BaseAttrInfoMapper baseAttrInfoMapper;
	@Autowired
	
	private BaseAttrValueMapper baseAttrValueMapper;
	
	@Autowired
	private SpuInfoMapper spuInfoMapper; 
	@Autowired
	
	private BaseSaleAttrMapper baseSaleAttrMapper;
	
	@Autowired
	
	private SpuImageMapper spuImageMapper;
	
	@Autowired
	
	private SpuSaleAttrMapper spuSaleAttrMapper;
	

	@Autowired
	
	private SpuSaleAttrValueMapper  spuSaleAttrValueMapper;
	
	@Autowired
	
	private SkuInfoMapper skuInfoMapper;
	
	@Autowired
	
	private SkuImageMapper  skuImageMapper;
	
	@Autowired
	private SkuAttrValueMapper   skuAttrValueMapper;
	
	@Autowired
	
	private SkuSaleAttrValueMapper  skuSaleAttrValueMapper;
	
	
	@Autowired
	
	private RedisUtil redisUtil;
	@Override
	public List<BaseCatalog1> getBaseCatalog1() {
		List<BaseCatalog1> selectAll = baseCatalog1Mapper.selectAll();
		return selectAll;
	}

	@Override
	public List<BaseCatalog2> getBaseCatalog2(String catalog1Id) {
		BaseCatalog2 baseCatalog2 = new  BaseCatalog2();
		baseCatalog2.setCatalog1Id(catalog1Id);
		
		return baseCatalog2Mapper.select(baseCatalog2);
	}

	@Override
	public List<BaseCatalog3> getBaseCatalog3(String catalog2Id) {
		BaseCatalog3 baseCatalog3 = new BaseCatalog3();
		baseCatalog3.setCatalog2Id(catalog2Id);
		return baseCatalog3Mapper.select(baseCatalog3);
	}

	@Override
	public List<BaseAttrInfo> getBaseAttrInfo(String catalog3Id) {
//		BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
//		 baseAttrInfo.setCatalog3Id(catalog3Id);
//		return  baseAttrInfoMapper.select(baseAttrInfo);
		
		List<BaseAttrInfo> baseAttrInfoList=	baseAttrInfoMapper.selectBaseAttrInfoListByCatalog3Id(catalog3Id);
		
		return baseAttrInfoList;
	}
  @Transactional
	@Override
	
	public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
	  
	  //修改数据
	  if(baseAttrInfo.getId()!=null||baseAttrInfo.getId().length()>0) {
		  baseAttrInfoMapper.updateByPrimaryKeySelective(baseAttrInfo);
	  }else {
		//保存数据
			baseAttrInfoMapper.insertSelective(baseAttrInfo);
			
	  }
	  
	  //baseAttrValue? 先清空在插入数据
	  //清空数据的条件 根据attrId
	  
	  //delete from BaseAttrValue where attrId=baseAttrInfo.id;
	  BaseAttrValue baseAttrValueDel = new BaseAttrValue();
	  baseAttrValueDel.setAttrId( baseAttrValueDel.getId());
	  baseAttrValueMapper.delete(baseAttrValueDel);
	  
	  
	  
		
		//1先获取baseAttrValue
		List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
		
		//判断attrValueList是否为空不为空直接添加
		
		if(attrValueList!=null&&attrValueList.size()>0) {
			
			for (BaseAttrValue baseAttrValue : attrValueList) {
				
				//1baseAttrValue.id不用添加自增
				//2baseAttrValue.name 前台穿过来
				//3baseAttrValue.attrId= baseAttrInfo.getid
				//4前提条件是baseAttrInfo对象的主键必须能够获取到自增的值!
				baseAttrValue.setAttrId(baseAttrInfo.getId());
				//保存添加成功
				baseAttrValueMapper.insertSelective(baseAttrValue);
				
				
			}
		}
		
		
	}

//@Override
//public List<BaseAttrValue> getAttrValueList(String attrId) {
//	BaseAttrValue baseAttrValue = new BaseAttrValue();
//	baseAttrValue.setAttrId(attrId);
//	return baseAttrValueMapper.select(baseAttrValue);
//	
//}

@Override
public BaseAttrInfo getAttrInfo(String attrId) {
	BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectByPrimaryKey(attrId);
	BaseAttrValue baseAttrValue = new BaseAttrValue();
	baseAttrValue.setAttrId(attrId);
	 List<BaseAttrValue> select = baseAttrValueMapper.select(baseAttrValue);
	 System.out.println(select);
	baseAttrInfo.setAttrValueList(select);
	return baseAttrInfo;
}

@Override
public List<SpuInfo> getspuList(SpuInfo spuInfo) {
	List<SpuInfo> spuInfoList = spuInfoMapper.select(spuInfo);
	return spuInfoList;
}

@Override
public List<BaseSaleAttr> getbaseSaleAttrList() {
	List<BaseSaleAttr> selectAll = baseSaleAttrMapper.selectAll();
	return selectAll;
}

@Override
public void saveSpuInfo(SpuInfo spuInfo) {
	//保存数据
	
	spuInfoMapper.insertSelective(spuInfo);
	List<SpuImage> spuImageList = spuInfo.getSpuImageList();
	if(spuImageList!=null&&spuImageList.size()>0) {
		for (SpuImage spuImage : spuImageList) {
			//设置spuId;
			spuImage.setSpuId(spuInfo.getId());
			spuImageMapper.insertSelective(spuImage);
			
		}
	}
	
	List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
	
	if(spuSaleAttrList!=null&&spuSaleAttrList.size()>0) {
		for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
			spuSaleAttr.setSpuId(spuInfo.getId());
			spuSaleAttrMapper.insertSelective(spuSaleAttr);
			
			
			List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
			if(spuSaleAttrValueList!=null && spuSaleAttrValueList.size()>0) {
				for(SpuSaleAttrValue spuSaleAttrValue:  spuSaleAttrValueList) {
					
					spuSaleAttrValue.setSpuId(spuInfo.getId());
					spuSaleAttrValueMapper.insertSelective(spuSaleAttrValue);
				}
			}
		}
	}
	
}

@Override
public List<SpuImage> getSpuImageList(SpuImage spuImage) {
	return 	spuImageMapper.select(spuImage);
	
}

@Override
public List<SpuSaleAttr> getSpuSaleAttrList(String spuId) {
	List<SpuSaleAttr> spuSaleAttrList=spuSaleAttrMapper.selectAttrList(spuId);
	return spuSaleAttrList;
	 

}

@Override
@Transactional
public void saveSkuInfo(SkuInfo skuInfo) {
	//skuInfo
	skuInfoMapper.insertSelective(skuInfo);
	List<SkuImage> skuImageList = skuInfo.getSkuImageList();
	if(skuImageList!=null && skuImageList.size()>0) {
		
		for (SkuImage skuImage : skuImageList) {
			skuImage.setSkuId(skuInfo.getId());
			skuImageMapper.insertSelective(skuImage);
		}
	}
	
	
	List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
	
	if(skuAttrValueList !=null &&skuAttrValueList.size()>0) {
		for (SkuAttrValue skuAttrValue : skuAttrValueList) {
			skuAttrValue.setSkuId(skuInfo.getId());
			
			skuAttrValueMapper.insertSelective(skuAttrValue);
			
			
		}
		
	}
	List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
	
	if(skuSaleAttrValueList!=null && skuSaleAttrValueList.size()>0) {
		for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
			
			skuSaleAttrValue.setSkuId(skuInfo.getId());
			
			skuSaleAttrValueMapper.insertSelective(skuSaleAttrValue);
			
		}
		
		
		
	}
	
}

//public SkuInfo getSkuInfo(String skuId) {
//	Jedis jedis =null;
//	SkuInfo skuInfo=null;
//	try {
//		//获取jedis
//		jedis= redisUtil.getJedis();
//		//涉及到redis,必须使用那种数据类型才能存储数据
//		/**
//		 * redis五种数据类型的使用场景!
//		 * String: 短信验证码,存储一个变量
//		 * hash: json字符串{对象转化的字符串}
//		 *   hset(key,field,value)
//		 *   hget(key,field)
//		 * list:lpush，pop队列中使用
//		 * set:去重,能够获取交集，并集，差集 补集。。不重复
//		 * zset:评分排序
//		 * 
//		 * 
//		 * 		//jedis.exists(key){
////		
////		jedis.hget(key,id,);
////		jedis.hget(key,name);
//			//获取hash中的所有数据
//			//jedis.hgetAll();
//			//jedis.hvals(key);
//		//}
//		 */
//		
//		//获取缓存中的数据
//		//定义key:sku: skuID:info
//		
//		String skuKey= ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKUKEY_SUFFIX;
//		//判断redis中是否有key
//		if(jedis.exists(skuKey)) {
//			//取得key中的value
//			
//			 String skuJson = jedis.get(skuKey);
//			//将字符串转换为对象
//			 skuInfo = JSON.parseObject(skuJson, SkuInfo.class);
//		      return skuInfo;
//		}else {
//			//判断缓存中是否有数据,如果有，从不还村中获取，如果没有从DB获取并将数据放入缓存
//			
//			 SkuInfo skuInfoDB = getSkuInfoDB(skuId);
//			 //放redis
//			 jedis.setex(skuKey,ManageConst.SKUKEY_TIMEOUT,JSON.toJSONString(skuInfoDB));
//			 return skuInfo;
//			
//		}
//		
//		
//	} catch (Exception e) {
//		
//		e.printStackTrace();
//	}finally {
//		
//		if(jedis!=null) {
//			jedis.close();
//		}
//		
//	}
//		
//		
//		
//	//当redis服务器宕机的时候直接查DB;
//	
//	
//	return getSkuInfoDB(skuId) ;
//}
//
//	

public SkuInfo getSkuInfo(String skuId) {
	
	return getSkuInfoJedis(skuId);
}

public SkuInfo getSkuInfoRedisson(String skuId) {
	
	Config config = new Config();
	config.useSingleServer().setAddress("redis://192.168.134.130:6379");
	config.useSingleServer().setPassword("123456");
	RedissonClient redissonClient = Redisson.create(config);
	//使用redisson调用getlock
	RLock lock = redissonClient.getLock("yourlock");
	//加锁
	lock.lock(10,TimeUnit.SECONDS);
	
	
	Jedis jedis =null;
	SkuInfo skuInfo=null;
	try {
		//获取jedis
		jedis= redisUtil.getJedis();
		
		//获取缓存中的数据
		//定义key:sku: skuID:info
		
		String skuKey= ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKUKEY_SUFFIX;
		//判断redis中是否有key
		if(jedis.exists(skuKey)) {
			//取得key中的value
			
			 String skuJson = jedis.get(skuKey);
			//将字符串转换为对象
			 skuInfo = JSON.parseObject(skuJson, SkuInfo.class);
		      return skuInfo;
		}else {
			//判断缓存中是否有数据,如果有，从不还村中获取，如果没有从DB获取并将数据放入缓存
			
			 SkuInfo skuInfoDB = getSkuInfoDB(skuId);
			 //放redis
			 jedis.setex(skuKey,ManageConst.SKUKEY_TIMEOUT,JSON.toJSONString(skuInfoDB));
			 return skuInfo;
			
		}
		
		
	} catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		
		if(jedis!=null) {
			jedis.close();
		}
		//解锁
		
		lock.unlock();
		
	}
		
		
		
	//当redis服务器宕机的时候直接查DB;
	
	return getSkuInfoDB(skuId) ;
	
	

}
	
	

	/**
	 *在高并发的状态下，如何防止：
	 *缓存击穿：
	 *当redis一个key无法使用,此时会有大量用户访问数据库,造成缓存击穿：
	 *加锁：
	 *redis分布式锁：
	 *redis setnx，setex
	 *set(key value,px过期时间,nx当没有key时使用)
	 *set k1 v1 10000 nx
	 *
	 *
	 *缓存雪崩:
	 *当所有的redis的key无法使用此时会给数据库造成巨大压力：即缓存雪崩
	 *解决方案：设置没一个key的过期时间不相同
	 *
	 *
	 *缓存穿透:
	 *用户在查询数据库根本没有的数据会造成缓存穿透
	 *set(key,null)
	 *
	 */
public SkuInfo getSkuInfoJedis(String skuId) {
	SkuInfo skuInfo =null;
    Jedis jedis= null;
	try {
		jedis= redisUtil.getJedis();
		//获取key;
	String skuKey= 	ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKUKEY_SUFFIX;
	//获取数据
	String skuJson= jedis.get(skuKey);
	if(skuJson==null || skuJson.length()==0) {
		//加锁
		System.out.println("缓存中没有数据");
		//执行set命令
		
		String skulockKey = ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKULOCK_SUFFIX;
		String lockKey= jedis.set(skulockKey,"OK","NX","PX",ManageConst.SKULOCK_EXPIRE_PX);
		if("OK".equals(lockKey)) {
			//此时加锁成功
			skuInfo= getSkuInfoDB(skuId);
			String skuRedisStr = JSON.toJSONString(skuInfo);
			jedis.setex(skuKey, ManageConst.SKUKEY_TIMEOUT, skuRedisStr);
			//删除key
			jedis.del(skulockKey);
			return skuInfo;
		}else {
			
			//等待
			Thread.sleep(1000);
			//调用getSkuInfo
			
			return getSkuInfo(skuId);
		}
	}else {
		
		skuInfo = JSON.parseObject(skuJson,SkuInfo.class);
		return skuInfo;
		
	}
		
	} catch (Exception e) {
		e.printStackTrace();
	}finally {
		if(jedis!=null) {
			jedis.close();
		}
		
		
	}
	
	return getSkuInfoDB(skuId);
}
	



public SkuInfo getSkuInfoDB(String skuId) {
	SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
    skuInfo.setSkuImageList(getSkuImageBySkuId(skuId));
    SkuAttrValue skuAttrValue = new SkuAttrValue();
    skuAttrValue.setSkuId(skuId);
    List<SkuAttrValue> skuAttrValueList = skuAttrValueMapper.select(skuAttrValue);
    skuInfo.setSkuAttrValueList(skuAttrValueList);
    return skuInfo;

}

@Override
public List<SkuImage> getSkuImageBySkuId(String skuId) {
	SkuImage skuImage = new SkuImage();
	skuImage.setSkuId(skuId);
	return 	skuImageMapper.select(skuImage);
	
}

@Override
public List<SpuSaleAttr> getSpuSaleAttrLiCheckBySku(SkuInfo skuInfo) {
	return  spuSaleAttrMapper.selectSpuSaleAttrListCheckBySku(skuInfo.getId(),skuInfo.getSpuId());
	
}

@Override
public List<SkuSaleAttrValue> getSkuSaleAttrValue(String spuId) {
	List<SkuSaleAttrValue> skuSaleAttrValueList= skuSaleAttrValueMapper.selectSkuSaleAttrValueCheckBySpu(spuId);
	return skuSaleAttrValueList;
}

@Override
public List<BaseAttrInfo> getAttrList(List<String> attrValueIdList) {
	String valueIds = StringUtils.join(attrValueIdList.toArray(),",");
	System.out.println("valueIds:"+valueIds);
	return  baseAttrInfoMapper.selectAttrInfoListByIds(valueIds);
	
}

}
