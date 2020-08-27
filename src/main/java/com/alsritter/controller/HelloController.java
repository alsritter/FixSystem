package com.alsritter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试项目启动
 *
 * @author alsritter
 * @version 1.0
 **/
@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/temp")
    public String sayHello() {
        return "hello";
    }
}
