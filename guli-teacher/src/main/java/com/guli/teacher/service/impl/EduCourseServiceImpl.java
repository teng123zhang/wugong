package com.guli.teacher.service.impl;

import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.EduCourseDescription;
import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.entity.query.CourseQuery;
import com.guli.teacher.entity.vo.CoursePublishVo;
import com.guli.teacher.entity.vo.CourseVo;
import com.guli.teacher.entity.vo.CourseWebVo;
import com.guli.teacher.mapper.EduCourseMapper;
import com.guli.teacher.service.EduChapterService;
import com.guli.teacher.service.EduCourseDescriptionService;
import com.guli.teacher.service.EduCourseService;
import com.guli.teacher.service.EduVideoService;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-14
 */
@Service
@Transactional
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

	@Autowired
	private EduVideoService videoService;
	@Autowired
	private EduChapterService chapterService;
	@Autowired EduCourseDescriptionService courseDescriptionService;
	@Override
	public String saveVo(CourseVo vo) {
		//1.添加课程
		baseMapper.insert(vo.getEduCourse());
		
		//2获取课程的ID
		String courseid = vo.getEduCourse().getId();
		
		//添加课程描述
		vo.getCourseDescription().setId(courseid);
		 courseDescriptionService.save(vo.getCourseDescription());
		return courseid;
	}
	@Override
	public CourseVo getCourseVoById(String id) {
		//创建一个vo对象
		CourseVo vo = new CourseVo();
		//根据课程Id获取课程对象Educourse
		EduCourse  eduCourse = baseMapper.selectById(id);
		if(eduCourse==null) {
			return vo;
		}
		//把课程加到vo对象里
		vo.setEduCourse(eduCourse);
		//根据课程ID获取描述
		EduCourseDescription eduCourseDescription = courseDescriptionService.getById(id);
		//把课程描述放到vo里，返回vo
		if(eduCourseDescription==null) {
			return vo;
		}
		vo.setCourseDescription(eduCourseDescription);
		return vo;
	}
	@Override
	public Boolean updateVo(CourseVo vo) {
		//先判断ID是否存在,如果不存在直接返回false
		if(StringUtils.isEmpty(vo.getEduCourse().getId())) {
			return false;
		}
		//2修改Course
		int i = baseMapper.updateById(vo.getEduCourse());
		if(i<=0) {
			return false;
		}
		//3修改描述
		vo.getCourseDescription().setId(vo.getEduCourse().getId());
		boolean b = courseDescriptionService.updateById(vo.getCourseDescription());
		return b;
	}
	@Override
	public void getpageList(Page<EduCourse> eduCourse, CourseQuery courseQuery) {
		QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<EduCourse>();
		if(courseQuery==null) {
			
			baseMapper.selectPage(eduCourse, queryWrapper);
		}
		
		 String subjectId = courseQuery.getSubjectId();
		 String subjectParentId = courseQuery.getSubjectParentId();
		 String teacherId = courseQuery.getTeacherId();
		 String title = courseQuery.getTitle();
		 
		 if(!StringUtils.isEmpty(subjectId)) {
			 queryWrapper.eq("subject_id", subjectId);
			 
		 }
		 
		 
		 if(!StringUtils.isEmpty(subjectParentId)) {
			 queryWrapper.eq("subject_parent_id", subjectParentId);
			 
		 }
		 
		 
		 if(!StringUtils.isEmpty(teacherId)) {
			 queryWrapper.eq("teacher_id", teacherId);
			 
		 }
		 
		 if(!StringUtils.isEmpty(title)) {
			 queryWrapper.like("title", title);
			 
		 }
		 
		 baseMapper.selectPage(eduCourse, queryWrapper);
		
		
	}
	
	
	@Override
	public boolean deleteById(String id) {
		//删除课程相关的小节
		//根据课程ID删除小节
		videoService.removeVideoByCourseId(id);
		//删除课程相关的章节
		chapterService.removeChapterById(id);
		//删除描述
		boolean b = courseDescriptionService.removeById(id);
		if(!b) {
			
			return false;
		}
		//删除基本信息
		int i = baseMapper.deleteById(id);
		return i==1;
	}
	@Override
	public CoursePublishVo getCoursePublishVoById(String id) {
	CoursePublishVo vo=	baseMapper.getCoursePublishVoById(id);
		return vo;
	}
	@Override
	public Boolean updateStatusById(String id) {
		EduCourse eduCourse = new EduCourse();
		eduCourse.setId(id);
		eduCourse.setStatus("Normal");
		int  i = baseMapper.updateById(eduCourse);
		return i==1;
	}
	@Override
	public Map<String, Object> getMapById(String id) {
	Map<String, Object> map=	baseMapper. getMapById(id);
		return map;
	}
	@Override
	public List<EduCourse> getCourceListTeacherId(String id) {
		QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<EduCourse>();
		queryWrapper.eq("teacher_id", id);
		List<EduCourse> list = baseMapper.selectList(queryWrapper);
		return list;
	}
	@Override
	public Map<String, Object> getCourseFrontList(Page<EduCourse> pageCouse) {
		baseMapper.selectPage(pageCouse, null);
		Integer current = (int) pageCouse.getCurrent();//当前页数-
		Integer pages =(int) pageCouse.getPages();//总页数
		Integer size = (int)pageCouse.getSize();//每页显示的总记录数
		Integer total = (int)pageCouse.getTotal();//总纪录数
		List<EduCourse> records = pageCouse.getRecords();//每页数据的list集合
		boolean hasPrevious = pageCouse.hasPrevious();//上一页
		boolean hasNext = pageCouse.hasNext();//下一页
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("current",current);
		map.put("pages",pages);
		map.put("size",size);
		map.put("total",total);
		map.put("records",records);
		map.put("hasPrevious",hasPrevious);
		map.put("hasNext",hasNext);
		return map;
		
	}
	@Override
	public CourseWebVo getCourseInfoFrontId(String id) {
		CourseWebVo courseVo =	baseMapper.getFrontCourseInfoId(id);
		return courseVo;
	}
	@Override
	public CourseWebVo getBaseCourseInfo(String courseId) {
		 //根据课程id，编写sql语句查询课程信息
	   
	        return baseMapper.getBaseCourseInfo(courseId);
	   
	}

}
