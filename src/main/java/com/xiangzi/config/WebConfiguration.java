package com.xiangzi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.xiangzi.intercepter.UriInterceptor;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	@Autowired
	private UriInterceptor uriInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(uriInterceptor).addPathPatterns("/admin/**").excludePathPatterns("");
	}
	
}
