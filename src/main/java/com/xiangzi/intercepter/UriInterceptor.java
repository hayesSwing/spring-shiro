package com.xiangzi.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.xiangzi.Global;
import com.xiangzi.util.JSONUtil;

@Component(Global.APP_SIGN + "UriInterceptor")
public class UriInterceptor extends HandlerInterceptorAdapter {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.debug("====== UriInterceptor preHandle ====== IP:" + request.getRemoteAddr());
		logger.debug("====== UriInterceptor preHandle ====== handler:" + handler);
		String requestURI = request.getRequestURI();
		logger.debug("====== UriInterceptor preHandle ====== requestURI:" + requestURI);

		SavedRequest savedRequest = WebUtils.getSavedRequest(request);

		logger.debug("====== UriInterceptor preHandle ====== savedRequest:" + savedRequest);
		logger.debug("====== UriInterceptor preHandle ====== savedRequest.toJSONString:" + JSONUtil.toJSONString(savedRequest));

		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		logger.debug("====== UriInterceptor postHandle ====== URI:" + request.getRequestURI());
		super.postHandle(request, response, handler, modelAndView);

		if (modelAndView != null && SecurityUtils.getSubject().isAuthenticated()) {
			// do something ...

		}

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		logger.debug("====== UriInterceptor afterCompletion ====== ");

	}

}
