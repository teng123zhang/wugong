package com.rc.gmall2020.service;

import java.util.List;

import com.rc.gmall2020.bean.BaseAttrInfo;
import com.rc.gmall2020.bean.BaseAttrValue;
import com.rc.gmall2020.bean.BaseCatalog1;
import com.rc.gmall2020.bean.BaseCatalog2;
import com.rc.gmall2020.bean.BaseCatalog3;
import com.rc.gmall2020.bean.BaseSaleAttr;
import com.rc.gmall2020.bean.SkuImage;
import com.rc.gmall2020.bean.SkuInfo;
import com.rc.gmall2020.bean.SkuSaleAttrValue;
import com.rc.gmall2020.bean.SpuImage;
import com.rc.gmall2020.bean.SpuInfo;
import com.rc.gmall2020.bean.SpuSaleAttr;

public interface ManageService {
	/**
	 * 获取所有的一级分类数据
	 * @return
	 */
	List<BaseCatalog1> getBaseCatalog1();
	
	/**
	 * 获取所有的二级分类数据
	 * @param catalog1Id
	 * @return
	 */
	
	List<BaseCatalog2>getBaseCatalog2(String catalog1Id);
	
	/**
	 * 获取所有的三级分类数据
	 * @param catalog2Id
	 * @return
	 */
	
	List<BaseCatalog3>getBaseCatalog3(String catalog2Id );
	
	/**
	 * 根据三级分类id查询平台属性
	 * @param catalog3Id
	 * @return
	 */
	
	List<BaseAttrInfo>getBaseAttrInfo(String catalog3Id);
/**
 * 保存平台的属性
 * @param baseAttrInfo
 */
	void saveAttrInfo(BaseAttrInfo baseAttrInfo);
/**
 * 根据平台属性id查询平添属性值
 * @param attrId
 * @return
 */
//List<BaseAttrValue> getAttrValueList(String attrId);
/**
 * 根据平台属性id查询平台属性对象
 * @param attrId
 * @return
 */
BaseAttrInfo getAttrInfo(String attrId);

/**
 * 根据spuInfo对象属性获取spuinfo的集合
 * @param spuInfo
 * @return
 */
List<SpuInfo> getspuList(SpuInfo spuInfo);
/**
 * 获取所有的销售属性
 * @return
 */
List<BaseSaleAttr>getbaseSaleAttrList();
/**
 * 保存spuInfo
 * @param spuInfo
 */
void saveSpuInfo(SpuInfo spuInfo);
/**
 * 根据SpuId查询图片列表
 * @param spuImage
 * @return
 */
List<SpuImage> getSpuImageList(SpuImage spuImage);
/**
 * 根据SpuId查询平台属性和平台属性值
 * @param spuId
 * @return
 */
List<SpuSaleAttr> getSpuSaleAttrList(String spuId);
/**
 * 保存SkuInfo
 * @param skuInfo
 */
void saveSkuInfo(SkuInfo skuInfo);
/**
 * 根据skuId查询SkuInfo
 * @param skuId
 * @return
 */
SkuInfo getSkuInfo(String skuId);
/**
 * 根据skuId查询普片列表
 * @param skuId
 * @return
 */
List<SkuImage> getSkuImageBySkuId(String skuId);
/**
 * 根据skuInfo查询销售属性和销售属性值集合
 * @param skuInfo
 * @return
 */
List<SpuSaleAttr> getSpuSaleAttrLiCheckBySku(SkuInfo skuInfo);
/**
 * 获取销售属性id
 * @param spuId
 * @return
 */
List<SkuSaleAttrValue> getSkuSaleAttrValue(String spuId);
/**
 * 根据平台属性值id查询销售属性值集合
 * @param attrValueIdList
 * @return
 */
List<BaseAttrInfo> getAttrList(List<String> attrValueIdList);
}


