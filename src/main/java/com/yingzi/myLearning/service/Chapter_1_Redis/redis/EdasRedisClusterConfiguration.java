package com.yingzi.myLearning.service.Chapter_1_Redis.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("edasRedisClusterConfiguration")
@ConfigurationProperties("edas.redis.cluster")
public class EdasRedisClusterConfiguration {
	
	public EdasRedisClusterConfiguration() {
		super();
	}
	
	private String group;
	private String dataId;
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	
}