package com.xiangzi.shiro.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import com.xiangzi.cache.CacheService;

public class DefaultShiroCache<K, V> implements Cache<K, V> {

	protected Logger logger = LoggerFactory.getLogger(DefaultShiroCache.class);

	private String name;

	private CacheService cacheService;

	public DefaultShiroCache(String name, CacheService cacheService) {
		this.name = name;
		this.cacheService = cacheService;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) throws CacheException {
		try {
			if(logger.isTraceEnabled()) {
				logger.trace("Getting object from cache [" + this.name + "] for key [" + key + "]");
			}
			
			if (key == null) {
				return null;
			} else {
				Object _result = this.cacheService.get(this.name, key.toString());
				if (_result == null) {
					logger.debug("Element for [" + key + "] is null.");
					return null;
				} else {
					return (V) SerializationUtils.deserialize((byte[]) _result);
				}
			}
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	@Override
	public V put(K key, V value) throws CacheException {
		if(logger.isTraceEnabled()) {
			logger.trace("Putting object in cache [" + this.name + "] for key [" + key + "]");
		}
		
		try {
			V previous = get(key);
			// 毫秒需要转换为秒
			// int secondsToExpire = (int)
			// (SecurityUtils.getSubject().getSession().getTimeout()/1000);
			// this.cacheService.set(this.name, key.toString(),
			// SerializationUtils.serialize(value), 0);

			this.cacheService.set(this.name, key.toString(), SerializationUtils.serialize(value), 60 * 60 * 24);
			return previous;
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public V remove(K key) throws CacheException {
		logger.info("DefaultShiroCache remove>>>" + key);
		V previos = null;
		try {
			previos = (V) this.cacheService.del(this.name, key.toString());
		} catch (Exception e) {
			logger.error("DefaultShiroCache remove Exception", e);
		}
		return previos;
	}

	@Override
	public void clear() throws CacheException {
		logger.info("DefaultShiroCache clear");
		try {
			this.cacheService.clear(this.name);
		} catch (Exception e) {
			logger.error("DefaultShiroCache clear Exception", e);
		}
	}

	@Override
	public int size() {
		int size = 0;
		try {
			size = this.cacheService.size(this.name);
		} catch (Exception e) {
			logger.error("DefaultShiroCache remove Exception", e);
		}
		return size;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<K> keys() {
		try {
			return (Set<K>) this.cacheService.keys(this.name);
		} catch (Exception e) {
			logger.error("DefaultShiroCache remove Exception", e);
		}
		return Collections.emptySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<V> values() {
		List<V> result = null;
		try {
			Collection<Object> _values = this.cacheService.values(this.name);
			result = new ArrayList<>(_values.size());
			for (Object object : _values) {
				V _result = (V) SerializationUtils.deserialize((byte[]) object);
				result.add(_result);
			}
			return result;
		} catch (Exception e) {
			logger.error("DefaultShiroCache values Exception", e);
		}

		if (!CollectionUtils.isEmpty(result)) {
			return Collections.unmodifiableList(result);
		} else {
			return Collections.emptyList();
		}

	}

}
