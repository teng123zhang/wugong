package com.guli.teacher.mapper;

import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.vo.CoursePublishVo;
import com.guli.teacher.entity.vo.CourseVo;
import com.guli.teacher.entity.vo.CourseWebVo;

import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-06-14
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

	CoursePublishVo getCoursePublishVoById(String id);

	Map<String, Object> getMapById(String id);

	CourseWebVo getFrontCourseInfoId(String id);

	CourseWebVo getBaseCourseInfo(String courseId);

}
