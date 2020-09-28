package com.guli.statistics.service.impl;

import com.guli.common.result.Result;
import com.guli.statistics.client.UcenterClient;
import com.guli.statistics.entity.Daily;
import com.guli.statistics.mapper.DailyMapper;
import com.guli.statistics.service.DailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-19
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {
  @Autowired
  private UcenterClient ucenterClient; 
	@Override
	public void getDataAdd(String day) {
		
		Result result = ucenterClient.getRegisterNum(day);
		
		//获取返回Result对象
		
		
	Integer registerNum=(Integer)result.getData().get("RegisterNum");
	//先删除在添加钢制数据冗余
		QueryWrapper<Daily> wrapper=new QueryWrapper<Daily>();
		wrapper.eq("date_calculated", day);
	    baseMapper.delete(wrapper);
	Daily daily=new Daily();
	//统计日期
	daily.setDateCalculated(day);
	//注册人数
	daily.setRegisterNum(registerNum);
	daily.setVideoViewNum(RandomUtils.nextInt(100,200));
	daily.setCourseNum(RandomUtils.nextInt(100, 200));
	daily.setLoginNum(RandomUtils.nextInt(100, 200));
	
	baseMapper.insert(daily);
	System.out.println("注册人数为:"+registerNum);
		
		
	}
	@Override
	public Map<String, Object> getCountData(String begin, String end, String type) {
		//1根据时间范围进行数据查询
		QueryWrapper<Daily> queryWrapper = new QueryWrapper<Daily>();
		 queryWrapper.between("date_calculated", begin,end);
		 //查询指定的字段
		 queryWrapper.select("date_calculated",type);
		List<Daily> dailyList = baseMapper.selectList(queryWrapper);
		//2把数据构建成想要的结构,最终要变成两个json数组
		//创建两个list
		//日期的list
		List<String> calculatedList = new ArrayList<String>();
	   //数据的list
		List<Integer> dataList = new ArrayList<Integer>();
		//3像两个list结合中封装数据
		//遍历查询集合
		for(int i=0;i<dailyList.size();i++) {
			//集合每个对象
			Daily staDaily = dailyList.get(i);
			//封装日期结合的数据
			String dateCalculated = staDaily.getDateCalculated();
			 calculatedList.add(dateCalculated);
			 //封装数据部分
			 //判断获取那个统计因子
			 switch (type) {
			  case "register_num":
				
				dataList.add(staDaily.getRegisterNum());
				break;
				
			  case "login_num":
				  dataList.add(staDaily.getLoginNum()); 
				  break;
					
			  case "video_view_num":
				  dataList.add(staDaily.getVideoViewNum()); 
				  break;
				  
					
			  case "course_num":
				  dataList.add(staDaily.getCourseNum()); 
				  break;
			}
			 
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("calculatedList", calculatedList );
		map.put("dataList", dataList);
		return map;
	}

}
