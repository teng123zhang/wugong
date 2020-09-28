package com.guli.teacher.service;

import com.guli.teacher.entity.EduChapter;
import com.guli.teacher.entity.vo.OneChapter;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-14
 */
public interface EduChapterService extends IService<EduChapter> {
/**
 * 根据课程ID查询课程章节和小节列表
 * @param id
 * @return
 */
	List<OneChapter> getChapterAndVideo(String courseId);
	/**
	 * 保存章节判断保存章节名称是否存在
	 * @param chapter
	 * @return
	 */

boolean saveChapter(EduChapter chapter);
/**
 * 修改章节
 * 修改后判断章节名称时候存在
 * @param chapter
 * @return
 */

boolean updateChapterById(EduChapter chapter);
/**
 * 根据章节ID删除章节信息
 * @param id
 * @return
 */
Boolean removeChapterById(String id);

}
