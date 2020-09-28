package com.rc.gmall2020.manage.mapper;

import java.util.List;

import com.rc.gmall2020.bean.SkuSaleAttrValue;

import tk.mybatis.mapper.common.Mapper;

public interface SkuSaleAttrValueMapper extends Mapper<SkuSaleAttrValue> {
        /**
         * 获取销售属性id
         * @param spuId
         * @return
         */
	List<SkuSaleAttrValue> selectSkuSaleAttrValueCheckBySpu(String spuId);
       
}
