package com.rc.gmall2020.cart.service.mapper;

import java.util.List;

import com.rc.gmall2020.bean.CartInfo;

import tk.mybatis.mapper.common.Mapper;

public  interface CartMapper extends Mapper<CartInfo> {
/**
 * 根据userId 查询 实时价格 到cartInfo中
 * @param userId
 * @return
 */
	List<CartInfo> selectCartListWithCurPrice(String userId);

}
