package com.guli.teacher.service.impl;

import com.guli.teacher.entity.EduChapter;
import com.guli.teacher.entity.EduVideo;
import com.guli.teacher.entity.vo.OneChapter;
import com.guli.teacher.entity.vo.TwoVideo;
import com.guli.teacher.exception.EduException;
import com.guli.teacher.mapper.EduChapterMapper;
import com.guli.teacher.service.EduChapterService;
import com.guli.teacher.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-14
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
@Autowired
EduVideoService eduVideoService;
	@Override
	public List<OneChapter> getChapterAndVideo(String courseId) {
		List<OneChapter> list= new ArrayList<OneChapter>();
		//根据ID查询章节列表
		QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<EduChapter>();
		queryWrapper.eq("course_id", courseId);
		queryWrapper.orderByAsc("sort");
		 List<EduChapter> eduChapter = baseMapper.selectList(queryWrapper);
		//遍历章节列表
		 for(EduChapter eduChapters:eduChapter) {
			//把每一个对象复制到OneChapter
			 OneChapter oneChapter = new OneChapter();
			 BeanUtils.copyProperties(eduChapters, oneChapter);
			 
			//根据每一个章节ID查询小节列表
			 QueryWrapper<EduVideo> wr = new QueryWrapper<EduVideo>();
			 wr.eq("chapter_id", oneChapter.getId());
			 wr.orderByAsc("sort");
			 List<EduVideo> videoList = eduVideoService.list(wr);
			//遍历每一个小节
			 for(EduVideo video:videoList) {
				 TwoVideo twoVideo = new TwoVideo();
				//把每一个小节对象赋值到TwoVideo
				 BeanUtils.copyProperties(video,twoVideo);
				//把每一个TwoVideo加到章节children
				 oneChapter.getChildren().add(twoVideo);
				//把每一个章节加到总集合中
			 }
			
			
			
			 
			 list.add(oneChapter);
		 }
		
		
		
		return list;
	}
	@Override
	/**
	 * 保存
	 */
	public boolean saveChapter(EduChapter chapter) {
		if(chapter==null) {
			return false;
		}
		QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<EduChapter>();
		queryWrapper.eq("title", chapter.getTitle());
		Integer count = baseMapper.selectCount(queryWrapper);
		if(count>0) {
			return false;
		}
		int insert = baseMapper.insert(chapter);
		return insert==1;
	}
	@Override
	public boolean updateChapterById(EduChapter chapter) {
		if(chapter==null) {
			return false;
		}
		QueryWrapper<EduChapter> wrapper = new QueryWrapper<EduChapter>();
		wrapper.eq("title", chapter.getTitle());
		Integer selectCount = baseMapper.selectCount(wrapper);
		if(selectCount>0) {
			throw new EduException(20001,"章节名称已经存在");
		}
		int updateById = baseMapper.updateById(chapter);
		return  updateById==1;
	}
	@Override
	public Boolean removeChapterById(String id) {
		//判断章节的ID下面是否存在小节
		QueryWrapper<EduVideo> wrapper = new QueryWrapper<EduVideo>();
		wrapper.eq("chapter_id", id);
		List<EduVideo> list = eduVideoService.list(wrapper);
		if(list.size()!=0) {
			//如果有不能删除直接返回false
			throw new EduException(20001,"此章节下有小节，请先删除小节在删除");
		}
		int deleteById = baseMapper.deleteById(id);
		//删除
		return  deleteById==1;
	}

}
