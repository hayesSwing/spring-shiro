package com.xiangzi.config;

import org.apache.shiro.realm.Realm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xiangzi.shiro.SampleRealm;

@Configuration
public class ShiroConfig {
	
	@Bean
	public Realm realm() {
		SampleRealm defaultRealm = new SampleRealm();
		return defaultRealm;
	}
	
}
