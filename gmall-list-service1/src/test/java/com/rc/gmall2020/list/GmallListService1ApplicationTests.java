package com.rc.gmall2020.list;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.Search.Builder;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallListService1ApplicationTests {
	@Autowired
	private JestClient jestClient;

	@Test
	public void contextLoads() {
		
		/**
		 * 1定义dsl语句
		 * 2定义执行的动作
		 * 3执行动作
		 * 4获取执行后的结果集
		 */
	}
	
	@Test
	public void testBs() throws IOException {
		
		String query="{\"query\": {\n" + 
				"  \"term\": {\n" + 
				"  \"actorList.name\": \"张译\"\n" + 
				"  }\n" + 
				"}\n" + 
				"  \n" + 
				"}";
		//查询Get
		
		Search search = new Search.Builder(query).addIndex("movie_chn").addType("movie").build();
		//执行动作
		SearchResult searchResult = jestClient.execute(search);
		//获取数据
		
		List<Hit<Map, Void>> hits = searchResult.getHits(Map.class);
		//循环遍历集合
		for (Hit<Map, Void> hit : hits) {
			
			Map source = hit.source;
			System.out.println(source.get("name")); //红海行动
			
		}
		
		
	}

}
