package com.rc.gmall2020.service;

import java.util.List;
import java.util.Map;

import com.rc.gmall2020.bean.OrderInfo;
import com.rc.gmall2020.bean.eunms.ProcessStatus;

public interface OrderService {
	/**
	 * 保存订单
	 * @param orderInfo
	 * @return
	 */

	String saveOrder(OrderInfo orderInfo);
	/**
	 * 生成流水号
	 * @param userId
	 * @return
	 */
	String getTradeNo(String userId);
	
	/**
	 * 
	 * @param userId 获取缓存的流水号
	 * @param tradeCodeNo 页面的流水号
	 * @return
	 */
	boolean checkTradeCode(String userId,String tradeCodeNo);
	
	/**
	 * 删除流水号
	 * 
	 * @param userId
	 */
	void delTradeCode(String userId); 
	/**
	 * 
	 * @param skuId
	 * @param skuNum
	 * @return
	 */
	
	boolean checkStock(String skuId,Integer skuNum);
	/**
	 * 根据orderId查询订单对象
	 * @param orderId
	 * @return
	 */
	OrderInfo getOrderInfo(String orderId);
	/**
	 * 更新订单状态
	 * @param orderId
	 * @param paid
	 */
	void updateOrderStatus(String orderId, ProcessStatus processStatus);
	/**
	 * 发送消息给库存
	 * @param orderId
	 */
	void sendOrderStatus(String orderId);
	/**
	 * 查询过期订单
	 * @return
	 */
	List<OrderInfo> getExpireOrderList();
	/**
	 * 处理过期订单
	 * @param orderInfo
	 */
	void exexExpiredOrder(OrderInfo orderInfo);
	
	/**
	 * 将orderInfo转换为map
	 * @param orderInfo
	 * @return
	 */
	
	Map initWareOrder(OrderInfo orderInfo);
	/**
	 * 拆单接口
	 * @param orderId
	 * @param wareSkuMap
	 * @return
	 */
	List<OrderInfo> orderSplit(String orderId, String wareSkuMap);

}
