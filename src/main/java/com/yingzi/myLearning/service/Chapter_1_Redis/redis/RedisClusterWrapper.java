package com.dfkj.myLearning.service.Chapter_1_Redis.redis;

import com.dfkj.myLearning.common.config.redis.EdasRedisClusterConfiguration;
import com.dfkj.myLearning.common.config.redis.RedisClusterModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableConfigurationProperties(EdasRedisClusterConfiguration.class)
public class RedisClusterWrapper extends JedisConnectionFactory  {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisClusterWrapper.class);
	private static final long serialVersionUID = 1L;

	public RedisClusterWrapper() {
		super();
	}
	
	public RedisClusterWrapper(RedisClusterConfiguration clusterConfig, JedisPoolConfig poolConfig) {
		super(clusterConfig,poolConfig);
	}
	
	public static JedisPoolConfig createJedisPoolConfig(RedisClusterModel.Pool poolConf) {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(poolConf.getMaxIdle());
		poolConfig.setMaxTotal(poolConf.getMaxActive());
		poolConfig.setMinIdle(poolConf.getMinIdle());
		return poolConfig;
	}

	
}
