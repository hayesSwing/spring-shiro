package com.xiangzi.shiro.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultShiroSessionDao extends CachingSessionDAO {

	protected Logger logger = LoggerFactory.getLogger(DefaultShiroSessionDao.class);

	public DefaultShiroSessionDao() {

	}

	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		return sessionId;
	}

	protected Session doReadSession(Serializable sessionId) {
		// should never execute because this implementation relies on parent
		// class to access cache, which is where all sessions reside - it is the
		// cache implementation that determines if the cache is memory only or
		// disk-persistent, etc.
//		logger.debug("doReadSession sessionId:" + sessionId);
//		return null; 
		
		Session session = getCachedSession(sessionId);
		logger.debug("doReadSession sessionId:" + sessionId + ",session:" + session);
		return session;
	}

	protected void doUpdate(Session session) {
		// does nothing - parent class persists to cache.
		logger.debug("doUpdate session.............");
	}

	protected void doDelete(Session session) {
		// does nothing - parent class removes from cache.
		logger.debug("doDelete session.............");
	}

}
