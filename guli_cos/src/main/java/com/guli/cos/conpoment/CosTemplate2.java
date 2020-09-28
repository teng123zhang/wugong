package com.guli.cos.conpoment;

import java.io.ByteArrayInputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.guli.cos.config.AppProjectConfig;
import com.guli.cos.config.PicUploadResult;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;


/**
 * 图片上传服务
 * 上传至腾讯云COS(对象存储中心)
 */
@Service
public class CosTemplate2 {

	 @Autowired
	    private COSClient cosClient;

	    @Autowired
	    private AppProjectConfig appProjectConfig ;

    // 允许上传的格式
    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg", ".jpeg", ".gif", ".png"};

    public PicUploadResult upload(MultipartFile uploadFile){
        // 校验图片格式
        boolean isLegal = false;
        for(String type : IMAGE_TYPE){
            if(StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(),type)){
                isLegal = true;
                break;
            }
        }
        // 封装Result对象，并且将文件的byte数组放置到result对象中
        PicUploadResult fileUploadResult = new PicUploadResult();
        if(!isLegal){
            fileUploadResult.setStatus("error:illegal file");
            return fileUploadResult;
        }

        // 文件新路径
        String fileName = uploadFile.getOriginalFilename();
        String filePath = getFilePath(fileName);

        //上传文件
        try {

            // 指定要上传到的存储桶
        	String bucketName = appProjectConfig.getBucketName();
            // 指定要上传到 COS 上对象键
            String key = filePath;
            //PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, new ByteArrayInputStream(uploadFile.getBytes()));
            //PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            //这里的key是查找文件的依据，妥善保管
            cosClient.putObject(bucketName,key,new ByteArrayInputStream(uploadFile.getBytes()),null);
            //设置输出信息
            fileUploadResult.setStatus("done");
            fileUploadResult.setName(this.appProjectConfig.getPath()+filePath);
            System.out.println(this.appProjectConfig.getPath()+filePath);
            fileUploadResult.setUid(String.valueOf(System.currentTimeMillis()));
            return fileUploadResult;
        }
        catch (Exception e){
            e.printStackTrace();
            fileUploadResult.setStatus("error: uploadFail");
            return fileUploadResult;
        }

    }
    
    /**
     * 删除某个图片
     * @param key
     */
    public boolean deletePic(String key){
        try {
            // 指定对象所在的存储桶
            String bucketName = this.appProjectConfig.getBucketName();
            cosClient.deleteObject(bucketName, key);
            return true;
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
            return false;
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
            return false;
        }
    }

    /**
     * 生成文件路径
     * @param sourceFileName
     * @return
     */
    private String getFilePath(String sourceFileName) {
        DateTime dateTime = new DateTime();
        return "/cover/"
                + dateTime.toString("yyyy") + "/" + dateTime.toString("MM")
                + "/" + dateTime.toString("dd")
                + "/" + System.currentTimeMillis()
                + RandomUtils.nextInt(10) + "."
                + StringUtils.substringAfterLast(sourceFileName, ".");
    }
}

