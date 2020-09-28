package com.guli.cos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;



@Configuration
public class AppProjectConfig {
	     @Value("${oss.secretId}")
	    private String secretId;

	    @Value("${oss.secretKey}")
	    private String secretKey;

	    @Value("${oss.region}")
	    private String region;

	    @Value("${oss.bucketName}")
	    private String bucketName;

	    @Value("${oss.url}")
	    private String path;


	    @Bean
	    public COSClient cosClient(){
	        // 1 初始化用户身份信息（secretId, secretKey）。
	        COSCredentials cred = new BasicCOSCredentials(this.secretId, this.secretKey);
	        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
	        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
	        Region region = new Region(this.region);
	        ClientConfig clientConfig = new ClientConfig(region);
	        // 3 生成 cos 客户端。
	        COSClient cosClient = new COSClient(cred, clientConfig);
	        return cosClient;
	    }


		public String getSecretId() {
			return secretId;
		}


		public void setSecretId(String secretId) {
			this.secretId = secretId;
		}


		public String getSecretKey() {
			return secretKey;
		}


		public void setSecretKey(String secretKey) {
			this.secretKey = secretKey;
		}


		public String getRegion() {
			return region;
		}


		public void setRegion(String region) {
			this.region = region;
		}


		public String getBucketName() {
			return bucketName;
		}


		public void setBucketName(String bucketName) {
			this.bucketName = bucketName;
		}


		public String getPath() {
			return path;
		}


		public void setPath(String path) {
			this.path = path;
		}


}
