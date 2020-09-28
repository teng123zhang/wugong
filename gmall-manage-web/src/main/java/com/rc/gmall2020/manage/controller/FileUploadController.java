package com.rc.gmall2020.manage.controller;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.javassist.compiler.ast.InstanceOfExpr;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin
public class FileUploadController {
	//@value使用的前提条件是,当前k类必须在spring容器中
	@Value("${fileServer.url}")
	private String fileUrl; // fileUrl=192.168.134.130
	//获取上传文件，需要使用SpringMvc技术
	@RequestMapping("fileUpload")
	public String fileUpload(MultipartFile file) throws IOException, MyException  {
		//当文件不为空的时候文件上传
		String imgUrl = fileUrl;
		if(file!=null) {
			
			String Configfile = this.getClass().getResource("/tracker.conf").getFile();
			ClientGlobal.init(Configfile);
			TrackerClient trackerClient =new TrackerClient();
			TrackerServer trackerServer =trackerClient.getConnection();
			StorageClient storageClient = new StorageClient(trackerServer,null);
			//获取文件上传名称
			String originalFilename = file.getOriginalFilename();
			//获取文件的后缀名
			String extName = StringUtils.substringAfterLast(originalFilename, ".");
			//String orginaFileName= file;
			//获取本地文件
			//String[] upload_file=storageClient.upload_file(originalFilename, extName, null);
			String[] upload_file=storageClient.upload_file(file.getBytes(), extName, null);
			for(int i=0;i<upload_file.length;i++) {
				String path= upload_file[i];
				imgUrl+="/"+path;
				System.out.println("imgUrl="+imgUrl);
			}
			
		
		}
		return imgUrl;
		
	}

}
