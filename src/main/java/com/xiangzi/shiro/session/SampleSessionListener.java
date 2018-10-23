package com.xiangzi.shiro.session;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleSessionListener implements SessionListener {
	// public class SampleSessionListener extends SessionListenerAdapter {

	private static Logger logger = LoggerFactory.getLogger(SampleSessionListener.class);

	private CacheManager cacheManager;
	private DefaultShiroSessionDao sessionDAO;

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public DefaultShiroSessionDao getSessionDAO() {
		return sessionDAO;
	}

	public void setSessionDAO(DefaultShiroSessionDao sessionDAO) {
		this.sessionDAO = sessionDAO;
	}

	@Override
	public void onStart(Session session) {
		// TODO Auto-generated method stub

		logger.debug("onStart SessionId:" + session.getId());

	}

	@Override
	public void onStop(Session session) {
		// TODO Auto-generated method stub

		logger.debug("onStop SessionId:" + session.getId());

	}

	@Override
	public void onExpiration(Session session) {
		// TODO Auto-generated method stub

		logger.debug("onExpiration SessionId:" + session.getId());

		cacheManager.getCache(sessionDAO.getActiveSessionsCacheName()).remove(session.getId());

	}

}
