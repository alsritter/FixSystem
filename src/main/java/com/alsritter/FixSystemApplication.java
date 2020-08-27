package com.alsritter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author alsritter
 * @version 1.0
 **/
@SpringBootApplication
@MapperScan("com.alsritter.mappers")
public class FixSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(FixSystemApplication.class, args);
    }
}
