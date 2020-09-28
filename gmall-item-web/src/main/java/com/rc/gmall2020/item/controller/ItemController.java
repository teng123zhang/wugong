package com.rc.gmall2020.item.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.rc.gmall2020.bean.SkuImage;
import com.rc.gmall2020.bean.SkuInfo;
import com.rc.gmall2020.bean.SkuSaleAttrValue;
import com.rc.gmall2020.bean.SpuSaleAttr;
import com.rc.gmall2020.bean.SpuSaleAttrValue;
import com.rc.gmall2020.config.LoginRequire;
import com.rc.gmall2020.service.ListService;
import com.rc.gmall2020.service.ManageService;

@Controller
public class ItemController {
	@Reference
	private ManageService manageService;
	
	@Reference
	
	private ListService listService;
	
	@RequestMapping("{skuId}.html")
	//@LoginRequire //用户访问商品详情的时候必须登陆
	//根据skuId获取数据
	public  String item(@PathVariable(value = "skuId") String skuId,HttpServletRequest request) {
	SkuInfo skuInfo=	manageService.getSkuInfo(skuId);
	
	//根据skuId获取skuImage图片列表
List<SkuImage> skuImageList=	manageService.getSkuImageBySkuId(skuId);


//查询销售属性，销售属性值集合
List<SpuSaleAttr> spuSaleAttrList=manageService.getSpuSaleAttrLiCheckBySku(skuInfo);

for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
	List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
	
	for (SpuSaleAttrValue spuSaleAttr2 : spuSaleAttrValueList) {
		System.out.println("wode :"+spuSaleAttr2.getId());
	}
}

//获取销售属性值id
List<SkuSaleAttrValue> skuSaleAttrValueList=manageService.getSkuSaleAttrValue(skuInfo.getSpuId());



String key ="";

Map<String, Object> map = new HashMap<String, Object>();
for(int i=0;i<skuSaleAttrValueList.size();i++) {
	
	SkuSaleAttrValue skuSaleAttrValue = skuSaleAttrValueList.get(i);
	if(key.length()!=0) {
		
		key+="|";
	}
	key+=skuSaleAttrValue.getSaleAttrValueId();
	if((i+1)==skuSaleAttrValueList.size()||!skuSaleAttrValue.getSkuId().equals(skuSaleAttrValueList.get(i+1).getSkuId())) {
		
		
		map.put(key,skuSaleAttrValue.getSkuId());
		System.out.println(skuSaleAttrValue.getSkuId());
		key="";
		
		
	}
	
}

//将map转换为json字符串

String valuesSkuJson = JSON.toJSONString(map);
System.out.println("拼接的Json"+valuesSkuJson);
request.setAttribute("valuesSkuJson", valuesSkuJson);
request.setAttribute("spuSaleAttrList", spuSaleAttrList);
request.setAttribute("skuImageList", skuImageList);
	//保存到作用域
	request.setAttribute("skuInfo", skuInfo);
	listService.incrHotScore(skuId);
		return "item";
	}

}
