package com.rc.gmall2020.list.service.impl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.rc.gmall2020.bean.SkuLsInfo;
import com.rc.gmall2020.bean.SkuLsResult;
import com.rc.gmall2020.bean.SkuParams;
import com.rc.gmall2020.service.ListService;
import com.rc.gmall2020.util.config.RedisUtil;

import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.core.Update;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation.Entry;
import redis.clients.jedis.Jedis;
@Service
public class ListServiceImpl implements ListService {
    @Autowired
	private JestClient jestClient;
    
    @Autowired
    private RedisUtil redisUtil; 
    
    private static final String BS_INDEX="gmall";
    
    private static final String BS_TYPE="SkuInfo";
	public void saveSkuLsInfo(SkuLsInfo skuLsInfo) {
		/**
		 * 1定义动作
		 * 2执行动作
		 * 3
		 */
		
		Index index=new Index.Builder(skuLsInfo).index(BS_INDEX).type(BS_TYPE).id(skuLsInfo.getId()).build();
		try {
			jestClient.execute(index);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public SkuLsResult search(SkuParams skuParams) {
		/**
		 * 1定义dsl语句
		 * 2定义动作
		 * 3执行动作
		 * 4获取结果集
		 */
		String query = makeQueryStringForSearch(skuParams);
		Search search = new Search.Builder(query).addIndex(BS_INDEX).addType(BS_TYPE).build();
		SearchResult searchResult=null;
		try {
			 searchResult = jestClient.execute(search);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SkuLsResult skuLsResult =makeResultForSearch(searchResult,skuParams);
				return skuLsResult;
	}
	//设置返回结果
	/**
	 * 
	 * @param searchResult 通过dsl语句查询出来的
	 * @param skuParams 入列参数
	 * @return
	 */
	private SkuLsResult makeResultForSearch(SearchResult searchResult, SkuParams skuParams) {
		SkuLsResult skuLsResult = new SkuLsResult();
		//声明一个集合来存储SkulsInfo数据
		List<SkuLsInfo> skuLsInfoList = new ArrayList<>();
		List<Hit<SkuLsInfo, Void>> hits = searchResult.getHits(SkuLsInfo.class);
		//循环遍历
		for (Hit<SkuLsInfo, Void> hit : hits) {
			SkuLsInfo skuLsInfo = hit.source;
			
			//Map<String, List<String>> highlight = highlight2;
			Map<String, List<String>> highlight = hit.highlight;
			if(hit.highlight!=null && hit. highlight.size()>0) {
				
				List<String> list = highlight .get("skuName");
				//高亮的SkuName;
				String skuNameHI = list.get(0);//获取集合中的第一个数据
				skuLsInfo.setSkuName(skuNameHI);
			}
			skuLsInfoList.add(skuLsInfo);
			
			
		}
		//给集合赋值
		skuLsResult.setSkuLsInfoList(skuLsInfoList);
		//给total赋值
		skuLsResult.setTotal( searchResult.getTotal());
		//设置总页数
		//long totalPage = searchResult.getTotal()%skuParams.getPageSize()==0?searchResult.getTotal()/skuParams.getPageSize():searchResult.getTotal()/skuParams.getPageSize()+1;
		long totalPage=(searchResult.getTotal()+skuParams.getPageSize()-1)/skuParams.getPageSize();
		skuLsResult.setTotalPage(totalPage);
		//声明一个集合
		List<String> attrValueIdList = new ArrayList<>();
		//获取平台属性值id;
		MetricAggregation aggregations = searchResult.getAggregations();
		TermsAggregation termsAggregation = aggregations.getTermsAggregation("groupby_attr");
		List<TermsAggregation.Entry> buckets = termsAggregation.getBuckets();
		for (TermsAggregation.Entry entry : buckets) {
			String valueId = entry.getKey();
			
			attrValueIdList.add(valueId);
			
			
		}
		
		skuLsResult.setAttrValueIdList(attrValueIdList);
		return skuLsResult;
	}
	//手写dsl语句
	private String makeQueryStringForSearch(SkuParams skuParams) {
		//定义一个查询器
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		//创建bool
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		//判断keyword;
		if(skuParams.getKeyword()!=null && skuParams.getKeyword().length()>0) {
			MatchQueryBuilder match = new MatchQueryBuilder("skuName",skuParams.getKeyword() );
			//创建一个must;
			boolQuery.must(match);
			
			//设置高亮
			HighlightBuilder highlighter = searchSourceBuilder.highlighter();
			//设置高亮的规则
			highlighter.field("skuName");
			highlighter.preTags("<span style=color:red>");
			highlighter.postTags("</span>");
			
			//将设置好的高亮对象放入到查询器中
			searchSourceBuilder.highlight(highlighter);
			
			
		}
		//判断平台属性值id
		if(skuParams.getValueId()!=null && skuParams.getValueId().length>0) {
			//循环
			for(String valueId:skuParams.getValueId()) {
				//创建term
				TermQueryBuilder term = new TermQueryBuilder("skuAttrValueList.valueId", valueId);
				
				//创建一个filter，并添加term
				boolQuery.filter(term);
				
			}
			
		}
		//判断三级分类id
		if(skuParams.getCatalog3Id()!=null && skuParams.getCatalog3Id().length()>0) {
			//创建term
			TermQueryBuilder term = new TermQueryBuilder("catalog3Id", skuParams.getCatalog3Id());
			
			//创建一个filter，并添加term
			boolQuery.filter(term);
		}
	
		//query
		searchSourceBuilder.query(boolQuery);
		
		//设置分页
		//from从第几条开始查询
		int from = (skuParams.getPageNo()-1)*skuParams.getPageSize();
		searchSourceBuilder.from(from);
		//size每页显示的条数
		searchSourceBuilder.size(skuParams.getPageSize());
		//设置排序
		searchSourceBuilder.sort("hotScore", SortOrder.DESC);
		
		//聚合
		//创建一个对象 aggs：——terms
		TermsBuilder terms = AggregationBuilders.terms("groupby_attr");
		terms.field("skuAttrValueList.valueId");
		//aggs 放入查询器
		 searchSourceBuilder.aggregation(terms);
		 String query = searchSourceBuilder.toString();
		 System.out.println("query:"+query);
		return query;
	}
	@Override
	public void incrHotScore(String skuId) {
		//获取jedis
		Jedis jedis = redisUtil.getJedis();
		//定义一个key
		String hotkey = "hotScore";
		//保存数据 skuId : 33
		Double count = jedis.zincrby(hotkey,1,"skuId:"+skuId );
		
		//按照一定的规则更新es
		
		if(count%10==0) {
			//则更新一次
			//更新语句
			updateHotScore(skuId,Math.round(count));
		}
		
		
		
		
		
		
		
		
		
	}
	//更新
	private void updateHotScore(String skuId, long hotScore) {
		/**
		 * 1编写dsl语句
		 * 2定义动作
		 * 3执行
		 */
		//1编写dsl语句
		String  upd="{\"doc\": {\r\n" + 
						"  \"hotScore\":"+hotScore+"\r\n" + 
						"}\r\n" + 
						"  \r\n" + 
						"}\r\n" + 
						"";
		//2定义动作
		Update build = new Update.Builder(upd).index(BS_INDEX).type(BS_TYPE).id(skuId).build();
		
		 //3执行
		try {
			jestClient.execute(build);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

}
