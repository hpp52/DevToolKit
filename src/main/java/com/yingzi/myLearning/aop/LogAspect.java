package com.dfkj.myLearning.aop;

import com.dfkj.center.bio.entity.exception.BioPlatformException;
import com.dfkj.center.bio.entity.utils.ValidatorUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @author cjy
 * @date 2018/9/6
 **/
@Aspect
@Component
public class LogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Around("@annotation(com.dfkj.center.bio.aop.SysVaild)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object result = null;
        try {
            Object[] params = joinPoint.getArgs();
            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            //2.最关键的一步:通过这获取到方法的所有参数名称的字符串数组
            String[] parameterNames = methodSignature.getParameterNames();
            // 入参校验
            this.paramValidate(params,parameterNames);
            result = joinPoint.proceed();

        } catch (Throwable throwable) {
            LOGGER.error("[请求接口异常]:{}", throwable.getMessage());
            // 再抛回给app，否则controller就捕获不了异常...
            throw new RuntimeException(throwable.getMessage());
        }
        return result;
    }



    private void paramValidate(Object[] params, String[] parameterNames) {
        if (ArrayUtils.isEmpty(params)) {
            return;
        }
        IntStream.range(0, params.length).forEach(i->{
            if (Objects.isNull(params[i])) {
                throw new BioPlatformException(500,parameterNames[i]+"传入参数为NULL");
            }
            ValidatorUtils.validate(params[i]);
        });
    }
}

