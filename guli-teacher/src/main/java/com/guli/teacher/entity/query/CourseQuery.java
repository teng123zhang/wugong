package com.guli.teacher.entity.query;

import java.io.Serializable;

import lombok.Data;

@Data
public class CourseQuery implements Serializable {
	private String subjectId;
	
	private String subjectParentId;
	 private String title;
	 
	 private String teacherId;
	
	

}
