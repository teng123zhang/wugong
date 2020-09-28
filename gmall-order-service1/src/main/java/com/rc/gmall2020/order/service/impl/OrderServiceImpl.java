package com.rc.gmall2020.order.service.impl;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.rc.gmall2020.bean.OrderDetail;
import com.rc.gmall2020.bean.OrderInfo;
import com.rc.gmall2020.bean.eunms.OrderStatus;
import com.rc.gmall2020.bean.eunms.ProcessStatus;
import com.rc.gmall2020.order.mapper.OrderDetailMapper;
import com.rc.gmall2020.order.mapper.OrderInfoMapper;
import com.rc.gmall2020.service.OrderService;
import com.rc.gmall2020.service.PaymentService;
import com.rc.gmall2020.util.HttpClientUtil;
import com.rc.gmall2020.util.config.ActiveMQUtil;
import com.rc.gmall2020.util.config.RedisUtil;

import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;
@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderDetailMapper  orderDetailMapper;
	
	@Autowired
	
	private OrderInfoMapper orderInfoMapper;
	
	@Autowired
	
	private RedisUtil redisUtil;
	
	@Autowired
	 
	private ActiveMQUtil  activeMQUtil;
	
	@Reference
	
	private PaymentService paymentService;

	@Override
	@Transactional 
	public String saveOrder(OrderInfo orderInfo) {
		
		//数据不完整 总金额，订单装态 第三方交易编号 创建时间 和过期时间 进程状态
		//总金额
		orderInfo.sumTotalAmount();
		
		//第三方交易编号
		String  outTradeNo= "ATGULI"+System.currentTimeMillis()+""+new Random().nextInt(1000);
		orderInfo.setOutTradeNo(outTradeNo);
		//创建时间
		
		orderInfo.setCreateTime(new Date());
		
		//过期时间
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		orderInfo.setExpireTime(calendar.getTime());
		
		//订单装态 
				orderInfo.setOrderStatus(OrderStatus.UNPAID);
		//进程状态
		orderInfo.setProcessStatus(ProcessStatus.UNPAID);
		
		
		
		//只保存了一份订单
		
		orderInfoMapper.insertSelective(orderInfo);
		
		//保存订单明细
		
		List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();
		
		if(orderDetailList!=null && orderDetailList.size()>0) {
			
			for (OrderDetail orderDetail : orderDetailList) {
				//设置OrderId
				
				orderDetail.setOrderId(orderInfo.getId());
				
				orderDetailMapper.insertSelective(orderDetail);
				
			}
		}
		
		return  orderInfo.getId();
	}

	@Override
	public String getTradeNo(String userId) {
		Jedis jedis = redisUtil.getJedis();
		//定义一个key
		String tradeNoKey = "user:"+userId+":tradeCode";
		//定义一个流水号
		String tradeNo = UUID.randomUUID().toString();
		jedis.set(tradeNoKey, tradeNo);
		jedis.close();
		
		
		return tradeNo;
	}
	@Override
	public boolean checkTradeCode(String userId, String tradeCodeNo) {
		
		Jedis jedis = redisUtil.getJedis();
		//定义一个key
		String tradeNoKey = "user:"+userId+":tradeCode";
		String tradeNo = jedis.get(tradeNoKey);
		jedis.close();
		
		return tradeCodeNo.equals(tradeNo);
	}

	@Override
	public void delTradeCode(String userId) {
		
		Jedis jedis = redisUtil.getJedis();
		//定义一个key
		String tradeNoKey = "user:"+userId+":tradeCode";
		jedis.close();
		jedis.del(tradeNoKey);
		
		
	}

	@Override
	public boolean checkStock(String skuId, Integer skuNum) {
		//调用gware-manage 库存系统 http://www.gware.com /hasStock?skuId=1022&num=2
		
		String result=HttpClientUtil.doGet("http://www.gware.com/hasStock?skuId="+skuId+"&num="+skuNum);
		
		
		
		
		return "1".equals(result);
	}

	@Override
	public OrderInfo getOrderInfo(String orderId) {
		
		OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setOrderId(orderId);
		
		orderInfo.setOrderDetailList(orderDetailMapper.select(orderDetail));
		return orderInfo;
	}

	@Override
	public void updateOrderStatus(String orderId, ProcessStatus processStatus) {
	  OrderInfo orderInfo = new OrderInfo();
	  
	  orderInfo.setId(orderId);
	  
	  orderInfo.setProcessStatus(processStatus);
	  
	  orderInfo.setOrderStatus(processStatus.getOrderStatus());
	  
		orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
	}

	@Override
	public void sendOrderStatus(String orderId) {
		//创建消息工厂
		Connection connection = activeMQUtil.getConnection();
		
		String orderInfoJson =initWareOrder(orderId);
		
		try {
			//启动gongchang 
			connection.start();
			//创建session
			Session session = connection.createSession(true,Session.SESSION_TRANSACTED);
			//创建队列
			Queue createQueue = session.createQueue("ORDER_RESULT_QUEUE");
			//创建消息提供者
			MessageProducer createProducer = session.createProducer( createQueue);
			//创建消息对象
			ActiveMQTextMessage activeMQTextMessage = new ActiveMQTextMessage();
			//orderInfo 组成的字符串
			activeMQTextMessage.setText(orderInfoJson);
			 createProducer.send(activeMQTextMessage);
			 session.commit();
			 createProducer.close();
			 session.close();
			 connection.close();
		} catch (JMSException e) {
			
			e.printStackTrace();
		}
		
		
		
		
	}
/**
 * 根据orderId将orderInfo变为json字符串
 * @param orderId
 * @return
 */
	private String initWareOrder(String orderId) {
		/**
		 * 根据orderId查询orderInfo
		 */
		
		OrderInfo orderInfo=getOrderInfo(orderId);
		//将orderInfo中游泳的数据转换为map
		Map  map =initWareOrder(orderInfo);
		//在将map转换为json返回
		return JSON.toJSONString(map);
	}
/**
 * 
 * @param orderInfo
 * @return
 */
public Map initWareOrder(OrderInfo orderInfo) {
	HashMap<String,Object> map = new HashMap<>();
	//给map的key赋值
	 map.put("orderId",orderInfo.getId());
     map.put("consignee", orderInfo.getConsignee());
     map.put("consigneeTel",orderInfo.getConsigneeTel());
     map.put("orderComment",orderInfo.getOrderComment());
     map.put("orderBody","给李云龙");
     map.put("deliveryAddress",orderInfo.getDeliveryAddress());
     map.put("paymentWay","2");
     /*getWareId仓库ID*/
     //map.put("wareId",orderInfo.getWareId());
     List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();
     //创建一个集合存储List
     ArrayList<Map> arrayList = new ArrayList<>();
     for (OrderDetail orderDetail : orderDetailList) {
    	 HashMap<String,Object> orderDetailmap = new HashMap<>();
    	 orderDetailmap.put("skuId", orderDetail.getSkuId());
    	 orderDetailmap.put("skuNum", orderDetail.getSkuNum());
    	 orderDetailmap.put("skuName", orderDetail.getSkuName());
    	 
    	 arrayList.add(orderDetailmap); 
		
	}
     
     map.put("details",  arrayList);
	return map;
}

@Override
public List<OrderInfo> getExpireOrderList() {
	//当前系统时间过期时间and当前状态是未支付
	//andLessThan  小于 xxx
	Example example = new Example(OrderInfo.class);
	example.createCriteria().andEqualTo("processStatus", ProcessStatus.UNPAID).andLessThan("expireTime", new Date());
	List<OrderInfo> orderInfoList = orderInfoMapper.selectByExample(example);
	return orderInfoList;
}

@Async
public void exexExpiredOrder(OrderInfo orderInfo) {
	//将订单状态改成关闭
	updateOrderStatus(orderInfo.getId(), ProcessStatus.CLOSED);
	//关闭paymentInfo
	paymentService.closePayment(orderInfo.getId());
}

@Override
public List<OrderInfo> orderSplit(String orderId, String wareSkuMap) {
	/**
	 * 1获取原始订单
	 * 2将wareSkuMap 转换为我们能操作的对象
	 * 3创建新的子订单
	 * 4给子订单赋值
	 * 5将子订单添加到集合中
	 * 6更新订单状态
	 */
	
	List<OrderInfo> subOrderInfoList = new  ArrayList<OrderInfo>();
	//1获取原始订单
	OrderInfo orderInfoOrigin = getOrderInfo(orderId);
	//2将wareSkuMap 转换为我们能操作的对象
	List<Map> maps = JSON.parseArray(wareSkuMap,Map.class);
	
	if(maps!=null) {
		//循环遍历集合
		for (Map map : maps) {
			//获取仓库id
			String wareId= (String)map.get("wareId");
			//获取仓库对应的商品id
			List<String> skuIds = (List<String>) map.get("skuIds");
			 //3创建新的子订单
			OrderInfo subOrderInfo = new OrderInfo();
			//给属性g赋值 属性拷贝
			BeanUtils.copyProperties(orderInfoOrigin, subOrderInfo);
			//id必须变为空、
			subOrderInfo.setId(null);
			subOrderInfo .setWareId(wareId);
			subOrderInfo.setParentOrderId(orderId);
			//声明一个新的子订单集合
			ArrayList<OrderDetail> subOrderDetailList = new ArrayList<>();
			//价格 获取到子订单的明细
			List<OrderDetail> orderDetailList = orderInfoOrigin.getOrderDetailList();
			for (OrderDetail orderDetail : orderDetailList) {
				
				for(String skuId:skuIds) {
					
					if(skuId.equals(orderDetail.getSkuId())) {
						orderDetail.setId(null);
						subOrderDetailList.add(orderDetail);
						
					}
				}
				
			}
			
			//将新的子订单放入子订单中
			subOrderInfo.setOrderDetailList(subOrderDetailList);
			
			//计算价格
			subOrderInfo.sumTotalAmount();
			//保存到数据库
			
			saveOrder(subOrderInfo);
			//将新的子订单放入集合中
			subOrderInfoList.add(subOrderInfo);
		}
	}
	updateOrderStatus(orderId, ProcessStatus.SPLIT);
	return subOrderInfoList;
}


}
