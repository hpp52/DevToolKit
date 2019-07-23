package com.yingzi.myLearning.common.config.redis;

public class RedisClusterModel {
	private int expireSeconds;
	
	//127.0.0.1:23679,127.0.0.1:23678
    private String clusterNodes;
    private int commandTimeout;
    private String password;
    private Pool pool = new Pool();
    
    
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Pool getPool() {
		return pool;
	}
	public void setPool(Pool pool) {
		this.pool = pool;
	}
	public int getExpireSeconds() {
		return expireSeconds;
	}
	public void setExpireSeconds(int expireSeconds) {
		this.expireSeconds = expireSeconds;
	}
	public String getClusterNodes() {
		return clusterNodes;
	}
	public void setClusterNodes(String clusterNodes) {
		this.clusterNodes = clusterNodes;
	}
	public int getCommandTimeout() {
		return commandTimeout;
	}
	public void setCommandTimeout(int commandTimeout) {
		this.commandTimeout = commandTimeout;
	}


	public class Pool{
		private Integer maxActive = 100;
		private Integer maxIdle = 100;
		private Integer minIdle = 0;
		public Integer getMaxActive() {
			return maxActive;
		}
		public void setMaxActive(Integer maxActive) {
			this.maxActive = maxActive;
		}
		public Integer getMaxIdle() {
			return maxIdle;
		}
		public void setMaxIdle(Integer maxIdle) {
			this.maxIdle = maxIdle;
		}
		public Integer getMinIdle() {
			return minIdle;
		}
		public void setMinIdle(Integer minIdle) {
			this.minIdle = minIdle;
		}
		
	}


}
