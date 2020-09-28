package com.rc.gmall2020.payment;

import javax.jms.Connection;

import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import com.rc.gmall2020.util.config.ActiveMQUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallPaymentApplicationTests {
	
	@Autowired
	
	private ActiveMQUtil activeMQUtil;

	
	
	@Test
	public void  testMq() throws Exception {
		
		Connection createConnection = activeMQUtil.getConnection();
		
		
		createConnection.start();
		//第一个参数c表示是否开启事务
		//第二个参数表示开始事务/关闭事务的配置参数
		
		 //4创建session
		//<1>第一种方式不开起事务
		//Session createSession = createConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//<2>第2种方式开起事务
		Session createSession = createConnection.createSession(true, Session.SESSION_TRANSACTED);//必须提交
		//5创建队列
		Queue dfrc = createSession.createQueue("dfrc-tools");
		 //6创建消息提供者
		MessageProducer createProducer = createSession.createProducer(dfrc);
		 //7创建消息对象
		ActiveMQTextMessage activeMQTextMessage = new ActiveMQTextMessage();
		// 8发送消息
		activeMQTextMessage.setText("睡醒了继续干");
		createProducer .send(activeMQTextMessage);
		createSession.commit();
		 //9关闭
		createProducer.close();
		createSession.close();
		createConnection.close();
		
		
	}

}
