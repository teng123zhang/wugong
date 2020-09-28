package com.guli.teacher.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guli.common.result.Result;

@RestController
@RequestMapping("/user")
@CrossOrigin //在跨域的类上加上@CrossOrigin
public class UserController {
	@PostMapping("/login")
	public Result login() {
		
		return Result.ok().data("token","admin");
	}
	
	
	@GetMapping("info")
	
	public Result info() {
		
		return  Result.ok().data("roles", "[\"admin\"]")
				.data("name","admin")
				.data("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b6f4-56703b4acafe.gif");
	}

}
