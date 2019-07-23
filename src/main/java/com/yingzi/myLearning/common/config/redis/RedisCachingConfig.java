package com.yingzi.myLearning.common.config.redis;

import com.yingzi.center.bio.entity.redis.RedisKeyGenerator;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisCachingConfig extends CachingConfigurerSupport {

	@Bean
	@Override
	public KeyGenerator keyGenerator() {
		// 缓存key生成者
		RedisKeyGenerator cacheKeyGenerator = new RedisKeyGenerator();
		return cacheKeyGenerator;
	}

	@Bean
	public RedisCacheManager cacheManager(RedisTemplate redisTemplate) {
		RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
		// 设置缓存过期时间
		// rcm.setDefaultExpiration(60);//秒
		return rcm;
	}
}
