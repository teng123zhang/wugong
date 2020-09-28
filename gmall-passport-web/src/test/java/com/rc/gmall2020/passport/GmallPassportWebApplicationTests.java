package com.rc.gmall2020.passport;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rc.gmall2020.passport.config.JwtUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallPassportWebApplicationTests {

	@Test
	public void contextLoads() {
	}
	
	
	@Test
	public void testJwt() {
		
		String key= "dfrc";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userId",1001);
		map.put("mickName","admin");
		String salt= "192.168.134.130";
		String token = JwtUtil.encode(key, map, salt);
		System.out.println("token"+token);
		
		
		//揭秘token
		Map<String, Object> decode = JwtUtil.decode(token, "123", salt);
		System.out.println(decode);
		
	}

}
