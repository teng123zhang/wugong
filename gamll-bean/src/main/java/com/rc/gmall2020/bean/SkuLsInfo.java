package com.rc.gmall2020.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class SkuLsInfo implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//不加注解是因为不是数据库的表
	String id;
	BigDecimal price;
	String skuName;
	String catalog3Id;
	String skuDefaultImg;
	//保存热度评分
	Long hotScore=0L;
	List<SkuLsAttrValue> skuAttrValueList;
	
	

}
