package com.guli.teacher.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.result.Result;
import com.guli.teacher.entity.CourseWebVoOrder;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.vo.CourseVo;
import com.guli.teacher.entity.vo.CourseWebVo;
import com.guli.teacher.entity.vo.OneChapter;
import com.guli.teacher.service.EduChapterService;
import com.guli.teacher.service.EduCourseService;

@RestController
@RequestMapping("/frontCourse")
@CrossOrigin
public class FrontCourseController {
	@Autowired
	private EduCourseService eduCourseService; 
	
	@Autowired
	private EduChapterService eduChapterService;
	//根据课程id查询课程的详情信息
	@GetMapping("getFrontCourseInfo/{id}")
	
	public Result getFrontCourseInfo(@PathVariable String id) {
		//先根据id查询课程基本信息
		CourseWebVo course=eduCourseService.getCourseInfoFrontId(id);
		//根据课程id查询课程大纲(章节和小节)
		List<OneChapter>  chapterList = eduChapterService.getChapterAndVideo(id);
		return Result.ok().data("course", course).data("chapterList", chapterList);
		
		
	}
	//课程分页列表
	@GetMapping("/getCourseFrontList/{page}/{limit}")
	
	public Result getCourseFrontList(@PathVariable Integer page,
			                         @PathVariable Integer limit) {
		
	         Page<EduCourse> pageCouse= new Page<EduCourse>(page,limit);
             Map<String, Object>	map=eduCourseService.getCourseFrontList(pageCouse);

	
	
	
									return Result.ok().data(map);
		
		
		
	}
	
	 //根据课程id查询课程信息
    @PostMapping(value = "getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id) {
        CourseWebVo courseInfo = eduCourseService.getBaseCourseInfo(id);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseInfo, courseWebVoOrder);
        return courseWebVoOrder;
    }

}
