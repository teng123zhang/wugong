package com.guli.statistics.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.guli.common.result.Result;
import com.guli.statistics.service.DailyService;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-19
 */
@RestController
@RequestMapping("/statistics")
@CrossOrigin
public class DailyController {
	//获取某一天的注册人数,把注册人数添加到统计分析表
	@Autowired
	private DailyService dailyService; 
	@PostMapping("/daily/{day}")
	
	public Result createDate(@PathVariable String day) {
		//获取统计数据添加到统计分析表中
		dailyService.getDataAdd(day);
		return Result.ok();
	}
	
	
	//返回进行统计的数据,两个数组
	@GetMapping("getCountData/{begin}/{end}/{type}")
	
	public Result getCountData(@PathVariable String begin,
			                   @PathVariable String end,
			                   @PathVariable String type) {
		//因为返回数据中 有两个数组，比较方便,所以使用map
		
	Map<String,Object> map=	dailyService.getCountData(begin,end,type);
		
								return Result.ok().data(map);
		
		
		
	}
	
	
	

}

