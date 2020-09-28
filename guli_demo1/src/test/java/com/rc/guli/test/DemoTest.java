package com.rc.guli.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sound.midi.SysexMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rc.guli.entity.User;
import com.rc.guli.mapper.UserMapper;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DemoTest {
	@Autowired
	
	UserMapper  userMapper;  
	
	
	
	/**
	 * 用户添加
	 * 1在企业中经常出现创建时间和修改时间，每次在插入和修改数据都要赋值这个字段，那么mp帮我们自动添加
	 * 2修改的时候容易数据的混乱,数据不准确：
	 *        锁：
	 *   悲观锁：它认为你是不合法的操作，每一次操作都要加锁，效率太低
	 *  乐观锁：认为你是操作是合法的，每一次操作都不加锁，数据安全太差
	 *  在表中添加一个version字段，在修改的数据的时候，根据版本号修改
	 *  
	 *  在修改数据的时候根据版本的号来修改
	 */
	@Test
	public void insertUser() {
		User user = new User();
		
		user.setAge(22);
		user.setName("周公瑾");
		
		userMapper.insert(user);
		//默认帮我们生成主键策略
		//默认帮我们生成主键回显
	System.err.println(user);
		
	}
	
	/**
	 * Mp_修改用户
	 */
	
	@Test
	public void updateUser() {
//		
//		User user = new User();
//		user.setId(1266730404394745857L);
//		user.setName("小桥");
//		
		User user = userMapper.selectById(1266733342081056769L);
		user.setName("鲁肃");
		
		userMapper.updateById(user);
	}
	
	
	@Test
	 public void selectUserTest() {
		
//		User user = userMapper.selectById(1266733342081056769L);
//		System.err.println(user.toString());
		List<Long> list = new ArrayList<Long>();
		list.add(1L);
		list.add(2L);
		list.add(3L);
		list.add(4L);
		List<User>  users = userMapper.selectBatchIds(list);
		
		users.forEach(System.err::println);
	}
	
	/**
	 * 条件查询
	 */
	@Test
	
	public void selectByWrapper() {
		
		QueryWrapper<User> wrapper = new QueryWrapper<User>();
		wrapper.gt("age", 18);
		wrapper.like("name", "德");
		List<User> users = userMapper.selectList(wrapper);
	    users.forEach(System.err::println);
		
		
	}
	
	
	
	/**
	 * 分页查询
	 */
	
	@Test
	
	public void selectByPage() {
		
		Page<User> page = new Page<User>(1,3);
		//设置分页的起始值和每页显示的记录数
		userMapper.selectPage(page, null);
		//第二种方式
//		IPage<Map<String, Object>> MapsPage = userMapper.selectMapsPage(page, null);
//		List<Map<String, Object>> records2 = MapsPage.getRecords();
		//显示分页的数据列表
		List<User> records = page.getRecords();
		records.forEach(System.err::println);
		System.err.println("当前页码：" +page.getCurrent());
		System.err.println("每页显示的记录数：" +page.getSize());
		System.err.println("总页数：" +page.getPages());
		System.err.println("总记录数：" +page.getTotal());
		System.err.println("是否有下一页：" +page.hasNext());
		System.err.println("是否有上一页：" +page.hasPrevious());
	}
	
	
	/**
	 * 删除用户
	 * 1创建字段
	 * 2实体类注解 @TableLogic
	 * 3在properties文件中配置删除和不删除的值
	 * 4在Config文件中配置插件
	 */
	
	@Test
	
	public void deleteUserTest() {
		
		userMapper.deleteById(2L);
	}
	
	

}
