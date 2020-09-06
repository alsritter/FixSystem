package com.alsritter.controller;

import com.alsritter.annotation.AuthToken;
import com.alsritter.model.ResponseTemplate;
import com.alsritter.pojo.Admin;
import com.alsritter.pojo.Orders;
import com.alsritter.services.LoginService;
import com.alsritter.services.OrdersService;
import com.alsritter.utils.ConstantKit;
import com.alsritter.utils.Md5TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

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

    private OrdersService ordersService;

    @Autowired
    public void setOrdersService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }


    @PostMapping(value = "/login")
    public ResponseTemplate<JSONObject> login(String workId, String password) {
        // 先查询数据
        Admin user = loginService.adminLogin(workId, password);

        // 先创建对象，下面分别赋值
        JSONObject result = new JSONObject();
        int code = 500;
        String massage = "";


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

            code = HttpServletResponse.SC_OK;
            massage = "登陆成功";
            result.put("status", "登录成功");
            result.put("workId", user.getWorkId());
            result.put("name", user.getName());
            result.put("gender", user.getGender());
            result.put("joinDate", user.getJoinDate());
            result.put("phone", user.getPhone());
            result.put("details", user.getDetails());
            result.put("token", token);
        } else {
            code = HttpServletResponse.SC_NOT_FOUND;
            massage = "登陆失败";
            result.put("status", "登录失败");
        }

        return ResponseTemplate.<JSONObject>builder()
                .code(code)
                .message(massage)
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


    @GetMapping("/order-list")
    @AuthToken
    public ResponseTemplate<List<Orders>> orderList() {
        List<Orders> allOrders = ordersService.getAllOrders();
        // 先赋初始值是报错的，如果下面找到数据了自然会覆盖
        int code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        String massage = "内部错误";

        if (!allOrders.isEmpty()) {
            code = HttpServletResponse.SC_OK;
            massage = "订单 list";
        }

        return ResponseTemplate
                .<List<Orders>>builder()
                .code(code)
                .message(massage)
                .data(allOrders)
                .build();
    }


    @GetMapping("/order")
    @AuthToken
    public ResponseTemplate<Orders> getOrder(@RequestParam long fixTableId){
        Orders orders = ordersService.getOrders(fixTableId);
        int code = HttpServletResponse.SC_NOT_FOUND;
        String massage = "未找到订单";

        if (orders != null) {
            code = HttpServletResponse.SC_OK;
            massage = "订单详情";
        }

        return ResponseTemplate
                .<Orders>builder()
                .code(code)
                .message(massage)
                .data(orders)
                .build();
    }
}
