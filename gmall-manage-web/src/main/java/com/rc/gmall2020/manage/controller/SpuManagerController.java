package com.rc.gmall2020.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rc.gmall2020.bean.SpuInfo;
import com.rc.gmall2020.service.ManageService;

@RestController
@CrossOrigin
public class SpuManagerController {
	@Reference
	private ManageService manageService;
	@RequestMapping("spuList")
	
	public List<SpuInfo> spuList( SpuInfo spuInfo){
		 List<SpuInfo> spuList = manageService.getspuList(spuInfo);
		return spuList;
		
		
	}
	
	@RequestMapping("saveSpuInfo")
	
	public void saveSpuInfo(@RequestBody SpuInfo spuInfo) {
		//调用保存
		if(spuInfo!=null) {
			
			manageService.saveSpuInfo(spuInfo);
		}
		
	}
	
	
	

}
