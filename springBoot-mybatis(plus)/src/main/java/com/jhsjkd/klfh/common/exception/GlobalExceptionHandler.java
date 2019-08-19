package com.dfkj.nlmis.common.exception;

import com.dfkj.nlmis.common.rest.RestResponse;
import com.dfkj.nlmis.common.rest.ReturnCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @Author: hk
 * @Description: 全局异常处理
 * @Date: 2019/8/14 14:20
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 参数校验
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public RestResponse bindException(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();

		String errorMesssage = "校验失败:";

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			errorMesssage += fieldError.getDefaultMessage() + ", ";
		}
		return RestResponse.builder(ReturnCodeEnum.PARAM_ERROR.getCode(),
				ReturnCodeEnum.PARAM_MISSING.getMsg() + errorMesssage);
	}

	/**
	 * 异常时打印具体信息
	 * @param ex
	 * @param request
	 */
	private void log(Exception ex, HttpServletRequest request) {

		logger.error("************************异常开始*******************************");

		logger.error("请求地址：" + request.getRequestURL());
		Enumeration enumeration = request.getParameterNames();
		logger.error("请求参数");
		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement().toString();
			logger.error(name + "---" + request.getParameter(name));
		}

		StackTraceElement[] error = ex.getStackTrace();
		for (StackTraceElement stackTraceElement : error) {
			logger.error(stackTraceElement.toString());
		}
		logger.error("************************异常结束*******************************");
	}

}
