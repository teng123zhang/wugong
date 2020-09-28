package com.rc.gmall2020.payment.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.h2.command.dml.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.rc.gmall2020.bean.OrderInfo;
import com.rc.gmall2020.bean.PaymentInfo;
import com.rc.gmall2020.bean.eunms.PaymentStatus;
import com.rc.gmall2020.payment.config.AlipayConfig;
import com.rc.gmall2020.service.OrderService;
import com.rc.gmall2020.service.PaymentService;

@Controller



public class PaymentController {
	
	@Reference
	private OrderService  orderService;
	
	@Autowired
    private PaymentService  paymentService;
	
	
	@Autowired
	private AlipayClient alipayClient;
	@RequestMapping("index")
	
	public String index(String orderId,HttpServletRequest req) {
		//选择支付渠道
		
		//获取总金额 通过orderId获取订单的总金额
		
		OrderInfo orderInfo= orderService.getOrderInfo(orderId);
		
		//保存订单编号
		req.setAttribute("orderId", orderId);
		//保存订单总金额
		req.setAttribute("totalAmount", orderInfo.getTotalAmount());
		
		
		
		
		return "index";
	}
	
	@RequestMapping(value = "/alipay/submit",method = RequestMethod.POST)
	@ResponseBody
	public String alipay(HttpServletRequest req,HttpServletResponse  httpResponse ) {
		/**
		 * 1保存支付记录 将数据放入到数据库
		 * 去重复 对账 幂等性 =保障每一笔交易 只能在支付宝中交易一次!（第三方交易编号 outTradeNo）
		 * paymentInfo
		 * 2生成二维码
		 * 
		 */
		//获取orderId
		String orderId = req.getParameter("orderId");
		//通过orderId获取OrderInfo
		OrderInfo orderInfo = orderService.getOrderInfo(orderId);
		
		System.out.println("orderInfo====>"+orderInfo);
		//属性赋值
		 
	    PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setOrderId(orderId);
		paymentInfo.setOutTradeNo(orderInfo.getOutTradeNo());
		paymentInfo.setTotalAmount(orderInfo.getTotalAmount());
		paymentInfo.setSubject("买手机付款");
		paymentInfo.setPaymentStatus(PaymentStatus.UNPAID);
		paymentInfo.setCreateTime(new Date());
		
		paymentService.savePaymentInfo(paymentInfo);
		
		//生成二维码
		
		//参数做成配置文件 进行软编码
		 //AlipayClient alipayClient =  new  DefaultAlipayClient( "https://openapi.alipay.com/gateway.do" , APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);  //获得初始化的AlipayClient 
		    AlipayTradePagePayRequest alipayRequest =  new  AlipayTradePagePayRequest(); //创建API对应的request 
		    //设置同步回调
		    alipayRequest.setReturnUrl(AlipayConfig.return_payment_url);
		    //alipayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp");
		    //设置异步回调
		   // alipayRequest.setNotifyUrl("http://domain.com/CallBack/notify_url.jsp"); //在公共参数中设置回跳和通知地址
		    alipayRequest.setNotifyUrl(AlipayConfig.notify_payment_url);
		   // alipayRequest.putOtherTextParam("app_auth_token", "201611BB8xxxxxxxxxxxxxxxxxxxedcecde6");//如果 ISV 代商家接入电脑网站支付能力，则需要传入 app_auth_token，使用第三方应用授权；自研开发模式请忽略
		    //参数
		    //声明一个map
		    Map<String, Object> map = new HashMap<>();
		    map.put("out_trade_no",paymentInfo.getOutTradeNo());
	        map.put("product_code","FAST_INSTANT_TRADE_PAY");
	        map.put("subject",paymentInfo.getSubject());
	        map.put("total_amount",paymentInfo.getTotalAmount());
	        /*把封装好的参数  传递给支付宝*/
	        alipayRequest.setBizContent(JSON.toJSONString(map));
		    
//		    alipayRequest.setBizContent( "{"  +
//		         "    \"out_trade_no\":\"20150320010101001\","  +
//		         "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\","  +
//		         "    \"total_amount\":88.88,"  +
//		         "    \"subject\":\"Iphone6 16G\","  +
//		         "    \"body\":\"Iphone6 16G\","  +
//		         "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\","  +
//		         "    \"extend_params\":{"  +
//		         "    \"sys_service_provider_id\":\"2088511833207846\""  +
//		         "    }" +
//		         "  }" ); //填充业务参数 
		    String form= "" ;
		     try  {
		        form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单 
		    }  catch  (AlipayApiException e) {
		        e.printStackTrace();
		    }
		    httpResponse.setContentType("text/html;charset=UTF-8");
//		    httpResponse.getWriter().write(form); //直接将完整的表单html输出到页面 
//		    httpResponse.getWriter().flush();
//		    httpResponse.getWriter().close();
		    
		   paymentService.sendDelayPaymentResult(paymentInfo.getOutTradeNo(), 15, 3);
			
		return form;
		
		
	}
	//同步回调
	//付款完成之后,购物车数据应该清空
	@RequestMapping("alipay/callback/return")
	
	public String callbackReturn() {
		
		
		return "redirect:"+AlipayConfig.return_order_url;
		
		
	}
	
	//异步回调
	/**
	 * 1首先进行字典排序,组成字符串，得到待签名字符串，使用base64编码发送给支付宝
	 *   支付宝除了sign，sign_type 在将字符串通过base64编码返回给商家，如果她们的顺序，都完全一致!
	 *   2验签里面的内容
	 *   out_trade_no total_amount 等参数信息
	 *   
	 *   只有交易装态为TRADE_SUCCESS 或者TRADE_FINISHED 时 ，支付宝才认为买家付款成功
	 *   
	 * @return
	 */
	@RequestMapping("alipay/callback/notify")
	
	public String callbackNotify(@RequestParam Map<String,String> paramMap, HttpServletRequest request) {
		
		
		  /*异步验签*/
//      Map<String, String> paramsMap = ... //将异步通知中收到的所有参数都存放到map中
      boolean signVerified = false; //调用SDK验证签名
      try {
          /* paramMap:异步通知中收到的所有参数  SIGN_TYPE:签名类型  alipay_public_key:用户自己的支付KEY码  charset:编码格式  */
          signVerified = AlipaySignature.rsaCheckV1(paramMap, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
      } catch (AlipayApiException e) {
          e.printStackTrace();
      }
      if(signVerified){
          // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
          /*对业务的二次校验*/
          /*只有交易状态为TRADE_SUCCESS--交易支付成功  或者TRADE_FINISHED--	交易结束，不可退款才算成功*/
          /*获取交易状态*/
          String trade_status = paramMap.get("trade_status");
          /*通过商户订单号查询支付记录    out_trade_no:商户订单号*/
          String out_trade_no = paramMap.get("out_trade_no");
          /*WAIT_BUYER_PAY	交易创建，等待买家付款
          TRADE_CLOSED	未付款交易超时关闭，或支付完成后全额退款
          TRADE_SUCCESS	交易支付成功
          TRADE_FINISHED	交易结束，不可退款*/
          /*交易状态*/
          
          if("TRADE_SUCCESS".equals(trade_status) || "TRADE_FINISHED".equals(trade_status)){
        	  //当前订单支付状态如果是已经付款，或者是关闭
        	  
              PaymentInfo paymentInfo = new PaymentInfo();
              /*将商户订单号保存到支付表中*/
              paymentInfo.setOutTradeNo(out_trade_no);
              /*通过商户订单号 查询支付信息*/
              PaymentInfo paymentInfoHas = paymentService.getPaymentInfo(paymentInfo);
              /*UNPAID("支付中"),
              PAID("已支付"),
              PAY_FAIL("支付失败"),
              ClOSED("已关闭");*/
              /*支付状态*/
              if (paymentInfoHas.getPaymentStatus()== PaymentStatus.PAID || paymentInfoHas.getPaymentStatus()== PaymentStatus.ClOSED) {
                  return "fail";
              }
              /*更新交易记录状态*/
              // 修改
              PaymentInfo paymentInfoUpd = new PaymentInfo();
              // 设置状态
              paymentInfoUpd.setPaymentStatus(PaymentStatus.PAID);
              // 设置创建时间
              paymentInfoUpd.setCallbackTime(new Date());
              // 设置内容
              paymentInfoUpd.setCallbackContent(paramMap.toString());
              paymentService.updatePaymentInfo(out_trade_no,paymentInfoUpd);
              /*发送消息队列给订单*/
              paymentService.sendPaymentResult(paymentInfo,"success");
              return "success";
          }
      }else{
          // TODO 验签失败则记录异常日志，并在response中返回failure.
          return "failure";
      }
      return "failure";
  }

  // 发送验证
  @RequestMapping("sendPaymentResult")
  @ResponseBody
  public String sendPaymentResult(PaymentInfo paymentInfo,@RequestParam("result") String result){
      paymentService.sendPaymentResult(paymentInfo,result);
      return "sent payment result";
  }

  /*退款*/
  /*http://payment.gmall.com/refund?orderId=100*/
  @RequestMapping("refund")
  @ResponseBody
  public String refund(String orderId){
      boolean flag = paymentService.refund(orderId);
      System.out.println("flag:"+flag);
      return flag+"";
  }

  // 查询订单信息
  @RequestMapping("queryPaymentResult")
  @ResponseBody
  public String queryPaymentResult(HttpServletRequest request){
      String orderId = request.getParameter("orderId");
      PaymentInfo paymentInfo = new PaymentInfo();
      paymentInfo.setOrderId(orderId);
      PaymentInfo paymentInfoQuery = paymentService.getPaymentInfo(paymentInfo);
      boolean flag = paymentService.checkPayment(paymentInfoQuery);
      return ""+flag;
  }


		
	
	
	
	
	
	
	//根据orderId支付
	@RequestMapping("wx/submit")
	
	@ResponseBody
	public Map wxSbumit(String orderId) {
		
		//调用服务层生成数据
		//orderId订单编号 1表示1分
		orderId=UUID.randomUUID().toString().replace("-", "");
		Map map=paymentService.createNative(orderId,"1");
		
		System.out.println("url====>"+map.get("code_url"));
		
		return map;
	}
	
	
}