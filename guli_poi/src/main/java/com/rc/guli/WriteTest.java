package com.rc.guli;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class WriteTest {
	/**
	 * poi写入Excel表格 03版本的
	 * @throws FileNotFoundException 
	 */

	@Test
	public void Writer03() throws Exception {
		//1创建工作表
		Workbook workbook = new HSSFWorkbook();
		//2在工作表中创建sheet
		Sheet sheet = workbook.createSheet("会员管理");
		//3在这个sheet中创建行数
		//创建行和列她们的索引是从0开始
		Row row = sheet.createRow(0);
		//4在行数创建列
		Cell cell = row.createCell(0);
		//5在响应的列对应的单元格中设置数据
		cell.setCellValue("人数");
		
		Cell cell2 = row.createCell(1);
		cell2.setCellValue(12345);
		//6写入磁盘
		FileOutputStream outputStream = new FileOutputStream("D:/0401.xls");
		workbook.write(outputStream);
		//7关闭流
		outputStream.close();
		
		
	}
	
	@Test
	public void Writer07() throws Exception {
		//1创建工作表
		Workbook workbook = new XSSFWorkbook();
		//2在工作表中创建sheet
		Sheet sheet = workbook.createSheet("会员管理");
		//3在这个sheet中创建行数
		//创建行和列她们的索引是从0开始
		Row row = sheet.createRow(0);
		//4在行数创建列
		Cell cell = row.createCell(0);
		//5在响应的列对应的单元格中设置数据
		cell.setCellValue("人数");
		
		Cell cell2 = row.createCell(1);
		cell2.setCellValue(12345);
		//6写入磁盘
		FileOutputStream outputStream = new FileOutputStream("D:/0401.xlsx");
		workbook.write(outputStream);
		//7关闭流
		outputStream.close();
		
		
	}
	
	/**
	 * 写入的时候，写的是大数据
	 * 比如： 几万条数据
	 * 时间考虑性能
	 *  
	 *  03版本最多65536数据
	 * 
	 * 
	 */
	
	@Test
	
	public void WriteData03() throws Exception{
		 long begin =  System.currentTimeMillis();
		//1创建工作表
				Workbook workbook = new HSSFWorkbook();
				//2在工作表中创建sheet
				Sheet sheet = workbook.createSheet("会员管理");
				//3在这个sheet中创建行数
				//创建行和列她们的索引是从0开始
				for(int i=0;i<65536;i++) {
					Row row = sheet.createRow(i);
					//4在行数创建列
					Cell cell = row.createCell(0);
					//5在响应的列对应的单元格中设置数据
					cell.setCellValue("人数"+i);
					
				}
				
				
				
				//6写入磁盘
				FileOutputStream outputStream = new FileOutputStream("D:/0401-03.xls");
				workbook.write(outputStream);
				long end = System.currentTimeMillis();
				System.err.println("时间为："+(double)(end-begin)/1000);
				//7关闭流
				outputStream.close();
				
		
		
	}
	
@Test
	
	public void WriteData07() throws Exception{
		 long begin =  System.currentTimeMillis();
		//1创建工作表
				Workbook workbook = new XSSFWorkbook();
				//2在工作表中创建sheet
				Sheet sheet = workbook.createSheet("会员管理");
				//3在这个sheet中创建行数
				//创建行和列她们的索引是从0开始
				for(int i=0;i<165536;i++) {
					Row row = sheet.createRow(i);
					//4在行数创建列
					Cell cell = row.createCell(0);
					//5在响应的列对应的单元格中设置数据
					cell.setCellValue("人数"+i);
					
				}
				
				
				
				//6写入磁盘
				FileOutputStream outputStream = new FileOutputStream("D:/0401-07.xlsx");
				workbook.write(outputStream);
				long end = System.currentTimeMillis();
				System.err.println("时间为："+(double)(end-begin)/1000);
				//7关闭流
				outputStream.close();
				
		
		
	}

@Test

public void SXWriteData07() throws Exception{
	 long begin =  System.currentTimeMillis();
	//1创建工作表
			Workbook workbook = new SXSSFWorkbook();
			//2在工作表中创建sheet
			Sheet sheet = workbook.createSheet("会员管理");
			//3在这个sheet中创建行数
			//创建行和列她们的索引是从0开始
			for(int i=0;i<165536;i++) {
				Row row = sheet.createRow(i);
				//4在行数创建列
				Cell cell = row.createCell(0);
				//5在响应的列对应的单元格中设置数据
				cell.setCellValue("人数"+i);
				
			}
			
			
			
			//6写入磁盘
			FileOutputStream outputStream = new FileOutputStream("D:/0401-07SXSSF.xlsx");
			workbook.write(outputStream);
			long end = System.currentTimeMillis();
			System.err.println("时间为："+(double)(end-begin)/1000);
			//7关闭流
			outputStream.close();
			
			((SXSSFWorkbook)workbook).dispose();
			
	
	
}
}
