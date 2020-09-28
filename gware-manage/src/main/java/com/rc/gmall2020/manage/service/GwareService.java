package com.rc.gmall2020.manage.service;

import java.util.List;
import java.util.Map;

import com.rc.gmall2020.manage.bean.WareInfo;
import com.rc.gmall2020.manage.bean.WareOrderTask;
import com.rc.gmall2020.manage.bean.WareSku;

public interface GwareService {
	
	public Integer  getStockBySkuId(String skuid);

    public boolean  hasStockBySkuId(String skuid,Integer num);

    public List<WareInfo> getWareInfoBySkuid(String skuid);

    public void addWareInfo();

    public Map<String,List<String>> getWareSkuMap(List<String> skuIdlist);

    public void addWareSku(WareSku wareSku);

    public void deliveryStock(WareOrderTask taskExample) ;

    public WareOrderTask saveWareOrderTask(WareOrderTask wareOrderTask );

    public  List<WareOrderTask>   checkOrderSplit(WareOrderTask wareOrderTask);

    public void lockStock(WareOrderTask wareOrderTask);

    public List<WareOrderTask> getWareOrderTaskList(WareOrderTask wareOrderTask);

    public List<WareSku> getWareSkuList();

    public List<WareInfo> getWareInfoList();

}
