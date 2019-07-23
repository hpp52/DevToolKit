package com.yingzi.myLearning.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BioBuildHealthPlanAop {
	private static Logger logger = LoggerFactory.getLogger(BioBuildHealthPlanAop.class);
	
	@Pointcut("@annotation(com.yingzi.center.bio.aop.BioBuildHealthPlan)||"
			+ " @within(com.yingzi.center.bio.aop.BioBuildHealthPlan)")
	public void pointCut() {

	}
	/**
	 * 注意是返回后才进行插入记录-主要是因为id的获取
	 */
	@AfterReturning("pointCut()")
	public void  doHandler(JoinPoint joinPoint) throws Throwable {
		

	
	}
	
	
	
	
}
