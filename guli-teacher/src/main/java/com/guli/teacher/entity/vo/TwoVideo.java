package com.guli.teacher.entity.vo;

import org.omg.CORBA.PRIVATE_MEMBER;

import lombok.Data;

@Data
public class TwoVideo {
	
	private String id;
	
	private String title;
	private String videoSourceId;
	private String videoName;
	private Boolean isFree;

}
