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

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // Jackson 序列方式
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();

        // Jackson 默认自动识别 Public 修饰的成员变量、getter、setter
        // private、protected、public 修饰的成员变量都可以自动识别，无需都指定 getter、setter 或者 public。
        // 参考 https://blog.csdn.net/sdyy321/article/details/40298081
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        //====================配置 Jackson2 使之能自动封装不同类型数据=====================
        /** 自动封装 8 种基本数据类型（List 之类的），使之能变成 JSON 的形式
         * 例如封装 ArrayList：["java.util.ArrayList",[{"@class":"com.model.app","id":72,"uuid":"c4d7fc52-4096-4c79-81ef-32cb1b87fd28","type":2}]]
         * 参考 https://www.jianshu.com/p/c5fcd2a1ab49
         * RedisTemplate配置的 jackson.ObjectMapper 里的一个 enableDefaultTyping 方法过期解决
         * 参考 https://blog.csdn.net/zzhongcy/article/details/105813105
         */
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        //===============================将其配置到 template 上========================================
        // Redis 链接
        template.setConnectionFactory(redisConnectionFactory);
        // Redis Key - Value 序列化使用 Jackson
        template.setKeySerializer(jackson2JsonRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // Redis HashKey - HashValue 序列化使用 Jackson
        template.setHashKeySerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 默认情况下的模板只能支持 RedisTemplate<String, String> ，也就是只能存入字符串
     * 这在开发中是不友好的，所以自定义模板是很有必要的，
     * 当自定义了模板又想使用 String 存储这时候就可以使用 StringRedisTemplate 的方式和自定义模板共存的方式
     */
    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate( RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
