package com.rc.gmall2020.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rc.gmall2020.util.config.RedisUtil;

import redis.clients.jedis.Jedis;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallServiceUtilApplicationTests {

	@Test
	public void contextLoads() {
		RedisUtil redisUtil= new RedisUtil();
		Jedis jedis = redisUtil.getJedis();
		System.out.println("jedis"+jedis);
		jedis.set("ok","nice");
		jedis.close();
		
		
	}

}
