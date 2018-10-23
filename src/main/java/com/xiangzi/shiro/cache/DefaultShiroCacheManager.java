package com.xiangzi.shiro.cache;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.LifecycleUtils;
import org.apache.shiro.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiangzi.cache.CacheService;

public class DefaultShiroCacheManager implements CacheManager, Destroyable {

	protected Logger logger = LoggerFactory.getLogger(DefaultShiroCacheManager.class);

	private final ConcurrentMap<String, Cache> caches;

	/**
	 * Default no-arg constructor that instantiates an internal name-to-cache
	 * {@code ConcurrentMap}.
	 */
	public DefaultShiroCacheManager() {
		this.caches = new ConcurrentHashMap<String, Cache>();
	}

	private CacheService cacheService;

	public CacheService getCacheService() {
		return cacheService;
	}

	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}

	@Override
	public void destroy() throws Exception {
		logger.info("DefaultShiroCacheManager Destroy");
		while (!caches.isEmpty()) {
			for (Cache cache : caches.values()) {
				LifecycleUtils.destroy(cache);
			}
			caches.clear();
		}
	}

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		if (!StringUtils.hasText(name)) {
			throw new IllegalArgumentException("Cache name cannot be null or empty.");
		}
		Cache cache;
		cache = caches.get(name);
		if (cache == null) {
			Cache existing = caches.putIfAbsent(name, new DefaultShiroCache<String, Object>(name, cacheService));
			if (existing != null) {
				cache = existing;
			}
		}
		return cache;
	}

	public String toString() {
		Collection<Cache> values = caches.values();
		StringBuilder sb = new StringBuilder(getClass().getSimpleName()).append(" with ").append(caches.size()).append(" cache(s)): [");
		int i = 0;
		for (Cache cache : values) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(cache.toString());
			i++;
		}
		sb.append("]");
		return sb.toString();
	}
}
