package com.rc.gmall2020.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Data;

@Data
public class BaseCatalog2 implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	
	@Column
	
	private String id;
	
	@Column
	
	private String name;
	
	@Column
	
	private String catalog1Id;
	 

}
