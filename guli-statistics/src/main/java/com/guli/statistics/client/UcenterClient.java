package com.guli.statistics.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.guli.common.result.Result;

@Component
@FeignClient("guli-ucenter")
public interface UcenterClient {
	
	@GetMapping(value = "/ucenter/member/{day}")
	public Result getRegisterNum(@PathVariable("day") String day);

}
