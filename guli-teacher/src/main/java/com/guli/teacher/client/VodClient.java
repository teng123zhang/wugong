package com.guli.teacher.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.guli.common.result.Result;



@FeignClient("guli-video")
@Component
public interface VodClient {
	
	 @DeleteMapping(value = "/vid/{videoSourceId}")
	    
	    public Result getVideoPlayAuth(@PathVariable String videoSourceId); 
	
	
	@DeleteMapping(value="/vid/removeList")
	
	public Result getRemoveList(@RequestParam("videoIdList") List<String> videoIdList);

}
