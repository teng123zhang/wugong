package com.guli.teacher.controller;


import java.util.List;

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
import com.guli.teacher.entity.EduChapter;
import com.guli.teacher.entity.vo.OneChapter;
import com.guli.teacher.service.EduChapterService;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-14
 */
@RestController
@RequestMapping("/chapter")
@CrossOrigin
public class EduChapterController {
	@Autowired
	private EduChapterService  eduChapterService;
	/**
	 * 根据课程的Id获取章节的和小节列表
	 * Tree
	 * 1创建一个对象，作为章节Vo,里面包括二级集合
	 * 2创建二级的Vo(video)
	 * 3根据课程ID查询章节的列表，遍历列表，根据我们每一个章节的id查询二级列表(video)
	 */
	
	@GetMapping("{courseId}")
	public Result getChapterAndVideo(@PathVariable String courseId) {
	List<OneChapter>	list=eduChapterService.getChapterAndVideo(courseId);
	
		return Result.ok().data("list", list);
		
		
	}
	
	@PostMapping("save")
	
	public Result save(@RequestBody EduChapter chapter) {
		boolean save = eduChapterService.saveChapter(chapter);
		if(save) {
			return Result.ok();
		}else {
			
			return Result.error();
		}
		
	}
	
	
	@GetMapping("get/{id}")
	
	public Result getChapterById(@PathVariable String id) {
		EduChapter chapter = eduChapterService.getById(id);
		return Result.ok().data("chapter", chapter);
		
	}
	
	@PutMapping("update")
	
	public Result updateById(@RequestBody EduChapter chapter) {
		
			boolean flag = eduChapterService.updateChapterById(chapter);
			if(flag) {
				
				return Result.ok();
			}else {
				
				return Result.error();
			}
		
			
			
		}
	
	@DeleteMapping("{id}")
	
	public Result deleteChapterById(@PathVariable String id) {
		
	Boolean flag=	eduChapterService.removeChapterById(id);
	
	if(flag) {
		
		return Result.ok();
	}else {
		
		return Result.error();
	}
	}
		
		
	}



