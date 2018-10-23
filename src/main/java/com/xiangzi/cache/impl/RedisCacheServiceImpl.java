package com.xiangzi.cache.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.codec.FstCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiangzi.cache.CacheService;

@Service
public class RedisCacheServiceImpl implements CacheService {

	private static final long serialVersionUID = -397464475490645291L;

	protected static Logger logger = LoggerFactory.getLogger(RedisCacheServiceImpl.class);

	private final String PREFIX = "estore";

	@Autowired
	private RedissonClient redissonClient;

	private Codec codec = new FstCodec();

	private String buildCacheName(String tenantId, String key) {
		return PREFIX + ":" + tenantId + ":" + key;
	}

	public RLock getLock(String tenantId, String lockName) {
		String cacheName = buildCacheName(tenantId, lockName);
		return redissonClient.getLock(cacheName);
	}

	@Override
	public void set(String tenantId, String key, Object value, int secondsToExpire) throws Exception {
		try {
			String cacheName = buildCacheName(tenantId, key);
			RMapCache<String, Object> map = redissonClient.getMapCache(cacheName, codec);
			if (secondsToExpire != 0) {
				map.put(key.toString(), value, secondsToExpire, TimeUnit.SECONDS);
				map.expire(secondsToExpire, TimeUnit.SECONDS);
			} else {
				map.put(key.toString(), value);
			}
		} catch (Exception e) {
			logger.error("RedisCacheServiceImpl.set ERROR", e);
		}
	}

	@Override
	public Object get(String tenantId, String key) throws Exception {
		try {
			String cacheName = buildCacheName(tenantId, key);
			RMapCache<String, Object> map = redissonClient.getMapCache(cacheName, codec);
			if (map.get(key) != null) {
				return map.get(key);
			}
		} catch (Exception e) {
			logger.error("RedisCacheServiceImpl.get ERROR", e);
		}
		return null;
	}

	@Override
	public Object del(String tenantId, String key) throws Exception {
		Object previos = null;
		try {
			String cacheName = buildCacheName(tenantId, key);
			RMapCache<String, Object> map = redissonClient.getMapCache(cacheName, codec);
			previos = map.get(key);
			if (previos != null) {
				map.remove(key);
			}
		} catch (Exception e) {
			logger.error("RedisCacheServiceImpl.del ERROR.", e);
		}
		return previos;
	}

	@Override
	public Set<String> keys(String tenantId) throws Exception {
		HashSet<String> result = new LinkedHashSet<String>();
		try {
			String cacheName = buildCacheName(tenantId, "*");
			Collection<String> keys = redissonClient.getKeys().findKeysByPattern(cacheName);
			if (keys != null && keys.size() > 0) {
				for (String key : keys) {
					String _value = key;
					// 只需要存入Key
					result.add(_value.substring(_value.lastIndexOf(":") + 1));
				}
			}
		} catch (Exception e) {
			logger.error("RedisCacheServiceImpl.keys ERROR.", e);
		}

		if (!CollectionUtils.isEmpty(result)) {
			return Collections.unmodifiableSet(result);
		} else {
			return Collections.emptySet();
		}

	}

	@Override
	public void clear(String tenantId) throws Exception {
		try {
			String cacheName = buildCacheName(tenantId, "*");
			redissonClient.getKeys().deleteByPattern(cacheName);
		} catch (Exception e) {
			logger.error("RedisCacheServiceImpl.clear ERROR.", e);
		}
	}

	@Override
	public int size(String tenantId) throws Exception {
		int size = 0;
		try {
			String cacheName = buildCacheName(tenantId, "*");
			size = redissonClient.getKeys().findKeysByPattern(cacheName).size();
		} catch (Exception e) {
			logger.error("RedisCacheServiceImpl.size ERROR.", e);
		}
		return size;
	}

	@Override
	public Collection<Object> values(String tenantId) throws Exception {
		List<Object> result = null;
		try {
			Set<String> keys = this.keys(tenantId);
			if (keys != null && keys.size() > 0) {
				result = new ArrayList<Object>(keys.size());
				for (String key : keys) {
					Object _value = this.get(tenantId, key);
					if (_value != null) {
						result.add(_value);
					}
				}
			}
		} catch (Exception e) {
			logger.error("RedisCacheServiceImpl.values ERROR.", e);
		}

		if (!CollectionUtils.isEmpty(result)) {
			return Collections.unmodifiableList(result);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public Object getQueue(String tenantId, String key) throws Exception {
		try {
			String cacheName = buildCacheName(tenantId, key);
			RQueue<Object> cacheQueue = redissonClient.getQueue(cacheName, codec);
			Object element = cacheQueue.poll();
			if (element != null) {
				return element;
			}
		} catch (Exception e) {
			logger.error("RedisCacheServiceImpl.getQueue ERROR", e);
		}

		return null;
	}

	@Override
	public Integer getQueueSize(String tenantId, String key) throws Exception {
		try {
			String cacheName = buildCacheName(tenantId, key);
			RQueue<Object> cacheQueue = redissonClient.getQueue(cacheName, codec);
			return cacheQueue.size();
		} catch (Exception e) {
			logger.error("RedisCacheServiceImpl.getQueueSize ERROR", e);
		}
		return 0;
	}

	@Override
	public void setQueue(String tenantId, String key, Boolean clearFlag, Collection value, int secondsToExpire) throws Exception {
		try {
			String cacheName = buildCacheName(tenantId, key);
			RQueue<Object> cacheQueue = redissonClient.getQueue(cacheName, codec);
			clearFlag = (clearFlag == null) ? true : false;
			if (clearFlag) {
				cacheQueue.clear();
			}
			cacheQueue.addAll(value);
		} catch (Exception e) {
			logger.error("RedisCacheServiceImpl.setQueue.collection ERROR", e);
		}
	}

	@Override
	public void setQueue(String tenantId, String key, Object value, int secondsToExpire) throws Exception {
		try {
			String cacheName = buildCacheName(tenantId, key);
			RQueue<Object> cacheQueue = redissonClient.getQueue(cacheName, codec);
			cacheQueue.add(value);
		} catch (Exception e) {
			logger.error("RedisCacheServiceImpl.setQueue.object ERROR", e);
		}
	}

}
