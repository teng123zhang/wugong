package com.guli.order.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.guli.common.result.Result;
import com.guli.order.service.EduPayService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-28
 */
@RestController
@RequestMapping("/pay/edu-pay")
public class EduPayController {
	
	 @Autowired
	    private EduPayService eduPayService ;

	    //生成微信支付二维码接口
	    //参数是订单号
	    @GetMapping("createNative/{orderNo}")
	    public Result createNative(@PathVariable String orderNo) {
	        //返回信息，包含二维码地址，还有其他需要的信息
	        Map map = eduPayService.createNatvie(orderNo);
	        System.out.println("****返回二维码map集合:"+map);
	        return Result.ok().data(map);
	    }

	    //查询订单支付状态
	    //参数：订单号，根据订单号查询 支付状态
	    @GetMapping("queryPayStatus/{orderNo}")
	    public Result queryPayStatus(@PathVariable String orderNo) {
	        Map<String,String> map = eduPayService.queryPayStatus(orderNo);
	        System.out.println("*****查询订单状态map集合:"+map);
	        if(map == null) {
	            return Result.error().message("支付出错了");
	        }
	        //如果返回map里面不为空，通过map获取订单状态
	        if(map.get("trade_state").equals("SUCCESS")) {//支付成功
	            //添加记录到支付表，更新订单表订单状态
	        	eduPayService.updateOrdersStatus(map);
	            return Result.ok().message("支付成功");
	        }
	        return Result.ok().code(25000).message("支付中");

	    }

}

