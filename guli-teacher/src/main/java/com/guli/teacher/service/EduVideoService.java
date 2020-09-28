package com.guli.teacher.service;

import com.guli.teacher.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-15
 */
public interface EduVideoService extends IService<EduVideo> {
   /**
    * 根据视频ID删除视频
    * @param id
    * @return
    */
	boolean removeVideoById(String id);
	/**
	 * 根据课程Id删除小节
	 * @param courseId
	 * @return
	 */
	boolean  removeVideoByCourseId(String courseId);

}
