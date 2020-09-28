package com.rc.gmall2020.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Data;

@Data
public class SpuImage implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column
	
	private String id;
	
	@Column
	
	private String spuId;
	
	@Column
	private String imgName;
	
	@Column
	private String imgUrl;

}
