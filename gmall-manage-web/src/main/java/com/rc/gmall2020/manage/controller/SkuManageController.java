package com.rc.gmall2020.manage.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rc.gmall2020.bean.SkuInfo;
import com.rc.gmall2020.bean.SkuLsInfo;
import com.rc.gmall2020.bean.SpuImage;
import com.rc.gmall2020.bean.SpuSaleAttr;
import com.rc.gmall2020.service.ListService;
import com.rc.gmall2020.service.ManageService;

@RestController
@CrossOrigin
public class SkuManageController {
	
	@Reference
	private ManageService manageService;
	
	@Reference
	private ListService listService ;
	
	@RequestMapping("spuImageList")
	
	public List<SpuImage> spuImageList(SpuImage spuImage){
		return manageService.getSpuImageList(spuImage);
		
		
	}

	@RequestMapping("spuSaleAttrList")
	
	public List<SpuSaleAttr> spuSaleAttrList(String spuId){
		 
		return		manageService.getSpuSaleAttrList(spuId);
		
		
	}
	
	@RequestMapping("saveSkuInfo")
	
	public void saveSkuInfo(@RequestBody SkuInfo skuInfo) {
		if(skuInfo!=null) {
			
			manageService.saveSkuInfo(skuInfo);
		}
		
		
	}
	
	//上传一个商品，如果上传批量!
	@RequestMapping("onSale")
	
	public void onSale(String skuId) {
		SkuLsInfo skuLsInfo = new SkuLsInfo();
		//给SkuLsInfo赋值
		SkuInfo skuInfo = manageService.getSkuInfo(skuId);
		//属性拷贝
		BeanUtils.copyProperties(skuInfo, skuLsInfo);
		listService .saveSkuLsInfo(skuLsInfo);
		
	}
	
	
	
}
