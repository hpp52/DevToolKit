package com.yingzi.myLearning.jobs;

import com.yingzi.center.bio.controller.bio.BioHealthPlanController;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 定时任务测试类 
 */
@Component
public class TestJob implements Job {
	private static Logger logger = LoggerFactory.getLogger(BioHealthPlanController.class);
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	logger.info("定时任务跑啊跑 1============= >");
    	logger.info("定时任务跑啊跑 2============= >");
    	logger.info("定时任务跑啊跑 3============= >");
    }

}
