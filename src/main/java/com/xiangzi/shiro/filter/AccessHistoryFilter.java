package com.xiangzi.shiro.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiangzi.Global;
import com.xiangzi.util.JSONUtil;

public class AccessHistoryFilter extends PassThruAuthenticationFilter {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		boolean accessDenied;
		String requestType = httpServletRequest.getHeader("X-Requested-With");

		logger.debug("AccessHistoryFilter --- requestType:" + requestType);

		if (isLoginRequest(request, response)) {
			// ajax请求
			if (requestType != null && requestType.equalsIgnoreCase("XMLHttpRequest")) {
				// saveRequestAndRedirectToLogin(request, response);
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(JSONUtil.toJSONString(Global.AJAX_FAIL));
				response.getWriter().flush();
				response.getWriter().close();
				accessDenied = false;
			} else {
				accessDenied = true;
			}
		} else {
			saveRequestAndRedirectToLogin(request, response);
			accessDenied = false;
		}

		logger.debug("AccessHistoryFilter --- accessDenied:" + accessDenied + " --- " + httpServletRequest.getRequestURL());

		return accessDenied;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		// TODO Auto-generated method stub

		boolean accessAllowed = super.isAccessAllowed(request, response, mappedValue);

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		logger.debug("AccessHistoryFilter --- isAccessAllowed:" + accessAllowed + " --- "
				+ httpServletRequest.getRequestURL());

		return accessAllowed;
	}

	@Override
	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		super.issueSuccessRedirect(request, response);

		logger.debug("AccessHistoryFilter --- issueSuccessRedirect ... ");

	}

	@Override
	public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		// TODO Auto-generated method stub

		logger.debug("AccessHistoryFilter --- onPreHandle ... ");

		return super.onPreHandle(request, response, mappedValue);
	}

	@Override
	protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
		// TODO Auto-generated method stub

		boolean loginRequset = super.isLoginRequest(request, response);

		String requestURI = getPathWithinApplication(request);

		logger.debug("AccessHistoryFilter --- isLoginRequest:" + loginRequset + ",requestURI:" + requestURI);

		return loginRequset;
	}

	@Override
	public Filter processPathConfig(String path, String config) {
		// TODO Auto-generated method stub
		return super.processPathConfig(path, config);
	}

	@Override
	public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterCompletion(request, response, exception);

		logger.debug("AccessHistoryFilter --- afterCompletion ... ");

	}

	@Override
	public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		try {
			super.doFilterInternal(request, response, chain);
		} catch (ServletException servletException) {
			// logger.error("AccessHistoryFilter --- doFilterInternal ...
			// servletException:"
			// + servletException.getMessage());
			// logger.error("AccessHistoryFilter --- doFilterInternal ...
			// servletException:",
			// servletException);

			// org.apache.shiro.session.UnknownSessionException: There is no
			// session with id [d961a4198f7e4323817f36d41e3c09cd]
			// java.lang.IllegalStateException:
			// org.apache.shiro.session.UnknownSessionException: There is no
			// session with id [
			if (StringUtils.startsWithIgnoreCase(servletException.getMessage(),
					"java.lang.IllegalStateException: org.apache.shiro.session.UnknownSessionException: There is no session with")) {
				logger.error("AccessHistoryFilter --- doFilterInternal ... servletException:"
						+ servletException.getMessage());
			} else {
				throw servletException;
			}
		} catch (Exception ex) {
			logger.error("AccessHistoryFilter --- doFilterInternal ... Exception:", ex);
		}

		logger.debug("AccessHistoryFilter --- doFilterInternal ... ");

	}

	private boolean isAjax(HttpServletRequest request) {  
        String header = request.getHeader("x-requested-with");  
        if (null != header && "XMLHttpRequest".endsWith(header)) {  
            return true;  
        }  
        return false;  
    }  
	
}
