package com.alsritter.services;

import com.alsritter.utils.ConstantKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 通用的用户数据获取
 *
 * @author alsritter
 * @version 1.0
 **/
@Service
@Slf4j
public class UserService {

    @Resource
    StringRedisTemplate stringTemplate;

    // 判断是否存在 redis 中
    public boolean isExistRedis(String id) {
        Set<String> members = stringTemplate.opsForSet().members(ConstantKit.USER_ID_LIST);
        for (String s : members) {
            if (s.equals(id)) {
                return true;
            }
        }
        return false;
    }
}
