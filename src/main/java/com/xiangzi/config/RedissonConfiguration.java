package com.xiangzi.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xiangzi.properties.RedisProperties;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonConfiguration {

	private static Logger logger = LoggerFactory.getLogger(RedissonConfiguration.class);

	@Autowired
	private RedisProperties properties;

	private enum RedisType {
		SINGLE("single");

		private String stringValue;

		RedisType(String s) {
			stringValue = s;
		}

		public String getStringValue() {
			return stringValue;
		}
	}

	@Bean(destroyMethod = "shutdown")
	public RedissonClient redissonClient() {
		RedissonClient redissonClient = null;
		try {
			Config config = new Config();

			if (StringUtils.isBlank(properties.getType()) || StringUtils.equalsIgnoreCase(RedisType.SINGLE.getStringValue(), properties.getType())) {
				String redisUrl = "redis://" + properties.getHost() + ":" + properties.getPort();
				config.useSingleServer().setAddress(redisUrl);
				config.useSingleServer().setDatabase(properties.getDatabase());// （数据库编号）,默认值：0,尝试连接的数据库编号。
				config.useSingleServer().setTimeout(properties.getTimeout());// （命令等待超时，单位：毫秒）,默认值：3000,等待节点回复命令的时间。该时间从命令发送成功时开始计时。
				config.useSingleServer().setConnectTimeout(properties.getTimeout());// （连接超时，单位：毫秒）,默认值：10000,同节点建立连接时的等待超时。时间单位是毫秒。
				config.useSingleServer().setSubscriptionConnectionPoolSize(50);// （发布和订阅连接池大小）,默认值：50,用于发布和订阅连接的连接池最大容量。连接池的连接数量自动弹性伸缩。
				config.useSingleServer().setConnectionPoolSize(64);// （连接池大小）,默认值：64,连接池最大容量。连接池的连接数量自动弹性伸缩。

				if (StringUtils.isNotBlank(properties.getPassword())) {
					config.useSingleServer().setPassword(properties.getPassword());
				}
			}

			redissonClient = Redisson.create(config);
		} catch (Exception e) {
			logger.error("redissonClient init Exception", e);
		}
		return redissonClient;
	}

}
