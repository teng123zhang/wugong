package com.rc.guli.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;

@TableName(value ="user")
public class User {
	
	//mp默认id生成策略是Long类型的 idWorker，全局唯一的
	@TableId(type= IdType.ID_WORKER)
	private Long id;
	
	private String name;
	
	private Integer age;
	@TableField(fill=FieldFill.INSERT)//指定属性在添加操作的时候进行自动补充
	private Date createTime;
	@TableField(fill=FieldFill.INSERT_UPDATE)//指定属性在添加和修改操作的时候自动补充
	private Date updateTime;
	//用户乐观锁修改版本号f的
	@Version
	@TableField(fill=FieldFill.INSERT)
	private Integer version;
	@TableLogic
	@TableField(fill=FieldFill.INSERT)
	private Integer deleted;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", version=" + version + ", deleted=" + deleted + "]";
	}
	
	
	
	

}
