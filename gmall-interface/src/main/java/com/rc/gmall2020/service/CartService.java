package com.rc.gmall2020.service;

import java.util.List;

import com.rc.gmall2020.bean.CartInfo;

public interface CartService  {
	//写方法？ skuNum skuId userId
	
	void addToCart(String skuId,String userId,Integer skuNum);
/**
 * 根据用户ID查询购物车
 * @param userId
 * @return
 */
	List<CartInfo> getCartList(String userId);
	/**
	 * 合并购物车
	 * @param cartListCK
	 * @param userId
	 * @return
	 */
List<CartInfo> mergeToCarList(List<CartInfo> cartListCK, String userId);
/**
 * 修改商品状态
 * @param skuId
 * @param isChecked
 * @param userId
 */
	void checkCart(String skuId, String isChecked, String userId);
	/**
	 * 根据用户Id查询购物车列表
	 * @param userId
	 * @return
	 */
List<CartInfo> getCartCheckedList(String userId);

/**
 * 
 * @param userId
 * @return
 */
List<CartInfo> loadCartCache(String userId);
	
	
	

}
