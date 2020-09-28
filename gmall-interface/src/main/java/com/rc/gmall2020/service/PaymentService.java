package com.rc.gmall2020.service;

import java.util.Map;

import com.rc.gmall2020.bean.PaymentInfo;

public interface PaymentService {
	/**
	 * 保存交易记录
	 * @param paymentInfo
	 */
	
	void savePaymentInfo(PaymentInfo paymentInfo);
	
	/**
	 * 
	 * @param orderId
	 * @param string
	 * @return
	 */

	Map createNative(String orderId, String string);
	
	/**
	 * 根据out_trade_no查询PaymentInfo 
	 * @param paymentInfo
	 * @return
	 */

	PaymentInfo getPaymentInfo(PaymentInfo paymentInfo);
	/**
	 * 根据out_trade_no修改paymentInfoUpd
	 * @param out_trade_no
	 * @param paymentInfoUpd
	 */

	void updatePaymentInfo(String out_trade_no, PaymentInfo paymentInfoUpd);
/**
 * 发送消息f给订单
 * @param paymentInfo
 * @param string
 */
	void sendPaymentResult(PaymentInfo paymentInfo, String result);
/**
 * 退款接口
 * @param orderId
 * @return
 */
	boolean refund(String orderId);
	/**
	 * 根据out_trade_no 查询交易
	 
	  * 
	  * @param paymentInfoQuery
	  * @return
	  */
	boolean checkPayment(PaymentInfo paymentInfoQuery);
	/**
	 * 每隔15秒主动去支付宝询问该笔订单是否支付成功
	 * @param outTradeNo 第三方交易编号
	 * @param delaySec 每隔多长时间查询一次
	 * @param checkCount 查询的次数
	 */
	void sendDelayPaymentResult(String outTradeNo,int delaySec,int checkCount);
/**
 * 根据订单id、关闭交易记录的状态
 * @param orderId
 */
	void closePayment(String orderId);
	

}
