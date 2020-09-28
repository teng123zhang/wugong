package com.guli.teacher.service.impl;

import com.guli.teacher.client.VodClient;
import com.guli.teacher.entity.EduVideo;
import com.guli.teacher.mapper.EduVideoMapper;
import com.guli.teacher.service.EduVideoService;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-15
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
@Autowired
private VodClient vodClient;
	@Override
	public boolean removeVideoById(String id) {
		//删除腾讯云上的视频
		EduVideo video = baseMapper.selectById(id);
		String videoSourceId= video.getVideoSourceId();
		if(!StringUtils.isEmpty(videoSourceId)) {
			vodClient.getVideoPlayAuth(videoSourceId);
		}
		//删除数据库中的video
		int deleteById = baseMapper.deleteById(id);
		return deleteById==1;
	}
	@Override
	public boolean removeVideoByCourseId(String courseId) {
		//根据课程ID查询所有的小节
		QueryWrapper<EduVideo> wrapper = new QueryWrapper<EduVideo>();
		wrapper.eq("course_id",courseId);
		wrapper.select("video_source_id");
		List<EduVideo> videoList = baseMapper.selectList(wrapper);
		//定义一个集合存放视频ID
		List<String> videoIds= new ArrayList<String>();
		//可以获取视频的ID
		for(EduVideo eduVideo:videoList) {
			if(!StringUtils.isEmpty(eduVideo.getVideoSourceId())) {
				videoIds.add(eduVideo.getVideoSourceId());
				
			}
		}
		
		if(videoIds.size()>0) {
			vodClient.getRemoveList(videoIds);
		}
		//删除
		
		QueryWrapper<EduVideo> wr = new QueryWrapper<EduVideo>();
		wr.eq("course_id",courseId);
		int delete = baseMapper.delete(wr);
		
		return delete>0 ;
	}

}
