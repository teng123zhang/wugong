package com.rc.gmall2020.manage.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;




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

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}

		public String getSkuId() {
			return skuId;
		}

		public void setSkuId(String skuId) {
			this.skuId = skuId;
		}

		public String getSkuName() {
			return skuName;
		}

		public void setSkuName(String skuName) {
			this.skuName = skuName;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public BigDecimal getOrderPrice() {
			return orderPrice;
		}

		public void setOrderPrice(BigDecimal orderPrice) {
			this.orderPrice = orderPrice;
		}

		public Integer getSkuNum() {
			return skuNum;
		}

		public void setSkuNum(Integer skuNum) {
			this.skuNum = skuNum;
		}

		public String getHasStock() {
			return hasStock;
		}

		public void setHasStock(String hasStock) {
			this.hasStock = hasStock;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}
	    
	    
	    

}
