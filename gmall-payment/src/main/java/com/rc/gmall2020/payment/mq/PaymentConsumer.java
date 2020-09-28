package com.rc.gmall2020.payment.mq;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rc.gmall2020.bean.PaymentInfo;
import com.rc.gmall2020.service.PaymentService;



@Component
public class PaymentConsumer {
	
	@Reference 
	private PaymentService paymentService;
	
	//消费检查是否支付成功的消息队列
		@JmsListener(destination = "PAYMENT_RESULT_CHECK_QUEUE",containerFactory = "jmsQueueListener")
		
		public void consumerSkuDeduct(MapMessage  mapMessage ) throws JMSException {
			//通过MapMessage获取
			
			String outTradeNo = mapMessage.getString("outTradeNo");
			int delaySec = mapMessage.getInt("delaySec");
			int checkCount = mapMessage.getInt("checkCount");
			PaymentInfo paymentInfo = new PaymentInfo();
			paymentInfo.setOutTradeNo(outTradeNo);
			//获取orderId
			PaymentInfo paymentInfoQuery = paymentService.getPaymentInfo(paymentInfo);
			
			//判断是否支付成功
			
			boolean  result = paymentService.checkPayment(paymentInfoQuery);
			System.out.println("检查结果："+result);
			//支付失败
			if(!result && checkCount>0) {
				//调用发送消息的方法
				System.out.println("检查次数："+checkCount);
				paymentService.sendDelayPaymentResult(outTradeNo, delaySec, checkCount-1);
				//
				
				
				
				
			}
			
			
			
	
	
		}
}
