package com.alsritter.interceptor;

import com.alsritter.model.ResponseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局处理返回值，把状态码加上，不同与之前那个全局报错处理的，这个主要把处理响应成功的状态码添加上
 * 这个 @RestControllerAdvice 注解等于 @ResponseBody + @ControllerAdvice
 *
 * @author alsritter
 * @version 1.0
 **/
@RestControllerAdvice
@Slf4j
public class GlobalReturnHandler implements ResponseBodyAdvice<ResponseTemplate<Object>> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 所有返回类型为 ResponseTemplate 把数据传到下面的 beforeBodyWrite 方法上
        return true;
    }

    @Override
    public ResponseTemplate<Object> beforeBodyWrite(ResponseTemplate<Object> body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.valueOf(body.getCode()));
        return body;
    }
}
