package com.xiangzi.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常信息捕获/统一异常处理器(GlobalExceptionHandler)<br/>
 * 
 * 主要用途：统一处理错误/异常(针对控制层) <br/>
 * 使用说明: <br/>
 * {@link ControllerAdvice}默认扫描路径：例如 com.xiangzi.controller <br/>
 * {@link ExceptionHandler} 指定捕捉异常<br/>
 * {@link ModelAndView} 返回异常信息页(View) <br/>
 * {@link ResponseBody} 返回异常信息(JSON)<br/>
 * <p>
 * 使用@ExceptionHandler时候需注意如下几点：<br/>
 * 1.获取异常：直接在方法参数注入<br/>
 * 2.常见缺点：无法捕捉404类异常<br/>
 * 3.替代方案：实现GlobalErrorController<br/>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Object defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		logger.error("=====================全局异常信息捕获=======================");

		String requestURI = request.getRequestURI();
		logger.error("requestURI:" + requestURI);
		logger.error("访问异常", ex);

		// 1.若为AJAX请求,则返回异常信息(JSON)
		if (isAjaxRequest(request)) {
			Map<String, String> resultMap = new HashMap<>();
			resultMap.put("status", "error");
			resultMap.put("message", "请求失败!" + ex.getMessage());
			resultMap.put("url", requestURI);
			return resultMap;
		}
		// 2.其余请求,则返回指定的异常信息页(View).
		ModelAndView mav = new ModelAndView();
		mav.addObject("msg", ex.getMessage());
		mav.addObject("url", requestURI);
		if (ex instanceof UnauthorizedException) {
			mav.setViewName("error/unauthorized");
		} else {
			mav.setViewName("error/5xx");
		}
		return mav;
	}

	private boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

}
