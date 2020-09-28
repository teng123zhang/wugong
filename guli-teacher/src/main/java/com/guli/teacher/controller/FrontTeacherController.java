package com.guli.teacher.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.result.Result;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.service.EduCourseService;
import com.guli.teacher.service.EduTeacherService;

@RestController
@RequestMapping("/front")
@CrossOrigin
public class FrontTeacherController {
	@Autowired
	private EduTeacherService eduTeacherService;
	
	@Autowired
	private EduCourseService  eduCourseService;
	
	@GetMapping("/findAllTeacherFront/{page}/{limit}")
	
	public Result getAllTeacherFront(@PathVariable Integer page
			                        ,@PathVariable Integer limit) {
		
		Page<EduTeacher> pageTeacher = new Page(page,limit);
	    Map<String, Object> map= eduTeacherService.getTeacherAllFront(pageTeacher);
		
		
		return Result.ok().data(map);
	}
	
	
	//根据讲师ID 查询讲师的详情信息
	
		@GetMapping("getFrontTeacherInfo/{id}")
		
		public Result getFrontTeacherInfo(@PathVariable String id) {
			
			EduTeacher teacherInfo = eduTeacherService.getById(id);
			
			List<EduCourse> courseList=eduCourseService.getCourceListTeacherId(id);
			return Result.ok().data("teacherInfo",teacherInfo).data("courseList", courseList);
			
			
			
		}

}
