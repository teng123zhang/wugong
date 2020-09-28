package com.guli.teacher.entity.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class OneChapter {
	
	private String id;
	private String title;
	List<TwoVideo> children= new ArrayList<TwoVideo>();
	 

}
