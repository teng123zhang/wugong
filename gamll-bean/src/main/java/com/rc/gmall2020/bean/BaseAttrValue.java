package com.rc.gmall2020.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;
@Data
public class BaseAttrValue implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column
	private  String id;
	@Column
	private String valueName;
	
	
	@Column
	
	private String attrId;
	
	//声明一个变量记录urlParam的变化
	
	@Transient
	private String urlParam;
	

}
