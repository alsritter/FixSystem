package com.alsritter.utils;

/**
 * 专门用来抛出数据库错误
 *
 * @author alsritter
 * @version 1.0
 **/
public class MyDBError extends RuntimeException {
    private final String msg;
    private final Throwable throwable;

    // 通过 Throwable 类来传递报错信息
    public MyDBError(String msg, Throwable throwable) {
        this.msg = msg;
        this.throwable = throwable;
    }

    @Override
    public String getMessage() {
        return msg + throwable.getMessage();
    }
}