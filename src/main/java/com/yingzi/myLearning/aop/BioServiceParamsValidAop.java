package com.dfkj.myLearning.aop;//package com.dfkj.center.bio.aop;
//
//import java.util.Arrays;
//import java.util.Objects;
//import java.util.stream.Stream;
//
//import org.apache.commons.lang3.ArrayUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import com.dfkj.center.bio.entity.exception.BioPlatformException;
//import com.dfkj.center.bio.entity.utils.ValidatorUtils;
//
//@Aspect
//@Component
//public class BioServiceParamsValidAop {
//	private static Logger log = LoggerFactory.getLogger(BioServiceParamsValidAop.class);
//
//	//@Pointcut("execution(public com.dfkj.center.bio.api.impl *(..))")
//
//
//	@Pointcut("@annotation(com.alibaba.boot.hkhk.annotation.hkhkProvider)||@within(com.alibaba.boot.hkhk.annotation.hkhkProvider)")
//	public void exeMethod() {
//	}
//
//	@Around("exeMethod()")
//	public Object around(ProceedingJoinPoint pjp) throws Throwable {
//		Object[] params = pjp.getArgs();
//		// 入参校验
//		this.paramValidate(params);
//
//		/**
//		 * 不是所有的结果都有返回值
//		 */
//		//Object proceed = pjp.proceed();
//		//String methodName = pjp.getSignature().getName();
//
//		/*if (Objects.isNull(proceed)) {
//			log.error("服务接口【{}】返回空，请求参数：{}", methodName, Arrays.toString(params));
//			throw new NullPointerException("服务返回结果为空");
//		}*/
//		return pjp.proceed();
//
//	}
//
//	private void paramValidate(Object[] params) {
//		if (ArrayUtils.isEmpty(params)) {
//			return;
//		}
//		Stream.of(params).forEach(param -> {
//			if (Objects.isNull(param)) {
//				throw new BioPlatformException("传入参数为空！");
//			}
//			ValidatorUtils.validate(param);
//		});
//	}
//
//}
