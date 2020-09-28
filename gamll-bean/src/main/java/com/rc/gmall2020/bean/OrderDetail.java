package com.rc.gmall2020.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

@Data
public class OrderDetail implements Serializable{
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	    @Column
	    private String id;
	    @Column
	    private String orderId;
	    @Column
	    private String skuId;
	    @Column
	    private String skuName;
	    @Column
	    private String imgUrl;
	    @Column
	    private BigDecimal orderPrice;
	    @Column
	    private Integer skuNum;

	    /*是否有库存*/
	    @Transient
	    private String hasStock;

}
