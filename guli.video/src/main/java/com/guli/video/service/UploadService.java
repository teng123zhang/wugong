package com.guli.video.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

	/**
	 * 插入视频
	 * @param file
	 * @return
	 */
	String upload(MultipartFile file);
	/**
	 * 根据Id删除腾讯云端视频
	 * @param videoSourceId
	 * @return
	 */

	Boolean deleteVodById(String videoSourceId);
	/**
	 * 根据多个ID视频删除多个云端视频
	 * @param videoIdList
	 * @return
	 */
	Boolean getRemoveListByIds(List<String> videoIdList);
	
	
	

}
