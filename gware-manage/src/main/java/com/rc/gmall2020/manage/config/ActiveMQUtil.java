package com.rc.gmall2020.manage.config;



import javax.jms.Connection;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;


public class ActiveMQUtil {
	
	
	 private PooledConnectionFactory poolFactory;

	    public    void init(String brokerUrl) {

	            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);

	              poolFactory = new PooledConnectionFactory(factory);
	            poolFactory.setMaxConnections(5);
	            //后台对象清理时，休眠时间超过了10000毫秒的对象为过期
	            poolFactory.setReconnectOnException(true);

	            poolFactory.setTimeBetweenExpirationCheckMillis(10000);


	    }




	    public Connection getConn() {
	        Connection connection = null;
	        try {
	            connection = poolFactory.createConnection();

	        } catch (JMSException e) {
	            e.printStackTrace();
	        }
	        return connection;
	    }



}
