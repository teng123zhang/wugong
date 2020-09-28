package com.guli.teacher.service;

import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.query.CourseQuery;
import com.guli.teacher.entity.vo.CoursePublishVo;
import com.guli.teacher.entity.vo.CourseVo;
import com.guli.teacher.entity.vo.CourseWebVo;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-14
 */
public interface EduCourseService extends IService<EduCourse> {
/**
 * 保存课程基本信息
 * @param vo
 * @return
 */
	String saveVo(CourseVo vo);
	/**
	 * 根据课程Id查询课程信息
	 * @param id
	 * @return
	 */

CourseVo getCourseVoById(String id);
/**
 * 修改课程基本信息
 * @param vo
 * @return
 */
	Boolean updateVo(CourseVo vo);
	/**
	 * 根据搜索条件分页查询
	 * @param eduCourse
	 * @param courseQuery
	 */
void getpageList(Page<EduCourse> eduCourse, CourseQuery courseQuery);
/**
 * 根据课程id删除课程信息
 * @param id
 * @return
 */
	boolean deleteById(String id);
	/**
	 * 根据课程ID查询发布课程的详情
	 * @param id
	 * @return
	 */
CoursePublishVo getCoursePublishVoById(String id);
/**
 * 根据课程ID修改状态
 * @param id
 * @return
 */
	Boolean updateStatusById(String id);
	/**
	 * 根据课程id查询课程对象
	 * @param id
	 * @return
	 */
Map<String, Object> getMapById(String id);
/**
 * 根据讲师ID查询课程信息
 * @param id
 * @return
 */
	List<EduCourse> getCourceListTeacherId(String id);
	/**
	 * 分页查询
	 * @param pageCouse
	 * @return
	 */
Map<String, Object> getCourseFrontList(Page<EduCourse> pageCouse);
//根据课程id查询课程信息
	CourseWebVo getCourseInfoFrontId(String id);
	CourseWebVo getBaseCourseInfo(String id);


}
