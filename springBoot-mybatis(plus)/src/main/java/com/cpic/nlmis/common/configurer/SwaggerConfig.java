package com.cpic.nlmis.common.configurer;

import com.cpic.nlmis.common.rest.ReturnCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.*;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @Author: hk
 * @Description: API接口测试：http://ip:port/swagger-ui.html
 * @Date: 2019/8/14 14:20
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
//
//		// 添加head参数start
//		ParameterBuilder tokenPar = new ParameterBuilder();

//		tokenPar.name("token").description("token(登录后获取token)").modelRef(new ModelRef("string"))
//				.parameterType("header").required(false).build();
//
//		pars.add(tokenPar.build());
//
		ApiSelectorBuilder apiSelectorBuilder = new Docket(DocumentationType.SWAGGER_2).apiInfo(getApiInfo()).select()
				// .apis(RequestHandlerSelectors.basePackage("com.cpic.nlmis.controller"))
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class));
		apiSelectorBuilder.paths(PathSelectors.any());
		List<Parameter> pars = new ArrayList<>();
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
				new ResponseMessageBuilder().code(Integer.parseInt(ReturnCodeEnum.OTHOR_ERROR.getCode())).message(ReturnCodeEnum.OTHOR_ERROR.getMsg()).build());


		return globalResponses;

	}

	private ApiInfo getApiInfo() {
		return new ApiInfoBuilder().title("湖南太平洋年轮商报救援--API测试说明文档")
				.description("开发测试版本API")
				.version(StringUtils.defaultString(this.version, "1.0.0"))
				.build();
	}
}
