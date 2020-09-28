package com.rc.gmall2020.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Data;

//保存平台属性
@Data
public class SkuAttrValue implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	
	@Column
	
	private String id;
	
	@Column
	
	private String attrId;
	
	@Column
	
	private String valueId;
	
	@Column
	
	private String skuId;
	
	

}
