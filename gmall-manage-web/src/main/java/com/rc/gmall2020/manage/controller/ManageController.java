package com.rc.gmall2020.manage.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rc.gmall2020.bean.BaseAttrInfo;
import com.rc.gmall2020.bean.BaseAttrValue;
import com.rc.gmall2020.bean.BaseCatalog1;
import com.rc.gmall2020.bean.BaseCatalog2;
import com.rc.gmall2020.bean.BaseCatalog3;
import com.rc.gmall2020.bean.BaseSaleAttr;
import com.rc.gmall2020.service.ManageService;

@RestController
@CrossOrigin
public class ManageController {
	
	@Reference
	
	private ManageService manageService;
	@RequestMapping("getCatalog1")
	
	
	public List<BaseCatalog1>getCatalog1(){
		
		List<BaseCatalog1> baseCatalog1 = manageService.getBaseCatalog1();
		return baseCatalog1;
		
		
	} 
	
	
	
	@RequestMapping("getCatalog2")
	
	
	public List<BaseCatalog2>getCatalog2(String catalog1Id){
		
		List<BaseCatalog2> baseCatalog2 = manageService.getBaseCatalog2(catalog1Id);
		return baseCatalog2;
		
		
	} 
	
	
	@RequestMapping("getCatalog3")
	
	
	public List<BaseCatalog3>getCatalog3(String catalog2Id){
		
		List<BaseCatalog3> baseCatalog3 = manageService.getBaseCatalog3(catalog2Id);
		return baseCatalog3;
		
		
	} 
	
	
	@RequestMapping("attrInfoList")
	
	public List<BaseAttrInfo>attrInfoList(String catalog3Id){
		
		List<BaseAttrInfo> baseAttrInfo = manageService.getBaseAttrInfo(catalog3Id);
		return baseAttrInfo;
		
		
	} 
	
	
	
	@RequestMapping("saveAttrInfo")
	
	public void saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){
		
		 manageService.saveAttrInfo(baseAttrInfo);
		
		
		
	} 
	
//	
//	@RequestMapping("getAttrValueList")
//	
//	public List<BaseAttrValue> getAttrValueList(String attrId){
//		return manageService.getAttrValueList(attrId);
//		
//		
//		
//	}
//	
	
	
	@PostMapping("getAttrValueList")
	
	public List<BaseAttrValue> getAttrValueList(String attrId){
		BaseAttrInfo baseAttrInfo = manageService.getAttrInfo(attrId);
		return baseAttrInfo.getAttrValueList();
		
	}
	
	@PostMapping("baseSaleAttrList")
	
	public List<BaseSaleAttr> baseSaleAttrList(){
		
	return	manageService.getbaseSaleAttrList();
		
		
	}
	

}
