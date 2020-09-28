package com.guli.teacher.service.impl;

import com.guli.teacher.entity.EduSubject;
import com.guli.teacher.entity.vo.OneSubject;
import com.guli.teacher.entity.vo.TwoSubject;
import com.guli.teacher.mapper.EduSubjectMapper;
import com.guli.teacher.service.EduSubjectService;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-12
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
          
	@Override
	public List<String> importEXCL(MultipartFile file) {
		
		//存储错误信息
		
		List<String> meg= new ArrayList<String>();
	   try {
		   //1获取文件的流
		InputStream inputStream = file.getInputStream();
		//2根据这流创建一个workBook
		Workbook workbook = new XSSFWorkbook(inputStream);
		//3获取Sheet，getSheet(0)
		 Sheet sheet = workbook.getSheetAt(0);
		//4根据sheet获取行数
		 
		 int lastRowNum = sheet.getLastRowNum();
		 if(lastRowNum<=1) {
			meg.add("请填写数据") ;
			return meg;
		 }
		//5遍历行数 
		for (int rowNum=1; rowNum<lastRowNum;rowNum++) {
			  Row row = sheet.getRow(rowNum);
			  //6获取每一行的第一列，一级分类
			  Cell cell = row.getCell(0);
			  if(cell==null) {
				  meg.add("第"+rowNum+"行第1列为空") ;
					continue;
			  }
			  
			  
			  String cellValue = cell.getStringCellValue();
			  if(StringUtils.isEmpty(cellValue)) {
				  meg.add("第"+rowNum+"行第1列数据为空") ;
				  continue;
			  }
			  
				
				//7判断列时候存在，存在获取类的数据
			  
			  EduSubject subject=this.selectSubjectByName(cellValue);
			  String pid=null;
			//8把第一列的数据保存到(一级分类)保存到数据库中
				if(subject==null) {
					EduSubject su = new EduSubject();
					su.setTitle(cellValue);
					su.setParentId("0");
					su.setSort(0);
					//9在保存之前判断此一级分类是否存在，如果存在，不在添加，如果不存在在保存
					baseMapper.insert(su);
					pid=su.getId();
				}else {
					pid=subject.getId();
				}
				
				
				
				
				//10在获取每一行的第二列
				Cell cell2 = row.getCell(1);
				if(cell2==null) {
					//11在获取第二列的数据（二级分类）
					meg.add("第"+rowNum+"行第2列为空") ;
					continue;
					
				}
				
				String stringCellValue = cell2.getStringCellValue();
				if(StringUtils.isEmpty(stringCellValue)) {
					meg.add("第"+rowNum+"行第2列的数据为空") ;
					continue;
					
				}
				
				
				
				//12判断此一级分类中是否存在此二级分类
				
			EduSubject subject2=	this.selectByNameAndParentId( stringCellValue ,pid);
				
				//13如果此一级分类中有此二级分类，不保存
			if(subject2==null) {
				//14如果没有保存二级分类
				
				EduSubject su = new EduSubject();
				su.setTitle(stringCellValue );
				su.setParentId(pid);
				su.setSort(0);
				baseMapper.insert(su);
				
			}
			
				
		}
		
		
		
		
	
		
		
		
		
	} catch (IOException e) {
		
		e.printStackTrace();
	}
		return meg;
	}
	/**
	 * 根据二级分类名称和parentid查询是否存在subject
	 * @param stringCellValue
	 * @param pid
	 * @return
	 */
private EduSubject selectByNameAndParentId(String stringCellValue, String pid) {
	QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
	queryWrapper.eq("title",stringCellValue);
	queryWrapper.eq("parent_id", pid);
	EduSubject subject = baseMapper.selectOne(queryWrapper);
		return subject;
	}
/**
 * 根据课程一级分类的名字查询是否存在
 * @param cellValue
 * @return
 */
	private EduSubject selectSubjectByName(String cellValue) {
		QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("title", cellValue);
		queryWrapper.eq("parent_id", 0);
		EduSubject subject = baseMapper.selectOne(queryWrapper);
		return subject;
	}
@Override
public List<OneSubject> getTree() {
	//1创建一个集合存放OneSubject
	List<OneSubject> oneSubjectList= new ArrayList<OneSubject>();
	//2获取一级分类列表
	QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<EduSubject>();
	queryWrapper.eq("parent_id", 0);
	List<EduSubject> eduSubjectList = baseMapper.selectList(queryWrapper);
	
	//3遍历一级分类列表 
	for (EduSubject subject : eduSubjectList) {
		
		//4一级分类数据复制到OneSubject
		OneSubject oneSubject = new OneSubject();
		BeanUtils.copyProperties(subject,  oneSubject);
		//5根据每一个一级分类获取二级分类的列表
		QueryWrapper<EduSubject> wrapper = new QueryWrapper<EduSubject>();
		wrapper.eq("parent_id", oneSubject.getId());
		List<EduSubject> eduSubjects = baseMapper.selectList(wrapper);
		// 6遍历二级分类列表
		for (EduSubject su : eduSubjects) {
			//7把二级分类的对象复制到TwoSubject
			TwoSubject twoSubject = new TwoSubject();
			BeanUtils.copyProperties(su, twoSubject);
			//8把TwoSubject添加到OneSubject的集合中
			 oneSubject.getChildren().add(twoSubject);
		}
		
		//9把OneSubjct添加到集合中，然后返回集合
		oneSubjectList.add(oneSubject);
	}
	
	return oneSubjectList;
}
@Override
public boolean deleteById(String id) {
	//根据ID 查询数据库中是否存在此ID为ParentId(二级分类)
	QueryWrapper<EduSubject> wrapper =new QueryWrapper<EduSubject>();
	wrapper.eq("parent_id", id);
	
	List<EduSubject> selectList = baseMapper.selectList(wrapper);
	//如果有,返回false
	if(selectList.size()!=0) {
		return false;
	}
	//如果没有直接删除并且返回true
	int i = baseMapper.deleteById(id);
	
	
	
	
	return i==1;
}
@Override
public Boolean saveLevelOne(EduSubject subject) {
	//根据title 判断一下一级分类是否存在
	EduSubject eduSubject = this.selectSubjectByName(subject.getTitle());
	//存在返回false
	if(eduSubject!=null) {
		return false;
	}
	
	//不存在保存到数据库并返回true
	subject.setSort(0);
	int insert = baseMapper.insert(subject);
	return insert==1;
}
@Override
public Boolean saveLevelTwo(EduSubject subject) {
	//判断此一级分类是否有二级分类的名字
	EduSubject sub = this.selectByNameAndParentId(subject.getTitle(), subject.getParentId());
	if(sub!=null) {
		//存在
		return false;
				
		
	}
	        //添加
			int insert = baseMapper.insert(subject);
	         return insert ==1;
}

}
