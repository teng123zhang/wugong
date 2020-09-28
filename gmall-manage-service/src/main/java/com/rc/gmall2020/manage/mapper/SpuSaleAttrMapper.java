package com.rc.gmall2020.manage.mapper;

import java.util.List;

import com.rc.gmall2020.bean.SpuSaleAttr;

import tk.mybatis.mapper.common.Mapper;

public interface SpuSaleAttrMapper extends Mapper<SpuSaleAttr>{
/**
 *根据spuId查询销售属性集合
 *需要使用mapper.xml写在resourse目录下
 * @param spuId
 * @return
 */
	List<SpuSaleAttr> selectAttrList(String spuId);
/**
 * 根据spuId和skuId查询平台属性和平台属性值
 * @param id
 * @param spuId
 * @return
 */
List<SpuSaleAttr> selectSpuSaleAttrListCheckBySku(String skuId, String spuId);

}
