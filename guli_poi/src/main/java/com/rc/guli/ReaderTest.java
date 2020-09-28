package com.rc.guli;

import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class ReaderTest {
	
	/**
	 * 读取EXCEL表格内容
	 * 
	 */
	@Test
	public void reader03() throws Exception{

		//获取一个文件（EXCL）路径，获取流
		
		FileInputStream InputStream = new FileInputStream("D:/0401-03.xls");
		
		//根据这个流创建一个workBook
		
		Workbook workbook = new HSSFWorkbook(InputStream);
		
		//获取此工作表中的sheet
		Sheet sheet = workbook.getSheetAt(0);
		
		//根据这个sheet获取一行
		
		Row row = sheet.getRow(0);
		
		//根据行获取列
		
		Cell cell = row.getCell(0);
		
		
		//根据列获取列中的值
		
		String value = cell.getStringCellValue();
		
		System.err.println(value);
		
		//关闭流
		InputStream.close();
		
		
		
		
		
	}
	
	
	@Test
	public void reader07() throws Exception{

		//获取一个文件（EXCL）路径，获取流
		
		FileInputStream InputStream = new FileInputStream("D:/0401.xlsx");
		
		//根据这个流创建一个workBook
		
		Workbook workbook = new XSSFWorkbook(InputStream);
		
		//获取此工作表中的sheet
		Sheet sheet = workbook.getSheetAt(0);
		
		//根据这个sheet获取一行
		
		Row row = sheet.getRow(0);
		
		//根据行获取列
		
		Cell cell = row.getCell(0);
		
		
		//根据列获取列中的值
		
		String value = cell.getStringCellValue();
		
		System.err.println(value);
		
		//关闭流
		InputStream.close();
		
		
		
		
		
	}

}
