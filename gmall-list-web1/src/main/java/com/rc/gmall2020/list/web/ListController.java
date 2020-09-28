package com.rc.gmall2020.list.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.rc.gmall2020.bean.BaseAttrInfo;
import com.rc.gmall2020.bean.BaseAttrValue;
import com.rc.gmall2020.bean.SkuLsInfo;
import com.rc.gmall2020.bean.SkuLsResult;
import com.rc.gmall2020.bean.SkuParams;
import com.rc.gmall2020.service.ListService;
import com.rc.gmall2020.service.ManageService;

@Controller
public class ListController {
	@Reference
	private ListService listService;
	
	@Reference
	
	private ManageService manageService;
	@RequestMapping("list.html")
	//@ResponseBody
	public String listData(SkuParams skuParams,HttpServletRequest request) {
		//设置每页显示的大小
		skuParams.setPageSize(2);

		SkuLsResult skuLsResult = listService.search(skuParams);
		
		//显示商品数据
		 List<SkuLsInfo> skuLsInfoList = skuLsResult.getSkuLsInfoList();
	     // 平台属性，平台属性值查询
		 //获取平台属性值集合
		 List<String> attrValueIdList = skuLsResult.getAttrValueIdList();
//		 for(String attr:attrValueIdList) {
//			 System.out.println( attr+" attr*******");
//		 }
		 
		 System.out.println(skuParams);
		List<BaseAttrInfo> baseAttrInfoList= manageService.getAttrList(attrValueIdList);
		
		
		//定义一个面包屑集合
		List<BaseAttrValue> baseAttrValueList = new ArrayList<>();
		      //编写一个方法来判断url 后面的参数条件
				String urlParam = makeUrlParam(skuParams);
				
		//使用迭代器
				for (Iterator<BaseAttrInfo> iterator =  baseAttrInfoList.iterator(); iterator.hasNext(); ) {
		            /*平台属性*/
		            BaseAttrInfo baseAttrInfo = iterator.next();
		            //System.out.println("baseAttrInfo:"+baseAttrInfo);
		            List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
		            /*平台属性值遍历*/
		            for(BaseAttrValue baseAttrValue : attrValueList){
		                 String[] valueId = skuParams.getValueId();
		               
						if(valueId!=null && valueId.length>0){
		                	//skuParams.getValueId();
		                    for(String Id : valueId){
		                        if(Id.equals(baseAttrValue.getId())){
							//如果平台属性值相同将数据移除
		                        	iterator.remove();
							
							//面包屑的组成
							//baseAttrInfo.getAttrName()+":"+baseAttrValue.getValueName();
							
							BaseAttrValue  baseAttrValueed= new  BaseAttrValue();
							//将平台属性值的名称改为面包屑
							baseAttrValueed.setValueName(baseAttrInfo.getAttrName()+":"+baseAttrValue.getValueName());
							
							
							//将用户点击的平台属性值Id传递到makeUrlParam中,重新制作返回 的url参数
							String newUrlParam = makeUrlParam(skuParams,valueId);
							//重新制作urlParam返回值
							baseAttrValueed.setUrlParam(newUrlParam);
							//将平台属性值(baseAttrValueed)存入list
							baseAttrValueList.add(baseAttrValueed);
						}
		                        
		                    	System.out.println(baseAttrValue.getId());

		                    
		                       
		                        
					}
					
				}
			}
			
		}
				//保存分页数据
				
				request.setAttribute("pageNo",skuParams.getPageNo() );
				request.setAttribute("totalPages",skuLsResult.getTotalPage() );
		//检索关键字
		request.setAttribute("keyword",skuParams.getKeyword());
		
		//将面包屑保存到作用域
		request.setAttribute("baseAttrValueList",baseAttrValueList);
		
		//保存到作用域
		request.setAttribute("urlParam", urlParam);
		
		//保存平台属性 集合
		request.setAttribute("baseAttrInfoList", baseAttrInfoList);
		//String jsonString = JSON.toJSONString(skuLsResult);
		request.setAttribute("skuLsInfoList", skuLsInfoList);
		return "list";
	}
	/**
	 * 判断url 后面有那些参数
	 * @param excludeValueIds 点击面包屑获取的平台属性值id
	 * @param skuParams
	 * @return
	 */
	private String makeUrlParam(SkuParams skuParams,String...excludeValueIds) {
		String urlParam ="";
		 
		//根据keyword
		if(skuParams.getKeyword()!=null && skuParams.getKeyword().length()>0) {
			
			urlParam+="keyword="+skuParams.getKeyword();	
			
		}
		//判断三级分类id
		if(skuParams.getCatalog3Id()!=null && skuParams.getCatalog3Id().length()>0) {
			//如果有多个参数则拼接&符号
			if(urlParam.length()>0) {
				urlParam+="&";
			}
			
			urlParam+="catalog3Id="+skuParams.getCatalog3Id();
		}
		
		//平台属性值id
		
		if(skuParams.getValueId()!=null && skuParams.getValueId().length>0) {
			
			//循环遍历
			for (String valueId : skuParams.getValueId()) {
				if(excludeValueIds!=null && excludeValueIds.length>0) {
					//获取点击面包屑时的平台属性值id
					
					String excludeValueId =excludeValueIds[0];
					System.out.println(excludeValueIds[0]+"excludeValueIds[0]");
					if(excludeValueId.equals(valueId)) {
						
						//break;continue
						
						continue;
					}
					
				}
				//System.out.println(valueId+"valueId");
				//如果有多个参数则拼接&符号
				if(urlParam.length()>0) {
					urlParam+="&";
				}
				
				urlParam+="valueId"+valueId;
			}
		}
		
		//制作好的返回参数
		return urlParam;
	}

}
