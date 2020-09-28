package com.guli.teacher.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.result.Result;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.query.CourseQuery;
import com.guli.teacher.entity.vo.CoursePublishVo;
import com.guli.teacher.entity.vo.CourseVo;
import com.guli.teacher.service.EduCourseService;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-14
 */
@RestController
@RequestMapping("/course")
@CrossOrigin
public class EduCourseController {
	/**
	 * 保存基本信息数据
	 */
	
	@Autowired 
	private EduCourseService  courseService;
	
	
	
	@PostMapping("saveVo")
	
	public Result save(@RequestBody CourseVo vo ) {
		String courseId=courseService.saveVo(vo);
		
		
		return Result.ok().data("id",courseId);
		
	}
	/**
	 * 根据课程ID获取课程基本信息和描述
	 */
	@GetMapping("{id}")
	
	public Result getCourseVoById(@PathVariable String id) {
		CourseVo vo=courseService.getCourseVoById(id);
		return Result.ok().data("courseInfo", vo);
		
	}
	
	/**
	 * 修改课程基本信息
	 */
	@PutMapping("updateVo")
	
	public Result updateVo(@RequestBody CourseVo vo) {
		Boolean flag=courseService.updateVo(vo);
		if(flag) {
			return Result.ok();
		}
		return Result.error();
		
	
	}
	
	/**
	 * 根据条件分页查询
	 * 
	 */
	
	
	@PostMapping("{page}/{limit}")
	public Result getpageList(@PathVariable Integer page,
			                  @PathVariable Integer limit,
		                      @RequestBody CourseQuery courseQuery) {
		Page<EduCourse> eduCourse = new Page<EduCourse>(page,limit);
		courseService.getpageList(eduCourse,courseQuery);
		
	return Result.ok().data("rows", eduCourse.getRecords()).data("total",eduCourse.getTotal());
		
	}
	
	/**
	 * 根据课程ID删除
	 */
	@DeleteMapping("{id}")
	
	public Result deleteById(@PathVariable String id) {
		boolean  flag = courseService.deleteById(id);
		if(flag) {
			return Result.ok();
		}
		return Result.error();
		
	}
	
	
	/**
	 * 根据课程ID查询发布课程的详情
	 */
	
	@GetMapping("vo/{id}")
	
	public Result getCoursePublishVoById(@PathVariable String id) {
		//CoursePublishVo vo=courseService.getCoursePublishVoById(id);
		//return Result.ok().data("coursePublishVo", vo);
		Map<String, Object> map = courseService.getMapById(id);
		return Result.ok().data(map);
	}
	
	@GetMapping("updateStatusById/{id}")
	
	public Result updateStatusById(@PathVariable String id) {
	Boolean flag=	courseService.updateStatusById(id);
	if(flag) {
		return Result.ok();
	}
		return Result.error();
		
		
	}
	

}

