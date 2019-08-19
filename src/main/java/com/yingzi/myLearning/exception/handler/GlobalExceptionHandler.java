package com.dfkj.myLearning.exception.handler;

import com.dfkj.center.bio.common.constant.ReturnCodeEnum;
import com.dfkj.center.bio.common.exception.TokenException;
import com.dfkj.center.bio.common.rest.RestResponse;
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

@ControllerAdvice
public class GlobalExceptionHandler {
	// MyBatisSystemException,BindingException,.BadSqlGrammarException,MySQLSyntaxErrorException
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

	@ExceptionHandler(TokenException.class)
	@ResponseBody
	public RestResponse tokenException(TokenException e) {
		logger.error(ReturnCodeEnum.TOKEN_DISABLE.getMsg() + "token失效，请重新登陆。", e.getStackTrace());
		return RestResponse.builder(ReturnCodeEnum.TOKEN_DISABLE.getCode(),
				ReturnCodeEnum.TOKEN_DISABLE.getMsg() + "token失效，请重新登陆。");
	}

	/*@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public RestResponse jsonHandler(HttpServletRequest request, Exception e) throws Exception {
		log(e, request);

		return RestResponse.builder(ReturnCodeEnum.OTHOR_ERROR.getCode(),
				ReturnCodeEnum.OTHOR_ERROR.getMsg() + e.getStackTrace());
	}*/

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
