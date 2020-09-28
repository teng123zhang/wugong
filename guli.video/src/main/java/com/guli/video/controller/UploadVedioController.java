package com.guli.video.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.google.common.base.Throwables;
import com.guli.video.result.BaseResult;
import com.guli.video.result.Result;
import com.guli.video.service.UploadService;
import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.ApplyUploadRequest;
import com.tencentcloudapi.vod.v20180717.models.ApplyUploadResponse;
import com.tencentcloudapi.vod.v20180717.models.DescribeAllClassRequest;
import com.tencentcloudapi.vod.v20180717.models.DescribeAllClassResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/vid")
@CrossOrigin
public class UploadVedioController {
    @Autowired
    VodClient       vodClient;
    @Autowired
    VodUploadClient vodUploadClient;
    @Autowired
   private  UploadService uploadService ;

    @RequestMapping(value = "/getVedioClass", method = { RequestMethod.GET, RequestMethod.POST })
    public BaseResult<DescribeAllClassResponse> getVedioClass() {
    	BaseResult<DescribeAllClassResponse> returnResult = new BaseResult<DescribeAllClassResponse>();
        try {
            DescribeAllClassResponse resp = vodClient
                .DescribeAllClass(new DescribeAllClassRequest());
            returnResult.setSuccess(true);
            returnResult.setData(resp);
        } catch (Exception e) {
            log.error("getVedioClass error. msg:{}", Throwables.getRootCause(e));
            returnResult.setMsg("获取视频分类列表失败");
        }
        return returnResult;
    }

    @RequestMapping(value = "/applyUpload", method = { RequestMethod.GET, RequestMethod.POST })
    public BaseResult<ApplyUploadResponse> applyUpload(ApplyUploadRequest request) {

        BaseResult<ApplyUploadResponse> returnResult = new BaseResult<ApplyUploadResponse>();
        if (request == null || request.getMediaType() == null) {
            returnResult.setMsg("mdiaType不能为空");
            return returnResult;
        }
        try {
            ApplyUploadResponse resp = vodClient.ApplyUpload(request);
            returnResult.setSuccess(true);
            returnResult.setData(resp);
        } catch (Exception e) {
            log.error("applyUpload error. msg:{}", Throwables.getRootCause(e));
            returnResult.setMsg("申请上传失败");
        }
        return returnResult;

    }
/**
 * 上传视屏
 * @param file
 * @return
 */
    @RequestMapping(value = "/upload", method = { RequestMethod.GET, RequestMethod.POST })
    
    public Result upload(@RequestParam("file") MultipartFile file) {
    String videoSourceId=	uploadService.upload(file);
		return Result.ok().data("videoSourceId", videoSourceId);
    	
    	
    }
    
    /**
     * 删除视频
     */
    
    @DeleteMapping("{videoSourceId}")
    
    public Result getVideoPlayAuth(@PathVariable String videoSourceId) {
    Boolean flag=	uploadService.deleteVodById(videoSourceId);
    if(flag) {
    	return Result.ok();
    }
		return Result.error();
    	
    	
    	
    }
    
    
    /**
     * 根据多个视频ID删除视频
     */
    
    @DeleteMapping("removeList")
    
    public Result getRemoveList(@RequestParam("videoIdList") List<String> videoIdList) {
    Boolean flag=	uploadService.getRemoveListByIds(videoIdList);
    if(flag) {
    	return Result.ok();
    }
		return Result.error();
    	
    	
    	
    }
   
}