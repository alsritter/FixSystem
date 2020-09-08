package com.alsritter.config;

import com.alsritter.interceptor.AuthorizationImageCodeInterceptor;
import com.alsritter.interceptor.ParamNotNullInterceptor;
import com.alsritter.interceptor.AuthorizationTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author alsritter
 * @version 1.0
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 注册拦截器 MyInterceptor 前面自定义的 拦截器
    @Bean
    public AuthorizationTokenInterceptor authorizationInterceptor() {
        return new AuthorizationTokenInterceptor();
    }

    @Bean
    public ParamNotNullInterceptor allParamNotNullInterceptor() {
        return new ParamNotNullInterceptor();
    }

    @Bean
    public AuthorizationImageCodeInterceptor authorizationImageCodeInterceptor(){
        return new AuthorizationImageCodeInterceptor();
    }

    // 设置编码
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setDefaultCharset(StandardCharsets.UTF_8);
        return jsonConverter;
    }

    // 在消息转换器里添加这个响应处理器
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        WebMvcConfigurer.super.configureMessageConverters(converters);
        converters.add(mappingJackson2HttpMessageConverter());
    }

    // 添加拦截器到 Spring MVC 拦截器链 里面
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所有路径  /** 表示这个请求下的所有请求 /* 只拦截一级请求
        registry.addInterceptor(authorizationInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(allParamNotNullInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(authorizationImageCodeInterceptor()).addPathPatterns("/**");
    }

    // 非简单请求的CORS请求，会在正式通信之前，增加一次 HTTP 查询请求，称为"预检"请求（preflight）,
    // 这种情况下除了设置origin，还需要设置 Access-Control-Request-Method 以及 Access-Control-Request-Headers
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 或者写上请求头里 Origin 的内容（用逗号分隔多个） .allowedOrigins("http://127.0.0.1:5500")
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }


}
