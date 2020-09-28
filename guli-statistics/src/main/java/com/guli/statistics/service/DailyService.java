package com.guli.statistics.service;

import com.guli.statistics.entity.Daily;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-19
 */
public interface DailyService extends IService<Daily> {
  /**
   * 获取某一天的注册人数,把注册人数添加到统计分析表中
   * @param day
   */
	void getDataAdd(String day);
	/**
	 * 返回统计数据，数组
	 * @param begin
	 * @param end
	 * @param type
	 * @return
	 */

Map<String, Object> getCountData(String begin, String end, String type);

}
