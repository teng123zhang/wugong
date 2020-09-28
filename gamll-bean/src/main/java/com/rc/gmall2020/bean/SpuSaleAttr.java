package com.rc.gmall2020.bean;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

@Data
public class SpuSaleAttr implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	
	@Column
	
	private String id;
	
	@Column
	
	private String spuId;
	
	@Column
	
	private  String saleAttrId;
	
	@Column
	
	private String saleAttrName;
	//销售属性值集合
	@Transient
	
	List<SpuSaleAttrValue> spuSaleAttrValueList;
	
	
	

}
