package com.dfkj.myLearning.service.Chapter_1_Redis.redis;


import com.alibaba.druid.util.StringUtils;
import com.alibaba.edas.configcenter.config.ConfigService;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dfkj.myLearning.common.config.redis.EdasRedisClusterConfiguration;
import com.dfkj.myLearning.common.config.redis.RedisClusterModel;
import com.dfkj.myLearning.common.config.redis.RedisClusterWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;


@Configuration
@ConditionalOnClass({JedisConnection.class, RedisOperations.class, Jedis.class })
@EnableConfigurationProperties({EdasRedisClusterConfiguration.class })
@AutoConfigureBefore({RedisAutoConfiguration.class })
public class RedisFactoryConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisFactoryConfig.class);

	@Autowired
	@Qualifier("edasRedisClusterConfiguration")
	private EdasRedisClusterConfiguration conf;


	@Bean("redisConnectionFactory")
	//@ConditionalOnMissingBean(RedisConnectionFactory.class) //其他地方创建的不需要
	@ConditionalOnProperty(value = { "edas.redis.cluster.group", "edas.redis.cluster.dataId" })
	public JedisConnectionFactory redisConnectionFactory() throws UnknownHostException {
		JedisConnectionFactory jedisConnectFactory = null;
		if (jedisConnectFactory == null) {
			if (StringUtils.isEmpty(conf.getDataId()) || StringUtils.isEmpty(conf.getGroup())) {
				LOGGER.error("没有redis相关配置文件，不进行JedisConnectionFactory初始化。");
				return null;
			}
			String configInfo;
			try {
				configInfo = ConfigService.getConfig(conf.getDataId(), conf.getGroup(), 3000);
				LOGGER.info(configInfo);
				RedisClusterModel conf = JSON.parseObject(configInfo, RedisClusterModel.class);
				// JedisCluster配置
				String[] serverArray = conf.getClusterNodes().split(",");
				RedisClusterConfiguration redisClusterConfiguration = null;
				if (serverArray != null) {
					List<String> asList = Arrays.asList(serverArray);
					redisClusterConfiguration = new RedisClusterConfiguration(asList);
				}
				jedisConnectFactory = new RedisClusterWrapper(redisClusterConfiguration, RedisClusterWrapper.createJedisPoolConfig(conf.getPool()));
				if(org.apache.commons.lang3.StringUtils.isNotBlank(conf.getPassword())){
					jedisConnectFactory.setPassword(conf.getPassword());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return jedisConnectFactory;
	}


	
	
	@Bean
	@ConditionalOnMissingBean(RedisTemplate.class)
	@ConditionalOnProperty(value = { "edas.redis.cluster.group", "edas.redis.cluster.dataId" })
	public RedisTemplate<Object, Object> redisTemplate(@Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
		/*RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
		template.setConnectionFactory(redisConnectionFactory);
		return template;*/


		RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
		template.setConnectionFactory(redisConnectionFactory);
		//使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
		Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		serializer.setObjectMapper(mapper);
		template.setValueSerializer(serializer);
		template.setKeySerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}

	@Bean
	@ConditionalOnMissingBean(StringRedisTemplate.class)
	@ConditionalOnProperty(value = { "edas.redis.cluster.group", "edas.redis.cluster.dataId" })
	public StringRedisTemplate stringRedisTemplate(@Qualifier("redisConnectionFactory")RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		template.setValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		return template;

	}



}
