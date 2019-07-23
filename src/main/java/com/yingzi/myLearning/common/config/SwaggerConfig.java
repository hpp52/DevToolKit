package com.yingzi.myLearning.common.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yingzi.center.bio.common.constant.ReturnCodeEnum;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.Parameter;

/**
 * API接口测试：http://ip:port/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	private final static String SWAGGER_APIOPEN = "close";

	@Value("${swagger.api.open}")
	private String swaggerApiIsopen;


	@Value("${swagger.api.version}")
	private String version;

	@Bean
	public Docket swagger2Api() {

		

		// 添加head参数start
		ParameterBuilder tokenPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<>();
		tokenPar.name("token").description("token(登录后获取token)").modelRef(new ModelRef("string"))
				.parameterType("header").required(false).build();

		pars.add(tokenPar.build());

		ApiSelectorBuilder apiSelectorBuilder = new Docket(DocumentationType.SWAGGER_2).apiInfo(getApiInfo()).select()
				// .apis(RequestHandlerSelectors.basePackage("com.yingzi.center.bio.web.controller"))
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class));

		
		if(SWAGGER_APIOPEN.equalsIgnoreCase(swaggerApiIsopen)) {
			apiSelectorBuilder.paths(PathSelectors.none());// 线上环境，添加路径过滤，设置为全部都不符合
		}else {
			apiSelectorBuilder.paths(PathSelectors.any());
		}
		Docket docket = apiSelectorBuilder.build().globalOperationParameters(pars)
				.globalResponseMessage(RequestMethod.GET, buildGlobalResponses())
	            .globalResponseMessage(RequestMethod.POST, buildGlobalResponses())
	            .globalResponseMessage(RequestMethod.PUT, buildGlobalResponses())
	            .globalResponseMessage(RequestMethod.DELETE, buildGlobalResponses());

		return docket;
	}
	



	private List<ResponseMessage> buildGlobalResponses() {
		List<ResponseMessage> globalResponses = Arrays.asList(
				new ResponseMessageBuilder().code(200).message("OK").build(),
				new ResponseMessageBuilder().code(Integer.parseInt(ReturnCodeEnum.PARAM_MISSING.getCode())).message(ReturnCodeEnum.PARAM_MISSING.getMsg()).build(),
				new ResponseMessageBuilder().code(Integer.parseInt(ReturnCodeEnum.PARAM_ERROR.getCode())).message(ReturnCodeEnum.PARAM_ERROR.getMsg()).build(),
				new ResponseMessageBuilder().code(Integer.parseInt(ReturnCodeEnum.PARAM_UN_LOGIN.getCode())).message(ReturnCodeEnum.PARAM_UN_LOGIN.getMsg()).build(),
				new ResponseMessageBuilder().code(Integer.parseInt(ReturnCodeEnum.FILE_UPLOAD_FAIL.getCode())).message(ReturnCodeEnum.FILE_UPLOAD_FAIL.getMsg()).build(),
				new ResponseMessageBuilder().code(Integer.parseInt(ReturnCodeEnum.FILE_UPLOAD_EMPTY.getCode())).message(ReturnCodeEnum.FILE_UPLOAD_EMPTY.getMsg()).build(),
				
				new ResponseMessageBuilder().code(Integer.parseInt(ReturnCodeEnum.TOKEN_EMPTY.getCode())).message(ReturnCodeEnum.TOKEN_EMPTY.getMsg()).build(),
				new ResponseMessageBuilder().code(Integer.parseInt(ReturnCodeEnum.TOKEN_ERROR.getCode())).message(ReturnCodeEnum.TOKEN_ERROR.getMsg()).build(),
				new ResponseMessageBuilder().code(Integer.parseInt(ReturnCodeEnum.TOKEN_DISABLE.getCode())).message(ReturnCodeEnum.TOKEN_DISABLE.getMsg()).build(),
				new ResponseMessageBuilder().code(Integer.parseInt(ReturnCodeEnum.OTHOR_ERROR.getCode())).message(ReturnCodeEnum.OTHOR_ERROR.getMsg()).build());
				

		return globalResponses;

	}

	private ApiInfo getApiInfo() {
		return new ApiInfoBuilder().title("生物中心安全--API说明文档")
				.description("开发测试版本API")
				.contact(new Contact("生物中心安全开发组", "", ""))
				.version(StringUtils.defaultString(this.version, "1.0.0"))
				.build();
	}
}
