package com.dfkj.myLearning.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 生物中心事件记录注解，实现由BioEventAop类实现具体的逻辑
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BioEvent {
	String value() default "";
}
