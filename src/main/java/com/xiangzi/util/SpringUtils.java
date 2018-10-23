package com.xiangzi.util;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Lazy(false)
public class SpringUtils implements ApplicationContextAware, DisposableBean {
	
	/** applicationContext */
	private static ApplicationContext applicationContext;

	private SpringUtils() {
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringUtils.applicationContext = applicationContext;
	}

	@Override
	public void destroy() throws Exception {
		applicationContext = null;
	}

	/**
	 * 获取applicationContext
	 *
	 * @return applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取实例
	 *
	 * @param name
	 *            Bean名称
	 * @return 实例
	 */
	public static Object getBean(String name) {
		Assert.hasText(name);
		return applicationContext.getBean(name);
	}

	/**
	 * 获取实例
	 *
	 * @param name
	 *            Bean名称
	 * @param type
	 *            Bean类型
	 * @return 实例
	 */
	public static <T> T getBean(String name, Class<T> type) {
		Assert.hasText(name);
		Assert.notNull(type);
		return applicationContext.getBean(name, type);
	}
		
	/**
	 * 获取实例
	 *
	 * @param type
	 *            Bean类型
	 * @return 实例
	 */
	public static <T> T getBean(Class<T> type) {
		Assert.notNull(type);
		return applicationContext.getBean(type);
	}
}
