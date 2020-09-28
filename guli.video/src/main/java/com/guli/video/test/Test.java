package com.guli.video.test;

import com.alibaba.fastjson.JSONObject;
import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;

public class Test {
    public static void main(String[] args) {
        String region = "ap-shanghai";
        String secretId = "AKIDaw2sSmCfEWYIo4HlTTIsDI6uUt474Qfd";
        String secretKey = "76i38xVmmoZ6ML3UeYS6YkfJgmbT4RWK";
        VodUploadClient vodClient = new VodUploadClient(secretId, secretKey);
        VodUploadRequest request = new VodUploadRequest();
        request.setMediaFilePath("D:\\vid\\mylove.mp4");
        request.setCoverFilePath("D:\\pic\\5.jpg");
        try {
            VodUploadResponse response = vodClient.upload(region, request);
            System.out.println(JSONObject.toJSONString(response));
            // log.info("Upload FileId = {}", gson.toJson(response));
        } catch (Exception e) {
        	System.out.println(e.getMessage());
            // 业务方进行异常处理
        }

    }
}
