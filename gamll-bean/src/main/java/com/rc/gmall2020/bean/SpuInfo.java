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
public class SpuInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
  @Column
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
  @Column
	private  String  spuName;
  @Column
	private String description;
  @Column
	private String catalog3Id;
  //销售属性集合
  @Transient
  private List<SpuSaleAttr> spuSaleAttrList;
  //图片集合
  @Transient
  
  private List<SpuImage>spuImageList;
	
}
