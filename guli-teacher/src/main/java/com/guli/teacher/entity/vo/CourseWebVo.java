package com.guli.teacher.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CourseWebVo {
	private String id;
	private String title;
	private String cover;
	private BigDecimal price;
	private String teacherId;
	private String teacherName;
	private String avatar;
	private String career;
	private String intro;
	 private String description;
	private String  subjectLevelOneId;
	private String subjectLevelTwoId;
	private String  subjectLevelOne;
	private String subjectLevelTwo;
	private Long buyCount;
	 private Long viewCount;
	 private Integer lessonNum;
	
	

}
