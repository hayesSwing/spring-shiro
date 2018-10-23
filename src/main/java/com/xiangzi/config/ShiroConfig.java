package com.xiangzi.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xiangzi.cache.CacheService;
import com.xiangzi.shiro.SampleRealm;
import com.xiangzi.shiro.cache.DefaultShiroCacheManager;
import com.xiangzi.shiro.filter.AccessHistoryFilter;
import com.xiangzi.shiro.session.DefaultShiroSessionDao;
import com.xiangzi.shiro.session.SampleSessionListener;
import com.xiangzi.shiro.session.ShiroSessionManager;

@Configuration
public class ShiroConfig {

	@Autowired
	private CacheService cacheService;

	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		shiroFilterFactoryBean.setLoginUrl("/admin/account/login");
		shiroFilterFactoryBean.setSuccessUrl("/admin/index");// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setUnauthorizedUrl("/admin/403"); // 未授权界面

		Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
		filters.put("authc", new AccessHistoryFilter());// 添加自定义Filter

		// 拦截器
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		// 配置退出过滤器,其中的具体代码Shiro已经替我们实现了
		// filterChainDefinitionMap.put("/logout", "logout");

		// filterChainDefinitionMap.put("/**", "anon");

		filterChainDefinitionMap.put("/admin/account/check", "anon");
		filterChainDefinitionMap.put("/admin/account/forget", "anon");
		filterChainDefinitionMap.put("/admin/account/checkUserName", "anon");
		filterChainDefinitionMap.put("/admin/account/ajaxSendMail", "anon");
		filterChainDefinitionMap.put("/admin/**", "authc");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;

	}

	@Bean
	public CacheManager cacheManager() {
		DefaultShiroCacheManager cacheManager = new DefaultShiroCacheManager();
		cacheManager.setCacheService(cacheService);
		return cacheManager;
	}

	@Bean(name = "defaultAuthorizingRealm")
	public SampleRealm defaultAuthorizingRealm(@Qualifier("cacheManager") CacheManager cacheManager) {
		SampleRealm defaultRealm = new SampleRealm();

		defaultRealm.setCachingEnabled(true);
		defaultRealm.setAuthenticationCachingEnabled(true);
		defaultRealm.setAuthenticationCacheName("shiro:web:authenticationCache");
		defaultRealm.setAuthorizationCachingEnabled(true);
		defaultRealm.setAuthorizationCacheName("shiro:web:authorizationCache");
		defaultRealm.setCacheManager(cacheManager);
		return defaultRealm;
	}

	@Bean(name = "sessionDao")
	public DefaultShiroSessionDao sessionDao() {
		DefaultShiroSessionDao sessionDao = new DefaultShiroSessionDao();
		sessionDao.setSessionIdGenerator(new JavaUuidSessionIdGenerator());// 会话ID生成器
		sessionDao.setActiveSessionsCacheName("shiro:web:activeSessionCache");
		return sessionDao;
	}

	@Bean(name = "securityManager")
	public SecurityManager securityManager(@Qualifier("defaultAuthorizingRealm") SampleRealm defaultAuthorizingRealm, @Qualifier("cacheManager") CacheManager cacheManager,
			@Qualifier("sessionManager") DefaultSessionManager sessionManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

		Collection<Realm> realms = new ArrayList<Realm>();
		realms.add(defaultAuthorizingRealm);
		securityManager.setRealms(realms);

		// securityManager.setRememberMeManager(rememberMeManager);
		securityManager.setCacheManager(cacheManager);
		securityManager.setSessionManager(sessionManager);
		return securityManager;
	}

	@Bean(name = "rememberMeCookie")
	public SimpleCookie rememberMeCookie() {
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		simpleCookie.setHttpOnly(true);
		simpleCookie.setMaxAge(2592000);// 30天
		return simpleCookie;
	}

	// 会话Cookie模板
	@Bean(name = "simpleCookie")
	public SimpleCookie simpleCookie() {
		SimpleCookie simpleCookie = new SimpleCookie();
		simpleCookie.setName("jsid_web");
		simpleCookie.setPath("/");
		simpleCookie.setHttpOnly(true);
		simpleCookie.setMaxAge(-1);
		return simpleCookie;
	}

	// 会话管理器
	@Bean(name = "sessionManager")
	public ShiroSessionManager sessionManager(@Qualifier("cacheManager") CacheManager cacheManager, @Qualifier("sessionDao") DefaultShiroSessionDao sessionDao,
			@Qualifier("simpleCookie") SimpleCookie simpleCookie) {
		ShiroSessionManager defaultSessionManager = new ShiroSessionManager();
		defaultSessionManager.setSessionDAO(sessionDao);
		defaultSessionManager.setSessionIdCookie(simpleCookie);
		defaultSessionManager.setSessionIdUrlRewritingEnabled(false);
		defaultSessionManager.setGlobalSessionTimeout(18000000L);// 5小时
		defaultSessionManager.setSessionValidationSchedulerEnabled(true);
		defaultSessionManager.setDeleteInvalidSessions(true);
		// 会话验证调度器
		ExecutorServiceSessionValidationScheduler sessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();
		sessionValidationScheduler.setInterval(3600000); // 默认是1小时
		sessionValidationScheduler.setSessionManager(defaultSessionManager);
		defaultSessionManager.setSessionValidationScheduler(sessionValidationScheduler);
		List<SessionListener> listeners = new ArrayList<>();
		SampleSessionListener sampleSessionListener = new SampleSessionListener();
		sampleSessionListener.setCacheManager(cacheManager);
		sampleSessionListener.setSessionDAO(sessionDao);
		listeners.add(sampleSessionListener);
		defaultSessionManager.setSessionListeners(listeners);
		return defaultSessionManager;
	}

	/**
	 * Shiro生命周期处理器
	 */
	// @Bean
	// public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
	// return new LifecycleBeanPostProcessor();
	// }

	/**
	 * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
	 * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
	 */
	@Bean
	// @DependsOn({ "lifecycleBeanPostProcessor" })
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

}
