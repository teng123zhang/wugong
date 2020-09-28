package com.rc.gmall2020.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Data;

@Data
public class UserAddress implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	@Id
	private String id;
	
	@Column
	
	private String userAddress;
	@Column
	
	private String userId;
	
	@Column
	
	private String consignee;
	
	@Column
	
	private String  phoneNum;
	
	@Column
	private String isDefault;
	 
	
	
	

}
