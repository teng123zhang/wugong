package com.guli.teacher.controller;


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

import com.guli.common.result.Result;
import com.guli.teacher.entity.EduVideo;
import com.guli.teacher.service.EduVideoService;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-15
 */
@RestController
@RequestMapping("/video")
@CrossOrigin
public class EduVideoController {
	/**
	 * 1保存
	 */
	@Autowired
	private EduVideoService eduVideoService;
	@PostMapping("save")
	
	public Result save(@RequestBody EduVideo video) {
		boolean flag = eduVideoService.save(video);
		if(flag) {
			return  Result.ok();
		}
		return Result.error();
		
	}
	
	/**
	 * 2根据Id查询video的回显
	 */
	
	@GetMapping("{id}")
	
	public Result getVideoById(@PathVariable String id) {
		EduVideo video = eduVideoService.getById(id);
		return Result.ok().data("video", video);
		
		
	}
	
	/**
	 * 3修改
	 */
	
	@PutMapping("update")
	
	public Result updateVideo(@RequestBody EduVideo video) {
		boolean flag = eduVideoService.updateById(video);
		if(flag) {
			return Result.ok();
		}else {
			
			return Result.error();
		}
		
		
	}
	
	/**
	 * 4删除
	 */
	
	@DeleteMapping("{id}")
	
	public Result deleteVideoById(@PathVariable  String id) {
		boolean flag = eduVideoService.removeVideoById(id);
		if(flag) {
			
			return Result.ok();
		}
		return Result.error();
		
	}


}

