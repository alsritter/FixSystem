package com.alsritter.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * @author alsritter
 * @version 1.0
 **/
@Component
public class Md5TokenGenerator implements TokenGenerator {

    @Override
    public String generate(String... strings) {
        long timestamp = System.currentTimeMillis();
        StringBuilder tokenMeta = new StringBuilder();
        for (String s : strings) {
            tokenMeta.append(s);
        }
        tokenMeta.append(timestamp);
        return DigestUtils.md5DigestAsHex(tokenMeta.toString().getBytes());
    }
}