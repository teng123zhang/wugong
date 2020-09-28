package com.guli.order.service;

import com.guli.order.entity.EduOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-28
 */
public interface EduOrderService extends IService<EduOrder> {

	String createOrders(String courseId, String memberIdByJwtToken);

}
