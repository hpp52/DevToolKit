package com.yingzi.myLearning.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动创建保健计划注解，实现由BioBuildHealthPlanAop类实现具体的逻辑
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BioBuildHealthPlan {
	String value() default "";
}
