package com.yingzi.myLearning.common;

import java.util.List;

/**
 * @Author: hk
 * @Description:
 * @Date: 2019/7/23 9:48
 */
@Configuration
@Import(ApplicationConfig.class)
public class WebAppConfig  extends WebMvcConfigurerAdapter {
    @Value("${bio.dev.no.interceptor}")
    private String noInterceptor;

    @Bean
    public YunxiUserService yunxiUserService() {
        return new YunxiUserServiceImpl();
    }

    @Bean
    public TokenInterceptor tokenInterceptor() {
        return new TokenInterceptor(noInterceptor, yunxiUserService());// 有参构造方法进行属性赋值
    }

    /**
     * 为swagger加的，不然找不到文件，我XX
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        // 注册自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(tokenInterceptor()).addPathPatterns("/api/**").excludePathPatterns("/api/user") // 登录页;
                .excludePathPatterns("/api/user/login") // 登录页;
                .excludePathPatterns("/api/user/logOut") // 登录页;
                .excludePathPatterns("/api/user/userresource");

        super.addInterceptors(registry);
    }

    /**
     * 解决long类型精度丢失的问题
     */
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        /**
         * 序列换成json时,将所有的long变成string 因为js中得数字类型不能包含所有的java long值
         */
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(jackson2HttpMessageConverter);
    }
}
