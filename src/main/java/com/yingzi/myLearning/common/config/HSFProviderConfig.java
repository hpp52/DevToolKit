package com.yingzi.myLearning.common.config;

import com.alibaba.boot.hsf.annotation.HSFConsumer;
import com.yingzi.center.human.core.api.OrgRefApi;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HSFProviderConfig {

	/***********************************************************人力中心*******************************************************************/
	@HSFConsumer(clientTimeout = 30000, serviceVersion = "${human.hsf.version}" , serviceGroup="${human.hsf.serviceGroup}")
	private OrgRefApi orgRefApi;

	
}

