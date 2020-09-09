package com.alsritter.controller;

import com.alsritter.annotation.AllParamNotNull;
import com.alsritter.annotation.AuthImageCode;
import com.alsritter.annotation.AuthToken;
import com.alsritter.model.ResponseTemplate;
import com.alsritter.pojo.Admin;
import com.alsritter.pojo.Orders;
import com.alsritter.services.LoginService;
import com.alsritter.services.OrdersService;
import com.alsritter.utils.BizException;
import com.alsritter.utils.CommonEnum;
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
    @AllParamNotNull
    @AuthImageCode
    public ResponseTemplate<JSONObject> login(
            String codevalue,
            String uuid,
            String workId,
            String password) {
        // 先查询数据
        Admin user = loginService.adminLogin(workId, password);
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
            result.put("joinDate", user.getJoinDate());
            result.put("phone", user.getPhone());
            result.put("details", user.getDetails());
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

        return ResponseTemplate
                .<List<Orders>>builder()
                .code(HttpServletResponse.SC_OK)
                .message("订单 list")
                .data(allOrders)
                .build();
    }


    @GetMapping("/order")
    @AuthToken
    public ResponseTemplate<Orders> getOrder(@RequestParam long fixTableId){
        Orders orders = ordersService.getOrders(fixTableId);

        if (orders == null) {
            throw new BizException(CommonEnum.NOT_FOUND);
        }

        return ResponseTemplate
                .<Orders>builder()
                .code(HttpServletResponse.SC_OK)
                .message("订单详情")
                .data(orders)
                .build();
    }


    @DeleteMapping(value = "/order", produces = "application/json;charset=utf-8")
    @AuthToken
    public String deleteOrder(long fixTableId){
        // TODO: 等待实现
        return "{\n" +
                "    \"code\": 204,\n" +
                "    \"message\": \"取消订单成功\",\n" +
                "    \"data\": {\n" +
                "        \"status\": \"取消订单成功\"\n" +
                "    }\n" +
                "}";
    }

    @GetMapping(value = "/select-worker", produces = "application/json;charset=utf-8")
    @AuthToken
    public String getLeisureWorkerList(){
        // TODO: 等待实现
        return "{\n" +
                "  \"code\": 200,\n" +
                "  \"message\": \"获取空闲工人列表\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"workId\": \"201825070120\",\n" +
                "      \"workerName\": \"张师傅\",\n" +
                "      \"grade\": 2.2\n" +
                "    },\n" +
                "    {\n" +
                "      \"workId\": \"201825070120\",\n" +
                "      \"workerName\": \"李师傅\",\n" +
                "      \"grade\": 3.1\n" +
                "    },\n" +
                "    {\n" +
                "      \"workId\": \"201825070120\",\n" +
                "      \"workerName\": \"梁师傅\",\n" +
                "      \"grade\": 4.2\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    @PatchMapping(value = "/select-worker" , produces = "application/json;charset=utf-8")
    @AuthToken
    public String selectLeisureWorker(String workId){
        // TODO: 等待实现
        return "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"选择成功\",\n" +
                "    \"data\": {\n" +
                "        \"status\": \"选择成功\"\n" +
                "    }\n" +
                "}";
    }

    @PatchMapping(value = "/user" , produces = "application/json;charset=utf-8")
    @AuthToken
    public String updateUser(String workId, String phone, String gender){
        // TODO: 等待实现
        return "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"更新成功\",\n" +
                "    \"data\": {\n" +
                "        \"status\": \"更新成功\"\n" +
                "    }\n" +
                "}";
    }

    @GetMapping(value = "/tool-list", produces = "application/json;charset=utf-8")
    @AuthToken
    public String getToolList(){
        // TODO: 等待实现
        return "{\n" +
                "   \"code\": 200,\n" +
                "   \"message\": \"获取耗材\",\n" +
                "   \"data\": [\n" +
                "       {\n" +
                "       \"toolId\": 1,\n" +
                "       \"toolName\": \"水管\",\n" +
                "       \"toolCount\": 100\n" +
                "       },\n" +
                "       {\n" +
                "       \"toolId\": 2,\n" +
                "       \"toolName\": \"胶布\",\n" +
                "       \"toolCount\": 100\n" +
                "       },\n" +
                "       {\n" +
                "       \"toolId\": 3,\n" +
                "       \"toolName\": \"网线\",\n" +
                "       \"toolCount\": 100\n" +
                "       }\n" +
                "   ]\n" +
                "}";
    }

    @PatchMapping(value = "/tool" , produces = "application/json;charset=utf-8")
    @AuthToken
    public String updateTool(long toolId, long toolCount){
        // TODO: 等待实现
        return "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"修改成功\",\n" +
                "    \"data\": {\n" +
                "        \"status\": \"修改成功\"\n" +
                "    }\n" +
                "}";
    }

    @PostMapping(value = "/tool" , produces = "application/json;charset=utf-8")
    @AuthToken
    public String createTool(String toolName, long toolCount){
        // TODO: 等待实现
        return "{\n" +
                "    \"code\": 201,\n" +
                "    \"message\": \"创建成功\",\n" +
                "    \"data\": {\n" +
                "        \"status\": \"创建成功\"\n" +
                "    }\n" +
                "}";
    }

    @DeleteMapping(value = "/tool" , produces = "application/json;charset=utf-8")
    @AuthToken
    public String deleteTool(long toolId){
        // TODO: 等待实现
        return "{\n" +
                "    \"code\": 204,\n" +
                "    \"message\": \"删除成功\",\n" +
                "    \"data\": {\n" +
                "        \"status\": \"删除成功\"\n" +
                "    }\n" +
                "}";
    }

    @GetMapping(value = "/student-list", produces = "application/json;charset=utf-8")
    @AuthToken
    public String getStudentList(){
        // TODO: 等待实现
        return "{\n" +
                "  \"code\": 200,\n" +
                "  \"message\": \"学生列表\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"studentId\": \"201825070120\",\n" +
                "      \"name\": \"张三\",\n" +
                "      \"gender\": \"男\",\n" +
                "      \"phone\": \"13128866666\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"studentId\": \"201825070120\",\n" +
                "      \"name\": \"张三\",\n" +
                "      \"gender\": \"男\",\n" +
                "      \"phone\": \"13128866666\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"studentId\": \"201825070120\",\n" +
                "      \"name\": \"张三\",\n" +
                "      \"gender\": \"男\",\n" +
                "      \"phone\": \"13128866666\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    @DeleteMapping(value = "/student" , produces = "application/json;charset=utf-8")
    @AuthToken
    public String deleteStudent(String studentId){
        // TODO: 等待实现
        return "{\n" +
                "    \"code\": 204,\n" +
                "    \"message\": \"删除成功\",\n" +
                "    \"data\": {\n" +
                "        \"status\": \"删除成功\"\n" +
                "    }\n" +
                "}";
    }

    @GetMapping(value = "/worker-list", produces = "application/json;charset=utf-8")
    @AuthToken
    public String getWorkerList(){
        // TODO: 等待实现
        return "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"职员列表\",\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"workId\": \"201825070120\",\n" +
                "            \"name\": \"张三\",\n" +
                "            \"gender\": \"男\",\n" +
                "            \"phone\": \"13128866666\",\n" +
                "            \"joinDate\": \"2020-08-09T20:36:26.000Z\",\n" +
                "            \"orderNumber\": 120,\n" +
                "            \"details\": \"的撒娇花费很少看到哈克贺卡圣诞贺卡计划\",\n" +
                "            \"avgGrade\": 5,\n" +
                "            \"state\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"workId\": \"201825070120\",\n" +
                "            \"name\": \"张三\",\n" +
                "            \"gender\": \"男\",\n" +
                "            \"phone\": \"13128866666\",\n" +
                "            \"joinDate\": \"2020-08-09T20:36:26.000Z\",\n" +
                "            \"orderNumber\": 120,\n" +
                "            \"details\": \"的撒娇花费很少看到哈克贺卡圣诞贺卡计划\",\n" +
                "            \"avgGrade\": 5,\n" +
                "            \"state\": 1\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    @PostMapping(value = "/sign-up-w" , produces = "application/json;charset=utf-8")
    @AuthToken
    public String signUpWorker(String workId, String password, String phone, String gender, String details){
        // TODO: 等待实现
        return "{\n" +
                "    \"code\": 201,\n" +
                "    \"message\": \"注册成功\",\n" +
                "    \"data\": {\n" +
                "        \"status\": \"注册成功\"\n" +
                "    }\n" +
                "}";
    }

    @DeleteMapping(value = "/worker" , produces = "application/json;charset=utf-8")
    @AuthToken
    public String deleteWorker(String workId){
        // TODO: 等待实现
        return "{\n" +
                "    \"code\": 204,\n" +
                "    \"message\": \"删除成功\",\n" +
                "    \"data\": {\n" +
                "        \"status\": \"删除成功\"\n" +
                "    }\n" +
                "}";
    }

    @GetMapping(value = "/worker", produces = "application/json;charset=utf-8")
    @AuthToken
    public String getWorker(String workId){
        // TODO: 等待实现
        return "{\n" +
                " \"code\": 200,\n" +
                " \"message\": \"工人详情页\",\n" +
                " \"data\": {\n" +
                "     \"workId\": \"201825070120\",\n" +
                "     \"name\": \"张三\",\n" +
                "     \"gender\": \"男\",\n" +
                "     \"joinDate\": \"2020-08-09T20:36:26.000Z\",\n" +
                "     \"phone\": \"13128866666\",\n" +
                "     \"details\": \"工人的详细介绍信息\",\n" +
                "     \"avgGrade\": 5,\n" +
                "     \"ordersNumber\": 15,\n" +
                "     \"ordersNumberToday\": 1,\n" +
                "     \"type\": {\n" +
                "         \"网络\": 10,\n" +
                "         \"水电\": 25,\n" +
                "         \"其它\": 30\n" +
                "     },\n" +
                "     \"thisMonth\": [\n" +
                "         {\n" +
                "             \"date\": \"2020-08-09T20:36:26.000Z\",\n" +
                "             \"grade\": 4.2\n" +
                "         },\n" +
                "         {\n" +
                "             \"date\": \"2020-08-09T20:36:26.000Z\",\n" +
                "             \"grade\": 3.2\n" +
                "         },\n" +
                "         {\n" +
                "             \"date\": \"2020-08-09T20:36:26.000Z\",\n" +
                "             \"grade\": 2.2\n" +
                "         }\n" +
                "     ]\n" +
                " }\n" +
                "}";
    }

    @GetMapping(value = "/statistics", produces = "application/json;charset=utf-8")
    @AuthToken
    public String getStatistics(){
        // TODO: 等待实现
        return "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"统计页\",\n" +
                "    \"data\": {\n" +
                "        \"ordersNumber\": 15,\n" +
                "        \"ordersNumber_0\": 2,\n" +
                "        \"ordersNumber_1\": 2,\n" +
                "        \"ordersNumber_2\": 2,\n" +
                "        \"thisMonth\": [\n" +
                "            {\n" +
                "                \"date\": \"2020-08-09T20:36:26.000Z\",\n" +
                "                \"grade\": 4.2\n" +
                "            },\n" +
                "            {\n" +
                "                \"date\": \"2020-08-09T20:36:26.000Z\",\n" +
                "                \"grade\": 3.2\n" +
                "            },\n" +
                "            {\n" +
                "                \"date\": \"2020-08-09T20:36:26.000Z\",\n" +
                "                \"grade\": 2.2\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
    }

    @GetMapping(value = "/massage-list", produces = "application/json;charset=utf-8")
    @AuthToken
    public String getMassageList(){
        // TODO: 等待实现
        return "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"消息中心\",\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"massageId\": 15,\n" +
                "            \"name\": \"发布者名字\",\n" +
                "            \"createDate\": \"2020-08-09T20:36:26.000Z\",\n" +
                "            \"massage\": \"消息中心\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"massageId\": 16,\n" +
                "            \"name\": \"发布者名字\",\n" +
                "            \"createDate\": \"2020-08-09T20:36:26.000Z\",\n" +
                "            \"massage\": \"消息中心\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    @PostMapping(value = "/massage" , produces = "application/json;charset=utf-8")
    @AuthToken
    public String pushMassage(String workId, String massage){
        // TODO: 等待实现
        return "{\n" +
                "    \"code\": 201,\n" +
                "    \"message\": \"创建成功\",\n" +
                "    \"data\": {\n" +
                "        \"status\": \"创建成功\"\n" +
                "    }\n" +
                "}";
    }
}
