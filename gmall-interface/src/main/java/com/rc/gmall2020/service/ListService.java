package com.rc.gmall2020.service;

import com.rc.gmall2020.bean.SkuLsInfo;
import com.rc.gmall2020.bean.SkuLsResult;
import com.rc.gmall2020.bean.SkuParams;

public interface ListService {
	/**
	 * 保存数据到es中
	 * @param skuLsInfo
	 */
	
	void saveSkuLsInfo(SkuLsInfo skuLsInfo);
	/**
	 * 检索数据
	 * @param skuParams
	 * @return
	 */
	SkuLsResult search(SkuParams skuParams);
	
	
	/**
	 * 记录每个商品被访问的次数
	 * @param skuId
	 */
	
	void incrHotScore(String skuId);

}
