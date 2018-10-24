package com.xiangzi.config;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

@Configuration
public class EventBusConfig {

	/**
	 * 创建事件总线对象/定义事件总线Bean
	 * <p>
	 * EventBus是Guava的事件处理机制，是设计模式中的观察者模式（生产/消费者编程模型）的优雅实现。对于事件监听和发布订阅模式，EventBus是一个非常优雅和简单解决方案，我们不用创建复杂的类和接口层次结构。
	 * </p>
	 */
	@Bean(name = "myEventBus")
	public EventBus eventBus() {

		return new AsyncEventBus(new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()));

		// return new AsyncEventBus(Executors.newFixedThreadPool(10));

		// BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(20);
		// ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20, 30,
		// TimeUnit.SECONDS, workQueue);
		// return new AsyncEventBus(executor);

		// return new EventBus();
	}

}
