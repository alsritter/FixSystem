package com.alsritter.controller;

import com.alsritter.annotation.AuthToken;
import com.alsritter.model.ResponseTemplate;
import com.alsritter.pojo.Admin;
import com.alsritter.services.LoginService;
import com.alsritter.utils.ConstantKit;
import com.alsritter.utils.Md5TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminLoginController {

    private LoginService loginService;

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    private Md5TokenGenerator tokenGenerator;

    @Autowired
    public void setTokenGenerator(Md5TokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    @Resource
    StringRedisTemplate stringTemplate;

    @PostMapping(value = "/login")
    public ResponseTemplate<JSONObject> login(String workId, String password) {

        log.info("username:" + workId + "      password:" + password);
        // 先查询数据
        Admin user = loginService.adminLogin(workId, password);

        JSONObject result = new JSONObject();
        if (user != null) {

            ValueOperations<String, String> valueOperations = stringTemplate.opsForValue();

            String token = tokenGenerator.generate(workId, password);

            // TODO: 每次重新登陆需要把 redis 缓存的 Token 删除掉
            // 需要先检查当前是否已经有 Token 了,如果已经有 Token 了先销毁之前的 Token,再创建新的
            String tempToken = valueOperations.get(workId);
            if (tempToken != null && !tempToken.trim().equals("")) {
                // 先删除用 token 当 key 存的 workId
                stringTemplate.delete(valueOperations.get(tempToken));
                // 再删除
            }

            valueOperations.set(workId, token);
            //设置 key 生存时间，当 key 过期时，它会被自动删除，时间是秒
            stringTemplate.expire(workId, ConstantKit.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

            valueOperations.set(token, workId);
            stringTemplate.expire(token, ConstantKit.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

            long currentTime = System.currentTimeMillis();
            valueOperations.set(token + workId, Long.toString(currentTime));

            // TODO: JSONObject 能否自动自动生成
            result.put("status", "登录成功");
            result.put("token", token);
        } else {
            result.put("status", "登录失败");
        }

        return ResponseTemplate.<JSONObject>builder()
                .code(200)
                .message("登录成功")
                .data(result)
                .build();

    }

    //测试权限访问
    @GetMapping(value = "/test")
    @AuthToken
    public ResponseTemplate<String> test() {

        log.info("已进入test路径");

        return ResponseTemplate.<String>builder()
                .code(200)
                .message("Success")
                .data("test url")
                .build();
    }
}
