package com.rc.gmall2020.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class SkuLsResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//页面显示的数据
	List<SkuLsInfo> skuLsInfoList;
	//查出来的总条数
	Long total;
	//总页码
	Long totalPage;
	
	//平台属性值id集合,根据平台属性值id查询平台属性
	
	List<String> attrValueIdList;

}
