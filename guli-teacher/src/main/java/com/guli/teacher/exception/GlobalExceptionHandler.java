package com.guli.teacher.exception;



import org.eclipse.jetty.util.log.Log;

/**
 * 全局异常处理器
 */

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guli.common.result.ExceptionUtils;
import com.guli.common.result.Result;

import lombok.extern.slf4j.Slf4j;
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	//全局异常处理器
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	
	public Result error(Exception e) {
		
		e.printStackTrace();
		return Result.error().message("出错了");
	}
	
	

	@ExceptionHandler(ArithmeticException.class)
	@ResponseBody
	
	public Result ArithmeticExceptionError(Exception e) {
		
		e.printStackTrace();
		return Result.error().message("除数不能为0");
	}
	
	@ExceptionHandler(EduException.class)
	@ResponseBody
	
	public Result error(EduException e) {
		
		e.printStackTrace();
	   log.error(ExceptionUtils.getMessage(e));

		return Result.error().code(e.getCode()).message(e.getMsg());
	}
	


}
