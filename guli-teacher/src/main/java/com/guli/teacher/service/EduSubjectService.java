package com.guli.teacher.service;

import com.guli.teacher.entity.EduSubject;
import com.guli.teacher.entity.vo.OneSubject;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-12
 */
public interface EduSubjectService extends IService<EduSubject> {
     /**
      * 根据传递的EXCL表格传递模板导入的课程分类
      * @param file
      * @return  错误的集合信息
      */
	List<String> importEXCL(MultipartFile file);
/**
 * 获取树状分类列表
 * @return
 */
	List<OneSubject> getTree();
	/**
	 * 根据ID删除课程分类
	 * @param id
	 * @return
	 */
     boolean deleteById(String id);
     /**
      * 添加课程一级分类
      * @param subject
      * @return ture false
      */
	Boolean saveLevelOne(EduSubject subject);
	/**
	 * 保存二级分类
	 * @param subject
	 * @return
	 */
	Boolean saveLevelTwo(EduSubject subject);

}
