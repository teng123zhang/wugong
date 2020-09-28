package com.rc.gmall2020.payment.mq;

import javax.jms.Connection;

import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;



public class ProducerTest {
	
	public static  void main(String[] args) throws Exception {
		
		/**
		 * 1创建连接工厂
		 * 2创建连接
		 * 3打开连接
		 * 4创建session
		 * 5创建队列
		 * 6创建消息提供者
		 * 7创建消息对象
		 * 8发送消息
		 * 9关闭
		 */
		
		//1创建连接工厂
		ActiveMQConnectionFactory activeMQ = new ActiveMQConnectionFactory("tcp://192.168.134.130:61616");
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
		 //6创建消息提供者
		MessageProducer createProducer = createSession.createProducer(dfrc);
		 //7创建消息对象
		ActiveMQTextMessage activeMQTextMessage = new ActiveMQTextMessage();
		// 8发送消息
		activeMQTextMessage.setText("困死了！特别想睡觉");
		createProducer .send(activeMQTextMessage);
		createSession.commit();
		 //9关闭
		createProducer.close();
		createSession.close();
		createConnection.close();
		
		
	}

}
