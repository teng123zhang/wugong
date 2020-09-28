package com.rc.gmall2020.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.rc.gmall2020.bean.eunms.OrderStatus;
import com.rc.gmall2020.bean.eunms.PaymentWay;
import com.rc.gmall2020.bean.eunms.ProcessStatus;

import lombok.Data;

@Data
public class OrderInfo implements  Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 @Column
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private String id;

	    @Column
	    private String consignee;

	    @Column
	    private String consigneeTel;


	    @Column
	    private BigDecimal totalAmount;

	    @Column
	    private OrderStatus orderStatus;

	    @Column
	    private ProcessStatus processStatus;


	    @Column
	    private String userId;

	    @Column
	    private PaymentWay paymentWay;

	    @Column
	    private Date expireTime;

	    @Column
	    private String deliveryAddress;

	    @Column
	    private String orderComment;

	    @Column
	    private Date createTime;

	    @Column
	    private String parentOrderId;

	    @Column
	    private String trackingNo;


	    @Transient
	    private List<OrderDetail> orderDetailList;


	    @Transient
	    private String wareId;

	    @Column
	    private String outTradeNo;

	    public void sumTotalAmount(){
	        BigDecimal totalAmount=new BigDecimal("0");
	        for (OrderDetail orderDetail : orderDetailList) {
	            totalAmount= totalAmount.add(orderDetail.getOrderPrice().multiply(new BigDecimal(orderDetail.getSkuNum())));
	        }
	        this.totalAmount=  totalAmount;
	    }
	

}
