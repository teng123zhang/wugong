package com.rc.gmall2020.bean;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

@Data
public class SpuSaleAttrValue implements Serializable{/**
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
	private String saleAttrValueName;
	
	
	@Transient
	String isChecked;
	
	
	
	
	

}
