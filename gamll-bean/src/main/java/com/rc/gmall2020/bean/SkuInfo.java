package com.rc.gmall2020.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

@Data
public class SkuInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column
	
	private String id;
	@Column
	private String spuId;
	
	@Column
	private BigDecimal price;
	
	@Column
	
	private String skuName;
	@Column
	
	private BigDecimal weight;
	
	@Column
	
	private String skuDesc;
	
	@Column
	
	private String catalog3Id;
	
	@Column
	
	private String skuDefaultImg;
	
	@Transient
	
	List<SkuImage> skuImageList;
	
	@Transient
	
	List<SkuAttrValue> skuAttrValueList;
	
	@Transient
	
	List<SkuSaleAttrValue> skuSaleAttrValueList;
	
	
	
	
	
	
	

}
