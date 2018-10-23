package com.xiangzi.cache;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public interface CacheService extends Serializable {

	void set(String tenantId, String key, Object value, int secondsToExpire) throws Exception;

	Object get(String tenantId, String key) throws Exception;

	Object del(String tenantId, String key) throws Exception;

	Object getQueue(String tenantId, String key) throws Exception;

	Integer getQueueSize(String tenantId, String key) throws Exception;

	void setQueue(String tenantId, String key, Boolean clearFlag, Collection value, int secondsToExpire) throws Exception;

	void setQueue(String tenantId, String key, Object value, int secondsToExpire) throws Exception;

	void clear(String tenantId) throws Exception;

	Set<String> keys(String tenantId) throws Exception;

	int size(String tenantId) throws Exception;

	Collection<Object> values(String tenantId) throws Exception;

}
