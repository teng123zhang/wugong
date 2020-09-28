package com.rc.gmall2020.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Data;
@Data
public class SkuImage implements Serializable{
	
	@Id
	
	@Column
	
	private String id;
	
	@Column
	
	private String skuId;
	@Column
	
	private String imgName;
	
	@Column
	
	private String imgUrl;
	
	@Column
	
	private String spuImgId;
	
	@Column
	
	private String isDefault;
	

}
