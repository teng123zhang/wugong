package com.guli.video.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Throwables;
import com.guli.video.result.BaseResult;
import com.guli.video.service.UploadService;
import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaResponse;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class UploadVedioServiceimpl implements UploadService {
	     @Autowired
	    VodClient       vodClient;
	    @Autowired
	    VodUploadClient vodUploadClient;
	
	 public String upload( MultipartFile file) {
	        BaseResult<VodUploadResponse> returnResult = new BaseResult<VodUploadResponse>();
	        if (file.isEmpty()) {
	            returnResult.setMsg("上传失败，请选择文件");
	            return null;
	        }

	        String fileName = file.getOriginalFilename();
	        String filePath = "D:\\vid";
	        File dest = null;
	        try {
	            dest = new File(filePath + fileName);
	            file.transferTo(dest);
	            log.info("上传成功");
	        } catch (Exception e) {
	            log.error(e.toString(), e);
	        }
	        VodUploadRequest request = new VodUploadRequest();
	        request.setMediaFilePath(dest.getAbsolutePath());
	        try {
	            VodUploadResponse response = vodUploadClient.upload(vodClient.getRegion(), request);
	            returnResult.setSuccess(true);
	            returnResult.setData(response);
	        } catch (Exception e) {
	            log.error("upload error. msg:{}", Throwables.getRootCause(e));
	            returnResult.setMsg("上传失败");
	        }
	        System.out.println("返回值是=====>"+returnResult.getData().getFileId());
	        return returnResult.getData().getFileId();
	    }

	@Override
	public Boolean deleteVodById(String videoSourceId) {
		
		     String region = "ap-shanghai";
	        String secretId = "AKIDaw2sSmCfEWYIo4HlTTIsDI6uUt474Qfd";
	        String secretKey = "76i38xVmmoZ6ML3UeYS6YkfJgmbT4RWK";
		    
		        try{

		            Credential cred = new Credential(secretId, secretKey);
		            
		            HttpProfile httpProfile = new HttpProfile();
		            httpProfile.setEndpoint("vod.tencentcloudapi.com");

		            ClientProfile clientProfile = new ClientProfile();
		            clientProfile.setHttpProfile(httpProfile);
		            
		            VodClient client = new VodClient(cred, region, clientProfile);
		           
		            String params = "{FileId:"+videoSourceId+"}";
		           
		            		
		            DeleteMediaRequest req = DeleteMediaRequest.fromJsonString(params, DeleteMediaRequest.class);
		            
		            DeleteMediaResponse resp = client.DeleteMedia(req);
		            String requestId = resp.getRequestId();
		            if(StringUtils.isEmpty(requestId)) {
		            	return false;
		            }
		            System.out.println(DeleteMediaRequest.toJsonString(resp));
		            
		        } catch (TencentCloudSDKException e) {
		                System.out.println(e.toString());
		                
		        }
		        return true;
		    }

	@Override
	public Boolean getRemoveListByIds(List<String> videoIdList) {
		  String region = "ap-shanghai";
	        String secretId = "AKIDaw2sSmCfEWYIo4HlTTIsDI6uUt474Qfd";
	        String secretKey = "76i38xVmmoZ6ML3UeYS6YkfJgmbT4RWK";
		    
		        try{

		            Credential cred = new Credential(secretId, secretKey);
		            
		            HttpProfile httpProfile = new HttpProfile();
		            httpProfile.setEndpoint("vod.tencentcloudapi.com");

		            ClientProfile clientProfile = new ClientProfile();
		            clientProfile.setHttpProfile(httpProfile);
		            
		            VodClient client = new VodClient(cred, region, clientProfile);
		           String str  =org.apache.commons.lang.StringUtils.join(videoIdList, ",");
		         
		           String FileId="FileId:";
		           
		        
		           System.out.println("视频的IDS:"+str);
		            String params = "{"+FileId+str+"}";
		           
		            		
		            DeleteMediaRequest req = DeleteMediaRequest.fromJsonString(params, DeleteMediaRequest.class);
		            
		            DeleteMediaResponse resp = client.DeleteMedia(req);
		            String requestId = resp.getRequestId();
		            if(StringUtils.isEmpty(requestId)) {
		            	return false;
		            }
		            System.out.println(DeleteMediaRequest.toJsonString(resp));
		            
		        } catch (TencentCloudSDKException e) {
		                System.out.println(e.toString());
		                
		        }
		        return true;
		 
	}
		
		
	}


