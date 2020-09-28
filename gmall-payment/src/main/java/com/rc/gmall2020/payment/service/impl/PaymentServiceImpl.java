package com.rc.gmall2020.payment.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.github.wxpay.sdk.WXPayUtil;
import com.rc.gmall2020.bean.OrderInfo;
import com.rc.gmall2020.bean.PaymentInfo;
import com.rc.gmall2020.bean.eunms.PaymentStatus;
import com.rc.gmall2020.bean.eunms.ProcessStatus;
import com.rc.gmall2020.payment.mapper.PaymentMapper;
import com.rc.gmall2020.payment.util.HttpClient;
import com.rc.gmall2020.service.OrderService;
import com.rc.gmall2020.service.PaymentService;
import com.rc.gmall2020.util.config.ActiveMQUtil;

import tk.mybatis.mapper.entity.Example;
@Service
public class PaymentServiceImpl implements PaymentService{
	
	@Autowired
	
	private PaymentMapper paymentMapper;
	
	@Autowired
	
	private ActiveMQUtil activeMQUtil;
	
	@Autowired
	
	private AlipayClient  alipayClient ;
	
	@Reference
	
	private OrderService orderService; 
	
	@Value("${appid}")
	
	private String appid;
	
	@Value("${partner}")
	
	private String partner;
	
	@Value("${partnerkey}")
	
	private String partnerkey;
	
	

	@Override
	public void savePaymentInfo(PaymentInfo paymentInfo) {
		
		paymentMapper.insertSelective(paymentInfo);	
	
	}

	@Override
	public Map createNative(String orderId, String money)  {
		/**
		 * 1 制作参数使用map
		 * 2map转换为xml 并发送支付接口
		 * 3 获取直接结果
		 * 
		 * 
		 */
		Map<String, String> map = new HashMap<>();
		map.put("appid", appid);
		map.put("mch_id",partner);
		map.put("nonce_str",WXPayUtil.generateNonceStr());
		map.put("body", "买衣服");
		map.put("out_trade_no",orderId);
		map.put("spbill_create_ip","127.0.0.1");
		map.put("total_fee",money);
		map.put("notify_url","http://www.weixin.qq.com/wxpay/pay.php");
		map.put("trade_type","NATIVE");
		try {
			//生成xml以post的请求方式  发送给支付接口
			String urlXml = WXPayUtil.generateSignedXml(map, partnerkey);
			//导入工具类项目中
			HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
			//设置https请求
			httpClient.setHttps(true);
			//设置参数
			httpClient.setXmlParam(urlXml);
			//以post请求
			httpClient.post();
			
			
			//获取结果 将结果放入map中
			HashMap<String,String> resultmap = new HashMap<>();
			
			//将结果转换为map
			 String result= httpClient.getContent();
			Map<String, String> xmlToMap = WXPayUtil.xmlToMap(result);
			resultmap.put("code_url", xmlToMap.get("code_url"));
			resultmap.put("total_fee", money);
			resultmap.put("out_trade_no", orderId);
			return resultmap;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}

	@Override
	public PaymentInfo getPaymentInfo(PaymentInfo paymentInfoQuery) {
		
		
		return paymentMapper.selectOne(paymentInfoQuery);
		
	}

	@Override
	public void updatePaymentInfo(String out_trade_no, PaymentInfo paymentInfoUpd) {
		Example example = new Example( PaymentInfo.class);
		example.createCriteria().andEqualTo("outTradeNo", out_trade_no);
		paymentMapper.updateByExampleSelective(paymentInfoUpd, example);
	}



	@Override
	public boolean refund(String orderId) {
		/*根据orderInfo获取数据*/
        OrderInfo orderInfo = orderService.getOrderInfo(orderId);
//        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","app_id","your private_key","json","GBK","alipay_public_key","RSA2");
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        HashMap<String, Object> map = new HashMap<>();
        map.put("out_trade_no",orderInfo.getOutTradeNo());
        map.put("refund_amount", orderInfo.getTotalAmount());
        map.put("refund_reason","哥不买了......");
        request.setBizContent(JSON.toJSONString(map));
        /*request.setBizContent("{" +
                "\"out_trade_no\":\"20150320010101001\"," +
                "\"trade_no\":\"2014112611001004680073956707\"," +
                "\"refund_amount\":200.12," +
                "\"refund_currency\":\"USD\"," +
                "\"refund_reason\":\"正常退款\"," +
                "\"out_request_no\":\"HZ01RF001\"," +
                "\"operator_id\":\"OP001\"," +
                "\"store_id\":\"NJ_S_001\"," +
                "\"terminal_id\":\"NJ_T_001\"," +
                "      \"goods_detail\":[{" +
                "        \"goods_id\":\"apple-01\"," +
                "\"alipay_goods_id\":\"20010001\"," +
                "\"goods_name\":\"ipad\"," +
                "\"quantity\":1," +
                "\"price\":2000," +
                "\"goods_category\":\"34543238\"," +
                "\"categories_tree\":\"124868003|126232002|126252004\"," +
                "\"body\":\"特价手机\"," +
                "\"show_url\":\"http://www.alipay.com/xxx.jpg\"" +
                "        }]," +
                "      \"refund_royalty_parameters\":[{" +
                "        \"royalty_type\":\"transfer\"," +
                "\"trans_out\":\"2088101126765726\"," +
                "\"trans_out_type\":\"userId\"," +
                "\"trans_in_type\":\"userId\"," +
                "\"trans_in\":\"2088101126708402\"," +
                "\"amount\":0.1," +
                "\"amount_percentage\":100," +
                "\"desc\":\"分账给2088101126708402\"" +
                "        }]," +
                "\"org_pid\":\"2088101117952222\"" +
                "  }");*/
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            /*更新状态 退款成功*/
            System.out.println("调用成功");
            return true;
        } else {
            /*退款失败*/
            System.out.println("调用失败");
            return false;
        }
    }

	@Override
	public void sendPaymentResult(PaymentInfo paymentInfo, String result)  {
		Connection connection = activeMQUtil.getConnection();
		Session session=null;
		try {
			//打开连接
			connection.start();
			//创建session
			session = connection.createSession(true, Session.SESSION_TRANSACTED);
			//创建队列
			Queue createQueue = session.createQueue("PAYMENT_RESULT_QUEUE");
			//创建消息提供者
			MessageProducer createProducer = session.createProducer(createQueue);
			//创建消息对象
			ActiveMQMapMessage activeMQMapMessage = new ActiveMQMapMessage();
			
			
			activeMQMapMessage.setString("orderId", paymentInfo.getOrderId());
			activeMQMapMessage.setString("result",result);
			//发送消息
			createProducer.send(activeMQMapMessage);
			//t提交
			session.commit();
			//关闭
			
			
				clossAll(createProducer,session,connection);
			
			
			
		} catch (JMSException e) {
			
			e.printStackTrace();
		}
		
		
	}
		
/**
 * 关闭消息队列
 * @param createProducer
 * @param session
 * @param connection
 * @throws JMSException 
 * @throws Exception 
 */

	private void clossAll(MessageProducer createProducer, Session session, Connection connection) throws JMSException  {
	
		createProducer.close();
		session.close();
		connection.close();
	}
//查询支付交易是否成功 ！需要根据orderId进行查询
	//http://payment.gmall.com/query?orderId=167
	@Override
	public boolean checkPayment(PaymentInfo paymentInfoQuery) {
		   AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","app_id","your private_key","json","GBK","alipay_public_key","RSA2");
	        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
	        /*判断是否支付*/
	        if (paymentInfoQuery.getPaymentStatus()== PaymentStatus.PAID || paymentInfoQuery.getPaymentStatus()==PaymentStatus.ClOSED){
	            return true;
	        }
	        HashMap<String, String> hashMap = new HashMap<>();
	        hashMap.put("out_trade_no",paymentInfoQuery.getOutTradeNo());
	        request.setBizContent(JSON.toJSONString(hashMap));
	        /*request.setBizContent("{" +
	                "\"out_trade_no\":\"20150320010101001\"," +
	                "\"trade_no\":\"2014112611001004680 073956707\"," +
	                "\"org_pid\":\"2088101117952222\"," +
	                "      \"query_options\":[" +
	                "        \"TRADE_SETTLE_INFO\"" +
	                "      ]" +
	                "  }");*/
	        AlipayTradeQueryResponse response = null;
	        try {
	            response = alipayClient.execute(request);
	        } catch (AlipayApiException e) {
	            e.printStackTrace();
	        }
	        if(response.isSuccess()){
	            System.out.println("调用成功");
	            // 改支付状态
	            PaymentInfo paymentInfoUpd = new PaymentInfo();
	            paymentInfoUpd.setPaymentStatus(PaymentStatus.PAID);
	            updatePaymentInfo(paymentInfoQuery.getOutTradeNo(),paymentInfoUpd);
	            sendPaymentResult(paymentInfoQuery,"success");
	            return true;
	        } else {
	            System.out.println("调用失败");
	        }
	        return false;
		
	}

	@Override
	public void sendDelayPaymentResult(String outTradeNo, int delaySec, int checkCount) {
	//创建工厂
		
		Connection connection = activeMQUtil.getConnection();
	
		
		try {
			//启动工厂
			connection.start();
			//创建session
			Session session = connection.createSession(true,Session.SESSION_TRANSACTED);
			//创建队列
			Queue createQueue = session.createQueue("PAYMENT_RESULT_CHECK_QUEUE");
			MessageProducer createProducer = session.createProducer(createQueue );
			//创建消息对象
			ActiveMQMapMessage activeMQMapMessage = new ActiveMQMapMessage();
			activeMQMapMessage.setString("outTradeNo", outTradeNo);
			activeMQMapMessage.setInt("delaySec", delaySec);
			activeMQMapMessage.setInt("checkCount", checkCount);
			//设置延迟队列的开启
			
			activeMQMapMessage.setProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delaySec*1000);
			createProducer.send(activeMQMapMessage);
			session.commit();
			//关闭
			createProducer.close();
			session.close();
			connection.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	
		
		
		
		
	}

	@Override
	public void closePayment(String orderId) {
		//更新状态
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setPaymentStatus(PaymentStatus.ClOSED);
		Example example = new Example(PaymentInfo.class);
		example.createCriteria().andEqualTo("orderId", orderId);
		
		
		paymentMapper.updateByExampleSelective(paymentInfo, example);
		
	}

}
