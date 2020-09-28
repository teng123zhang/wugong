package com.rc.gmall2020.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Data;

//销售属性
@Data
public class SkuSaleAttrValue implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	@Id
	@Column
	
	private String id ;
	
	@Column
	
	private String skuId;
	
	@Column
	
	private String saleAttrId;
	
	@Column
	
	private String saleAttrValueId;
	
	@Column
	
	private String saleAttrName;
	
	@Column
	
	private String saleAttrValueName;

}
