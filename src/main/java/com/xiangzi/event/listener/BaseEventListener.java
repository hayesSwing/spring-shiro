package com.xiangzi.event.listener;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.EventBus;

public abstract class BaseEventListener {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EventBus eventBus;

	/**
	 * 注册这个监听器
	 */
	@PostConstruct
	public void register() {
		eventBus.register(this);
	}

}
