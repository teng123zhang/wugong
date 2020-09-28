package com.rc.gmall2020.bean;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;
@Data

public class BaseAttrInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY) //获取主键自增
	private  String id;
	@Column
	private String attrName;
	
	@Column
	
	private String catalog3Id;
	
	@Transient //表示当前不是表中的字段，是业务需要的字段
	
	private List<BaseAttrValue> attrValueList;
	

}
