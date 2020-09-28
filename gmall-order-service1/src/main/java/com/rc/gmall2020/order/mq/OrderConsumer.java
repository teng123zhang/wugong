package com.rc.gmall2020.order.mq;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.rc.gmall2020.bean.eunms.ProcessStatus;
import com.rc.gmall2020.service.OrderService;
@Component
public class OrderConsumer {
	/**
	 * destination ：表示监听的队列名
	 * 
	 * containerFactory:使用那个消息监听器工厂监听
	 * @throws JMSException 
	 */
	
	@Autowired
	
	private OrderService orderService; 
	//获取消息队列中的数据
	@JmsListener(destination = "PAYMENT_RESULT_QUEUE",containerFactory = "jmsQueueListener")
	
	public void consumerPaymentResult(MapMessage  mapMessage ) throws JMSException {
		//通过MapMessage获取
		String orderId = mapMessage.getString("orderId");
		String result = mapMessage.getString("result");
		//支付成功
		if("success".equals(result)) {
			
			//更新订单状态
			
			orderService.updateOrderStatus(orderId,ProcessStatus.PAID);
			//发送消息给库存
			orderService.sendOrderStatus(orderId);
			//更新订单状态
			
			orderService.updateOrderStatus(orderId,ProcessStatus.NOTIFIED_WARE);
			
		}
		
		
		
		
	}

	
	@JmsListener(destination = "SKU_DEDUCT_QUEUE",containerFactory = "jmsQueueListener")
		public void consumerSkuDeduct(MapMessage  mapMessage ) throws JMSException {
		
		//通过mapMessage获取
		String orderId = mapMessage.getString("orderId");
		String status =mapMessage.getString("status");
		if("DEDUCTED".equals(status)) {
			orderService.updateOrderStatus(orderId, ProcessStatus.DELEVERED);
		}
}
	

}
