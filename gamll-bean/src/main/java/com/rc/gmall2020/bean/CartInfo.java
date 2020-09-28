package com.rc.gmall2020.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;
@Data
public class CartInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@Id
	@Column
	String id;
	
	@Column
	String userId;
	@Column
	String skuId;
	//数据库中 加入购物车时的价格
	@Column
	BigDecimal cartPrice;
	@Column
	Integer skuNum;
	@Column
	String imgUrl;
	@Column
	String skuName;
	
	//实时价格 商品详情中的价格 skuInfo.price
	@Transient
	BigDecimal skuPrice;
	//下单时候商品是否勾选
	
	@Transient
	
	String isChecked="0";
	

	
	
	
	
	
	

}
