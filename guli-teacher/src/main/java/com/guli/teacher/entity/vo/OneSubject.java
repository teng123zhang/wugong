package com.guli.teacher.entity.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class OneSubject {
	
	private String id;
	private String title;
	List<TwoSubject> children= new ArrayList<TwoSubject>();
	

}
