package com.alsritter.services;

import com.alsritter.pojo.User;
import com.alsritter.utils.BizException;
import com.alsritter.utils.CommonEnum;
import com.alsritter.utils.ConstantKit;
import com.alsritter.utils.Md5TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

    private Md5TokenGenerator tokenGenerator;

    @Autowired
    public void setTokenGenerator(Md5TokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

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

    public String getId(HttpServletRequest request) {
        // 直接通过 Token 来取得数据
        String id = (String) request.getAttribute(ConstantKit.REQUEST_CURRENT_KEY);
        if (id == null) {
            throw new NullPointerException(CommonEnum.UNAUTHORIZED.getResultMsg());
        }

        return id;
    }


    public String createToken(User user, ConstantKit.UserKey userKey) {
        if (user == null) {
            throw new BizException(CommonEnum.NOT_FOUND);
        }

        ValueOperations<String, String> valueOperations = stringTemplate.opsForValue();
        // 需要先检查当前是否已经有 Token 了,如果已经有 Token 了先销毁之前的 Token,再创建新的
        String beforeToken = valueOperations.get(user.getId());

        if (beforeToken != null && !beforeToken.trim().equals("")) {
            // 同拦截器，因为之前没有考虑多类型用户的 Token 鉴别问题，所以这里直接加个 Key 来避免更改太多下面的代码
            beforeToken = userKey.toString() + beforeToken;

            // 只需删除用 token 当 key 存的 workId,因为 workId 当 key 的那个会给覆盖掉
            stringTemplate.delete(beforeToken);
            // 还需要把那个由 token + workId 组成的用来记录创建时间的 key 删掉
            stringTemplate.delete(beforeToken + user.getId());
            log.debug("删除了之前的 Token: {}", beforeToken);
        }

        // 生成新的 Token
        String token = tokenGenerator.generate(user.getId(), user.getPassword());
        String tempToken = token;
        // 同理
        token = userKey.toString() + token;

        valueOperations.set(user.getId(), token);
        //设置 key 生存时间，当 key 过期时，它会被自动删除，时间是秒
        stringTemplate.expire(user.getId(), ConstantKit.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

        valueOperations.set(token, user.getId());
        stringTemplate.expire(token, ConstantKit.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

        // 这一步主要是记录创建的时间，拦截器通过创建时间计算还有多久过期
        valueOperations.set(token + user.getId(), Long.toString(System.currentTimeMillis()));
        stringTemplate.expire(token + user.getId(), ConstantKit.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        return tempToken;
    }
}
