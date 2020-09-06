package com.alsritter.utils;

import org.springframework.stereotype.Component;

@Component
public interface TokenGenerator {
    String generate(String... strings);
}
