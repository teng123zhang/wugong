package com.rc.gmall2020.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rc.gmall2020.bean.BaseAttrInfo;

import tk.mybatis.mapper.common.Mapper;

public interface BaseAttrInfoMapper extends Mapper<BaseAttrInfo> {
/**
 * 根据三级分类Id查询平台属性集合
 * @param catalog3Id
 * @return
 */
	List<BaseAttrInfo> selectBaseAttrInfoListByCatalog3Id(String catalog3Id);
/**
 * 平台属性值id查询
 * @param valueIds
 * @return
 */
List<BaseAttrInfo> selectAttrInfoListByIds(@Param("valueIds")String valueIds);

}
