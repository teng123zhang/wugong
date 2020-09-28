package com.guli.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.guli.common.result.Result;
import com.guli.order.entity.UcenterMapperOrder;


@Component
@FeignClient("guli-ucenter")
public interface UcenterClient {
	
	 @PostMapping(value = "/ucenter/getUserInfoOrder/{id}")
	    public  UcenterMapperOrder getUserInfoOrder(@PathVariable String id);
	
	
	
	
	
	

}
