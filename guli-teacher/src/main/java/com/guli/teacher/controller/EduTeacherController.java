package com.guli.teacher.controller;


import static org.junit.Assert.assertFalse;

import java.util.List;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.result.Result;
import com.guli.common.result.ResultCode;
import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.entity.query.TeacherQuery;
import com.guli.teacher.exception.EduException;
import com.guli.teacher.service.EduTeacherService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-01
 */
@RestController
@RequestMapping("/teacher")
@Api(tags = "讲师管理")

@CrossOrigin //在跨域的类上加上@CrossOrigin


public class EduTeacherController {
	
	@Autowired
	
	 EduTeacherService eduTeacherServiceimpl;
	@ApiOperation(value = "所有讲师列表")
	@GetMapping("list")
	public Result list(){
		try {
			List<EduTeacher> list = eduTeacherServiceimpl.list(null);
			list.forEach(System.err::println);
			return  Result.ok().data("items",list);
		} catch (Exception e) {
			e.printStackTrace();
			
			return Result.error();
		}
		
		
				
	}
	
	@ApiOperation(value = "讲师删除")
	@DeleteMapping("{id}") //占位符
	public  Result  delete(
		@ApiParam(name = "id",value = "讲师Id",required = true)
		@PathVariable(value = "id") String id ) {
		
		
		try {
			eduTeacherServiceimpl.removeById(id);
			
			return  Result.ok();
		} catch (Exception e) {
			
			e.printStackTrace();
			return Result.error();
		}
		
		
	}
	
	
	@ApiOperation(value = "讲师分页列表")
	
	@GetMapping("/{page}/{limit}")
	public Result selectTeacherBypage(
			@ApiParam(name ="page",value = "当前页",required = true)
			@PathVariable(value ="page") Integer page,
			@ApiParam(name="limit",value = "每页显示的数据",required = true)
			@PathVariable(value ="limit") Integer limit) {
		try {
			
			Page<EduTeacher> teacherPage = new Page<EduTeacher>(page,limit);	
			eduTeacherServiceimpl.page(teacherPage, null);
				
			List<EduTeacher> teacherList= teacherPage.getRecords();
			System.out.println(teacherList.toString());
			long total= teacherPage.getTotal();
			
			return Result.ok().data("total", total).data("row",teacherList);
		} catch (Exception e) {
			
			e.printStackTrace();
			return Result.error();
			
		}
	}
	
	
	@ApiOperation(value = "讲师分页条件查询")
	
	@PostMapping("/{page}/{limit}")
	public Result selectTeacherBypageAndWrapper(
			@ApiParam(name ="page",value = "当前页",required = true)
			@PathVariable(value ="page") Integer page,
			@ApiParam(name="limit",value = "每页显示的记录数",required = true)
			@PathVariable Integer limit,
			@RequestBody  TeacherQuery query
			
			) {
		  
		try {
			
			Page<EduTeacher> teacherPage = new Page<EduTeacher>(page,limit);	
			eduTeacherServiceimpl.pageQuery(teacherPage, query);
            List<EduTeacher> teacherList= teacherPage.getRecords();
			long total= teacherPage.getTotal();
			System.out.println(query.toString());
			System.out.println(teacherPage.toString());
			System.out.println(total);
			teacherList.forEach(System.err::println);
			return Result.ok().data("total", total).data("row",teacherList);
		} catch (Exception e) {
			
			e.printStackTrace();
			return Result.error();
			
		}
	}
	
	/**
	 * 新增
	 */
	
	@ApiOperation(value = "保存讲师对象")
	@PostMapping("save")
	
	public Result saveTeacher(@RequestBody EduTeacher teacher) {
		try {
			eduTeacherServiceimpl.save(teacher);
			return Result.ok();
		} catch (Exception e) {
			
			e.printStackTrace();
			return Result.error();
		}
		
	}
	
	
	/**
	 * 根据ID查询
	 */
	
	@ApiOperation(value = "根据IDx查询")
	@GetMapping("{id}")
	
	public Result selectTeacherById(
			@ApiParam(name = "id" ,value = "讲师Id",required = true)
			@PathVariable String id) {
		//当我们的业务被非法参数操作时，我们可以自定义异常(业务异常)
//		 EduTeacher  teacher = eduTeacherServiceimpl.getById(id);
//		    if( teacher==null) {
//		    	throw new EduException(ResultCode.EDU_ID_ERROR,"没有此讲师信息");
//		    }
		try {
			       
			 EduTeacher  teacher = eduTeacherServiceimpl.getById(id);
					return  Result.ok().data("teacher",teacher);
		} catch (Exception e) {
		
			e.printStackTrace();
			return  Result.error();
		}
				
	
	}
	
	/**
	 * 修改
	 */
	
	@ApiOperation(value = "根据id修改讲师信息")
	@PutMapping("update")
	
	public Result updateTeacher(@RequestBody EduTeacher teacher) {
		
		 try {
			eduTeacherServiceimpl.updateById(teacher);
			
			
			return Result.ok().data("teacher",teacher);
		} catch (Exception e) {
			
			e.printStackTrace();
			
			return Result.error();
		}
		
	}
	
	
	
	
	
	

}

