package com.alsritter.controller;

import com.alsritter.utils.BizException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 其它的全局访问
 *
 * @author alsritter
 * @version 1.0
 **/
@RestController
public class OtherController {
    /**
     * 防止一些浏览器乱发错误请求导致报错，所以配置错误路径用来接收请求
     */
    @GetMapping("/error")
    public void sendError(){
        throw new BizException(404,"可能是访问了错误的路径，或者浏览器、NodeJS框架乱发的资源请求");
    }
}
