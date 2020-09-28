package com.rc.gmall2020.manage.mapper;

import java.util.List;

import com.rc.gmall2020.manage.bean.WareSku;

import tk.mybatis.mapper.common.Mapper;

public interface WareSkuMapper extends Mapper<WareSku> {


    public Integer selectStockBySkuid(String skuid);

    public int incrStockLocked(WareSku wareSku);

    public int selectStockBySkuidForUpdate(WareSku wareSku);

    public int  deliveryStock(WareSku wareSku);

    public List<WareSku> selectWareSkuAll();
}