package com.dfkj.myLearning.common.config;

import com.alibaba.boot.hkhk.annotation.hkhkConsumer;
import com.dfkj.center.human.core.api.OrgRefApi;
import org.springframework.context.annotation.Configuration;

@Configuration
public class hkhkProviderConfig {

	/***********************************************************人力中心*******************************************************************/
	@hkhkConsumer(clientTimeout = 30000, serviceVersion = "${human.hkhk.version}" , serviceGroup="${human.hkhk.serviceGroup}")
	private OrgRefApi orgRefApi;

	
}

