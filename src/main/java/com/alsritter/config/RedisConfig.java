package com.alsritter.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.time.Duration;

/**
 * redis 配置类
 *
 * @author alsritter
 * @version 1.0
 **/
// RedisAutoConfiguration 自动把单机模式或者集群模式给配置了，
// 同时还自动配置了客户端的问题（好像默认使用的是 lettuce）
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {

    // 配置缓存
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        // 设置缓存过期时间为 120 秒后
        return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(120)).disableCachingNullValues();
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        // 使用 RedisCacheManager 作为缓存管理器
        return RedisCacheManager.builder(factory).cacheDefaults(cacheConfiguration()).transactionAware().build();
    }

    /**
     * 默认情况下的模板只能支持 RedisTemplate<String, String> ，也就是只能存入字符串
     * 这在开发中是不友好的，所以自定义模板是很有必要的，
     * 当自定义了模板又想使用 String 存储这时候就可以使用 StringRedisTemplate 的方式和自定义模板共存的方式
     *
     * ConditionalOnMissingBean 注解作用在 @bean 定义上，它的作用就是在容器加载它作用的 bean 时，
     * 检查容器中是否存在目标类型（ConditionalOnMissingBean 注解的 value 值）的 bean 了，
     * 如果存在这跳过原始 bean 的 BeanDefinition 加载动作。
     */
    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate( RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
