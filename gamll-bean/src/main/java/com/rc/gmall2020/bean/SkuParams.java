package com.rc.gmall2020.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class SkuParams implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//自定义用户实体类
	//keyword =SkuName;
	String keyword;
	String catalog3Id;
	String [] valueId;
	int pageNo=1;
	int pageSize=20;
	
	

}
