package com.alsritter.interceptor;

import com.alsritter.annotation.AuthToken;
import com.alsritter.utils.BizException;
import com.alsritter.utils.CommonEnum;
import com.alsritter.utils.ConstantKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 这个拦截器只拦截带有 AuthToken 注解的方法
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
public class AuthorizationTokenInterceptor implements HandlerInterceptor {



    @Resource
    StringRedisTemplate stringTemplate;

    /**
     * 执行方法前执行
     * <br>
     * <b>
     * 自定义的 Token 拦截器
     * 客户端登录，输入用户id和密码，后台进行验证，如果验证失败则返回登录失败的提示。
     * 如果验证成功，则生成 token 然后将 userId 和 token 双向绑定 （可以根据 userId 取出 token 也可以根据 token 取出 userId）存入redis，
     * 同时使用 token+userId 作为 key 把当前时间戳也存入 redis。并且给它们都设置过期时间。
     * <br>
     * <br>
     * 如果该接口标注了 @AuthToken 注解，则要检查客户端传过来的 Authorization 字段，获取 token。
     * 由于 token 与 userId 双向绑定，可以通过获取的 token 来尝试从 redis 中获取 userId，
     * 如果可以获取则说明 token 正确，反之，说明错误，返回鉴权失败。
     * <br>
     * <br>
     * token 可以根据用户使用的情况来动态的调整自己过期时间。
     * 在生成 token 的同时也往 redis 里面存入了创建 token 时的时间戳，
     * 每次请求被拦截器拦截 token 验证成功之后，
     * 将当前时间与存在 redis 里面的 token 生成时刻的时间戳进行比较，
     * 当当前时间的距离创建时间快要到达设置的 redis 过期时间的话，
     * 就重新设置 token 过期时间，将过期时间延长。
     * 如果用户在设置的 redis 过期时间的时间长度内没有进行任何操作（没有发请求），则 token 会在 redis 中过期。
     * </b>
     *
     * @param request  :
     * @param response :
     * @param handler  :
     * @return : boolean
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 第三个参数 handler 可以获取访问的目标对象（Controller）所以通过其能够得到目标对象上面是否存在自定义的注解

        // 这个 HandlerMethod 可以用来匹配 Controller，如果不是 Controller 则跳过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 如果打上了 AuthToken 注解则需要验证 token
        if (method.getAnnotation(AuthToken.class) != null ||
                handlerMethod.getBeanType().getAnnotation(AuthToken.class) != null) {

            // 检查 request Header，默认是在 Authorization 存放鉴权信息的
            String token = request.getHeader("Authorization");
            // logger 的 {} 就是占位符
            log.info("Get token from request is {} ", token);
            String userId = "";

            ValueOperations<String, String> valueOperations = stringTemplate.opsForValue();

            if (token != null && token.length() != 0) {
                userId = valueOperations.get(token);
                log.info("Get username from Redis is {}", userId);
            }

            // 非空则判断超时
            if (userId != null && !userId.trim().equals("")) {
                // 获取距离 Token 创建已经过了多久
                long tokeBirthTime = Long.parseLong(Objects.requireNonNull(valueOperations.get(token + userId)));
                log.info("token Birth time is: {}", tokeBirthTime);
                long diff = System.currentTimeMillis() - tokeBirthTime;

                log.info("token is exist : {} ms", diff);

                // 当距离超时还剩半个小时时则刷新 Token 的过期时间
                // ConstantKit.TOKEN_EXPIRE_TIME 以秒为单位
                if (diff > ((ConstantKit.TOKEN_EXPIRE_TIME - ConstantKit.TOKEN_RESET_TIME) * 1000)) {
                    stringTemplate.expire(userId, ConstantKit.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
                    stringTemplate.expire(token, ConstantKit.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
                    // 也重置一下创建时间
                    valueOperations.set(token + userId, Long.toString(System.currentTimeMillis()));
                    stringTemplate.expire(token + userId, ConstantKit.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
                    log.info("Reset expire time success!");
                }

                // 如果 token 验证成功，将 token 对应的 userId 存在 request 中，以后就可以无须登陆直接取到 userId
                request.setAttribute(ConstantKit.REQUEST_CURRENT_KEY, userId);
                return true;

                // 如果为空则鉴权失败
            } else {
                throw new BizException(CommonEnum.UNAUTHORIZED);
            }

        }

        // 不需要鉴权的 api 取不到 用户 id
        request.setAttribute(ConstantKit.REQUEST_CURRENT_KEY, null);

        return true;
    }
}
