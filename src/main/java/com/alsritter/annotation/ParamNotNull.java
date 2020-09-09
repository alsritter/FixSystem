package com.alsritter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 检查参数非空，当有多个参数
 * 不能为空时需要使用下面那个
 * 数组来传入多个参数名
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamNotNull {
    String param() default "";

    String[] params() default {};
}