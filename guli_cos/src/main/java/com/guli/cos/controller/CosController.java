package com.guli.cos.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.guli.common.result.Result;
import com.guli.cos.config.PicUploadResult;
import com.guli.cos.conpoment.CosTemplate;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
@Api(tags = "图片上传")
@RequestMapping("oss")
@RestController
@CrossOrigin
public class CosController {
	
	@Autowired
	CosTemplate cosTemplate;
	
	
	@ApiOperation(value = "上传头像")
	@PostMapping("file/upload")
	
	public Result upload( @RequestParam("file")MultipartFile files,HttpServletResponse response) {
		
		
		PicUploadResult upload = cosTemplate.upload(files);
			
			
		
		
		return Result.ok().data("upload", upload);
	}

	
	
}
