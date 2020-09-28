package com.guli.video.test;


	
	
	import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaResponse;

	public class RemoveVideoTest
	
	
	{
	    public static void main(String [] args) {
	        try{

	            Credential cred = new Credential("AKIDaw2sSmCfEWYIo4HlTTIsDI6uUt474Qfd", "76i38xVmmoZ6ML3UeYS6YkfJgmbT4RWK");
	            
	            HttpProfile httpProfile = new HttpProfile();
	            httpProfile.setEndpoint("vod.tencentcloudapi.com");

	            ClientProfile clientProfile = new ClientProfile();
	            clientProfile.setHttpProfile(httpProfile);
	            
	            VodClient client = new VodClient(cred, "ap-shanghai", clientProfile);
	             String Id="5285890804143898679";
	            String params = "{FileId:"+Id+"}";
	           
	            		
	            DeleteMediaRequest req = DeleteMediaRequest.fromJsonString(params, DeleteMediaRequest.class);
	            
	            DeleteMediaResponse resp = client.DeleteMedia(req);
	            
	            System.out.println(DeleteMediaRequest.toJsonString(resp));
	        } catch (TencentCloudSDKException e) {
	                System.out.println(e.toString());
	        }

	    }
	    
	}

