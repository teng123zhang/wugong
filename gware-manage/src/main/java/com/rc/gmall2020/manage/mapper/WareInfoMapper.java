package com.rc.gmall2020.manage.mapper;

import java.util.List;

import com.rc.gmall2020.manage.bean.WareInfo;

import tk.mybatis.mapper.common.Mapper;

public interface WareInfoMapper extends Mapper<WareInfo> {


    public List<WareInfo> selectWareInfoBySkuid(String skuid);



}