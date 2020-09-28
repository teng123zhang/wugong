package com.guli.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.guli.order.entity.CourseWebVoOrder;
@Component
@FeignClient("guli-teacher")
public interface CourseClient {
	
	@PostMapping(value = "/frontCourse/getCourseInfoOrder/{id}")
		
	public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id);

}
