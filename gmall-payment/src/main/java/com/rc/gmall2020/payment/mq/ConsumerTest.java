package com.rc.gmall2020.payment.mq;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ConsumerTest {
	
	public static void main(String[] args) throws Exception {
		
		/**
		 * 1创建连接工厂
		 * 2创建连接
		 * 3打开连接
		 * 4创建session
		 * 5创建队列
		 * 6创建消息消费者
		 * 7消费消息
		
		 */
		
		
		//1创建连接工厂,默认用户名 密码
				ActiveMQConnectionFactory activeMQ = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,ActiveMQConnection.DEFAULT_PASSWORD,"tcp://192.168.134.130:61616");
				//2创建连接
				Connection createConnection = activeMQ.createConnection();
				//3打开连接
				createConnection.start();
				//第一个参数c表示是否开启事务
				//第二个参数表示开始事务/关闭事务的配置参数
				
				 //4创建session
				//<1>第一种方式不开起事务
				//Session createSession = createConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				//<2>第2种方式开起事务
				Session createSession = createConnection.createSession(true, Session.SESSION_TRANSACTED);//必须提交
				//5创建队列
				Queue dfrc = createSession.createQueue("dfrc");
				// 6创建消息消费者
				MessageConsumer createConsumer = createSession.createConsumer(dfrc);
				createConsumer.setMessageListener(new  MessageListener() {
					
					@Override
					public void onMessage(Message message) {
						//如何将消息获取到
						//instanceof 比较对象想等
						if(message instanceof TextMessage) {
							try {
								String text = ((TextMessage) message).getText();
								System.out.println("获取的消息:==>"+text);
							} catch (JMSException e) {
							
								e.printStackTrace();
							}
						}
						
						
					}
				});
		
	}

}
