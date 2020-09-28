package com.guli.teacher.controller;


import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.xmlbeans.impl.jam.mutable.MElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.guli.common.result.Result;
import com.guli.teacher.entity.EduSubject;
import com.guli.teacher.entity.vo.OneSubject;
import com.guli.teacher.service.EduSubjectService;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-12
 */
@RestController
@RequestMapping("/subject")
@CrossOrigin
public class EduSubjectController {
	
	@Autowired
	
	private EduSubjectService subjectService;
	
	
	@PostMapping("import")
	
	public Result importSubject(MultipartFile file) {
		//因为考虑到模板中的数据不准确，返回多个错误信息，那么多个错误信息放在集合中
		List<String> mesList=subjectService.importEXCL(file);
		if(mesList.size() ==0) {
		  return Result.ok();	
		}
		return Result.error().data("messageList", mesList);
		
		
	}
	/**
	 * 获取课程分类的树
	 * @return
	 */
	@GetMapping("tree")
	
	public Result TreeSubject() {
		List<OneSubject> subjectList=subjectService.getTree();
		
		return Result.ok().data("subjectList", subjectList);
	}
	
	@DeleteMapping("/{id}")
	
	public Result deleteById(@PathVariable String id) {
		boolean b = subjectService.deleteById(id);
		if(b) {
			return Result.ok();
		}
		return Result.error();
		
		
		
	}
	
	@PostMapping("saveLevelOne")
	
	public Result saveLevelOne(@RequestBody EduSubject subject) {
	Boolean flag	=subjectService.saveLevelOne(subject);
	if(flag) {
	return Result.ok();
	}
		return Result.error();
		
		
	}
	
	@PostMapping("saveLevelTwo")
	
	public Result saveLevelTwo(@RequestBody EduSubject subject) {
	Boolean flag=	this.subjectService.saveLevelTwo(subject);
	if(flag) {
		return Result.ok();
	}
		return Result.error();
		
	
	}
	

}

