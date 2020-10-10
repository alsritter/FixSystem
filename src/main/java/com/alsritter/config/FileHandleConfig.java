package com.alsritter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author alsritter
 * @version 1.0
 **/
@Configuration
public class FileHandleConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //创建 显示
        registry.addResourceHandler("/extStatic/**").addResourceLocations("file:../images/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        // http://127.0.0.1:7758/extStatic/35463838634_4fc79a297c_k.jpg
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}