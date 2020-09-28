package com.guli.order.service;


import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.order.entity.EduPay;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-28
 */
public interface EduPayService extends IService<EduPay> {
	//生成微信支付二维码接口
	Map createNatvie(String orderNo);
	//根据订单号查询订单支付状态
	Map<String, String> queryPayStatus(String orderNo);
	//向支付表添加记录，更新订单状态
	void updateOrdersStatus(Map<String, String> map);

}
