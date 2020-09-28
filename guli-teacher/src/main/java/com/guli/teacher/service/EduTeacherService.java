package com.guli.teacher.service;

import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.entity.query.TeacherQuery;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-01
 */


public interface EduTeacherService extends IService<EduTeacher> {

	void pageQuery(Page<EduTeacher> teacherPage, TeacherQuery query);

	Map<String, Object> getTeacherAllFront(Page<EduTeacher> pageTeacher);

	
	

}
