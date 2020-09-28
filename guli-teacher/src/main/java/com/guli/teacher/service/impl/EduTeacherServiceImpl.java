package com.guli.teacher.service.impl;

import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.entity.query.TeacherQuery;
import com.guli.teacher.mapper.EduTeacherMapper;
import com.guli.teacher.service.EduTeacherService;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-01
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {
	
	public void pageQuery(Page<EduTeacher> teacherPage, TeacherQuery query) {
		if(query==null) {
			IPage<EduTeacher> selectPage = baseMapper.selectPage(teacherPage,null);
			System.out.println(selectPage.toString());
		}
		
		//获取对象属性
		String name= query.getName();
		Integer level= query.getLevel();
		Date gmtCreate = query.getGmtCreate();
		
		
		Date  gmtModified= query.getGmtModified();
		
		//创建一个wrapper
		QueryWrapper<EduTeacher> wrapper= new QueryWrapper<EduTeacher>();
		//判断对象是否存在
		if(!StringUtils.isEmpty(name)) {
			//如果存在在加入条件
			wrapper.like("name", name);
		}
		
		if(null!=level) {
			//如果存在在加入条件
			wrapper.eq("level",level);
		}
		
		if(gmtCreate!=null) {
			//如果存在在加入条件
			wrapper.ge("gmt_Create",gmtCreate);
		}
		
		if(gmtModified!=null) {
			//如果存在在加入条件
			wrapper.le("gmt_Modified",gmtModified);
		}
		
		
		
		baseMapper.selectPage(teacherPage,wrapper);
	}

	/**
	 * 前台系统讲师分页的方法
	 */
	

	@Override
	public Map<String, Object> getTeacherAllFront(Page<EduTeacher> pageTeacher) {
		baseMapper.selectPage(pageTeacher, null);
		Integer current = (int) pageTeacher.getCurrent();//当前页数-
		Integer pages =(int) pageTeacher.getPages();//总页数
		Integer size = (int)pageTeacher.getSize();//每页显示的总记录数
		Integer total = (int)pageTeacher.getTotal();//总纪录数
		List<EduTeacher> records = pageTeacher.getRecords();//每页数据的list集合
		boolean hasPrevious = pageTeacher.hasPrevious();//上一页
		boolean hasNext = pageTeacher.hasNext();//下一页
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("current",current);
		map.put("pages",pages);
		map.put("size",size);
		map.put("total",total);
		map.put("records",records);
		map.put("hasPrevious",hasPrevious);
		map.put("hasNext",hasNext);
		return map;
	}

}
