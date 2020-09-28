package com.rc.guli.handler;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
/**
 * 在操作数据库的时候h根据语句拦截，帮我们自动补充被拦截的语句
 * @author DELL
 *
 */
@Component
public class DateMetaObjectHandler implements MetaObjectHandler {
//在执行insert语句的时候被拦截操作的
	@Override
	public void insertFill(MetaObject metaObject) {
		this.setFieldValByName("version", 1, metaObject);

		this.setFieldValByName("createTime", new Date(), metaObject);
		this.setFieldValByName("updateTime", new Date(), metaObject);
		this.setFieldValByName("deleted", 0, metaObject);

	}

	//修改语句
	@Override
	public void updateFill(MetaObject metaObject) {
		this.setFieldValByName("updateTime", new Date(), metaObject);
	}

}
