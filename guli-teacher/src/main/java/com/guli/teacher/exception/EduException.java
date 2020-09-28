package com.guli.teacher.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@ApiModel(value = "全局异常")
public class EduException extends RuntimeException{
	
	@ApiModelProperty(value = "状态码")
	private Integer code;
	@ApiModelProperty(value = "异常消息")
	private String msg;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public EduException(Integer code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "EduException [code=" + code + ", msg=" + msg + "]";
	}
	
	

}
