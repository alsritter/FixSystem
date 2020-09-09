package com.alsritter.controller;

import com.alsritter.annotation.AuthImageCode;
import com.alsritter.model.ResponseTemplate;
import com.alsritter.pojo.Worker;
import com.alsritter.services.WorkerService;
import com.alsritter.utils.BizException;
import com.alsritter.utils.CommonEnum;
import com.alsritter.utils.ConstantKit;
import com.alsritter.utils.Md5TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/worker")
public class WorkerController {


    @Resource
    StringRedisTemplate stringTemplate;
    private WorkerService workerService;
    private Md5TokenGenerator tokenGenerator;


    @Autowired
    public void setWorkerService(WorkerService workerService) {
        this.workerService = workerService;
    }


    @Autowired
    public void setTokenGenerator(Md5TokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }


    @PostMapping("/login")
    @AuthImageCode
    public ResponseTemplate<JSONObject> login(
            String codevalue,
            String uuid,
            String workId,
            String password) {
        // 先查询数据
        Worker user = workerService.loginWorker(workId, password);
        JSONObject result = new JSONObject();

        if (user != null) {

            ValueOperations<String, String> valueOperations = stringTemplate.opsForValue();

            // 需要先检查当前是否已经有 Token 了,如果已经有 Token 了先销毁之前的 Token,再创建新的
            String beforeToken = valueOperations.get(workId);
            if (beforeToken != null && !beforeToken.trim().equals("")) {
                // 只需删除用 token 当 key 存的 workId,因为 workId 当 key 的那个会给覆盖掉
                stringTemplate.delete(beforeToken);
                // 还需要把那个由 token + workId 组成的用来记录创建时间的 key 删掉
                stringTemplate.delete(beforeToken + workId);
                log.debug("删除了之前的 Token: {}", beforeToken);
            }

            // 生成新的 Token
            String token = tokenGenerator.generate(workId, password);

            valueOperations.set(workId, token);
            //设置 key 生存时间，当 key 过期时，它会被自动删除，时间是秒
            stringTemplate.expire(workId, ConstantKit.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

            valueOperations.set(token, workId);
            stringTemplate.expire(token, ConstantKit.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

            // 这一步主要是记录创建的时间，拦截器通过创建时间计算还有多久过期
            valueOperations.set(token + workId, Long.toString(System.currentTimeMillis()));
            stringTemplate.expire(token + workId, ConstantKit.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);


            result.put("status", "登录成功");
            result.put("workId", user.getWorkId());
            result.put("name", user.getName());
            result.put("gender", user.getGender());
            result.put("phone", user.getPhone());
            result.put("joinDate", user.getJoinDate());
            result.put("details", user.getDetails());
            result.put("ordersNumber", user.getOrdersNumber());
            result.put("avgGrade", user.getAvgGrade());
            result.put("token", token);
            return ResponseTemplate.<JSONObject>builder()
                    .code(HttpServletResponse.SC_OK)
                    .message("登陆成功")
                    .data(result)
                    .build();
        } else {
            result.put("status", "当前工人不存在");
            return ResponseTemplate.<JSONObject>builder()
                    .code(404)
                    .message("当前工人不存在")
                    .data(result)
                    .build();
        }
    }
}
