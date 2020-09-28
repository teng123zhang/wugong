package com.guli.teacher.entity.query;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.druid.stat.TableStat.Name;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.guli.common.result.Result;
import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.service.EduTeacherService;

import ch.qos.logback.classic.Level;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

public class TeacherQuery implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@JsonProperty(value = "name") 
	private String name;
	@JsonProperty(value = "level") 
	private Integer level;
	@ApiModelProperty(value = "创建时间",example = "2020-6-1 21:55:08")
	@JsonProperty(value = "gmtCreate")
	private Date gmtCreate;
	 @ApiModelProperty(value = "更新时间",example = "2020-6-1 21:55:08")
		@JsonProperty(value = "gmtModified")
	 private Date gmtModified;
	 
	 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Date getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Date getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "TeacherQuery [name=" + name + ", level=" + level + ", gmtCreate=" + gmtCreate + ", gmtModified="
				+ gmtModified + "]";
	}
	
	

	

	
	
	

}
