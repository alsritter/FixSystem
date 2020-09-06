package com.alsritter.config;

import com.alsritter.interceptor.AuthorizationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author alsritter
 * @version 1.0
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 注册拦截器 MyInterceptor 前面自定义的 拦截器
    @Bean
    public AuthorizationInterceptor authorizationInterceptor(){
        return new AuthorizationInterceptor();
    }

    // 添加拦截器到 Spring MVC 拦截器链 里面
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所有路径  /** 表示这个请求下的所有请求 /* 只拦截一级请求
        registry.addInterceptor(authorizationInterceptor()).addPathPatterns("/admin/**");
    }
}
