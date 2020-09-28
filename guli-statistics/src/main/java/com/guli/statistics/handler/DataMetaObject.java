package com.guli.statistics.handler;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

@Component
public class DataMetaObject implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		
		//在自动补充teacher对象属性中的数据，isdeleted，Boolean所以我们应该放入true，false；
		this.setFieldValByName("isDeleted",false,metaObject);
		this.setFieldValByName("gmtCreate", new Date(), metaObject);
		this.setFieldValByName("gmtModified", new Date(), metaObject);
		
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		this.setFieldValByName("gmtModified", new Date(), metaObject);
		
	}
	
	

}
