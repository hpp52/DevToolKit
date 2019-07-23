package com.yingzi.myLearning.common.config;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.javassist.ClassClassPath;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtMethod;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;

/**
 * 方法参数打印切面
 */
@Aspect
@Component
public class MethodParamtersAdvisor {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static ClassPool classPool;

	static {

		classPool = ClassPool.getDefault();
		classPool.insertClassPath(new ClassClassPath(MethodParamtersAdvisor.class));
	}

	@Before("execution(* com.yingzi.center.bio.api..*(..))")
	public void before(JoinPoint joinPoint) {
		try {

			StringBuilder message = new StringBuilder();
			message.append("方法：{}.{}  参数值：");

			String className = joinPoint.getSignature().getDeclaringTypeName();
			String targetClassName = joinPoint.getTarget().getClass().getName();
			String methodName = joinPoint.getSignature().getName();
			String[] paramNames = getParamsName(targetClassName, methodName);

			String invokerMethodName = joinPoint.getSignature().getName();
			Object[] args = joinPoint.getArgs();
			for (int index = 0; index < args.length; ++index) {
				message.append(paramNames[index]).append("=").append(JSON.toJSONString(args[index])).append("  ");
			}
			if (logger.isInfoEnabled()) {
				logger.info(message.toString(), className, invokerMethodName);
			}
		} catch (Exception e) {
			logger.error("打印参数错误", e);
		}

	}

	private String[] getParamsName(String targetClassName, String methodName) throws Exception {

		CtClass ct = classPool.get(targetClassName);
		CtMethod cm = ct.getDeclaredMethod(methodName);
		MethodInfo methodInfo = cm.getMethodInfo();

		CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
		LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
		String[] paramNames = new String[cm.getParameterTypes().length];
		int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
		for (int i = 0; i < paramNames.length; i++)
			paramNames[i] = attr.variableName(i + pos);

		return paramNames;
	}

}
