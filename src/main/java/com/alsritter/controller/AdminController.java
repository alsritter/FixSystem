package com.alsritter.controller;

import com.alsritter.annotation.AllParamNotNull;
import com.alsritter.annotation.AuthImageCode;
import com.alsritter.annotation.AuthToken;
import com.alsritter.model.ResponseTemplate;
import com.alsritter.pojo.Admin;
import com.alsritter.pojo.Orders;
import com.alsritter.pojo.Worker;
import com.alsritter.services.AdminService;
import com.alsritter.services.OrdersService;
import com.alsritter.services.UserService;
import com.alsritter.services.WorkerService;
import com.alsritter.utils.BizException;
import com.alsritter.utils.CommonEnum;
import com.alsritter.utils.ConstantKit;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService;

    private OrdersService ordersService;
    private WorkerService workerService;
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setWorkerService(WorkerService workerService) {
        this.workerService = workerService;
    }

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

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
        Admin user = adminService.adminLogin(workId, password);
        // 再生成 Token
        String token = userService.createToken(user, ConstantKit.UserKey.ADMIN);

        JSONObject result = new JSONObject();
        result.put("workId", user.getId());
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
    public ResponseTemplate<Orders> getOrder(@RequestParam long fixTableId) {
        Orders orders = ordersService.getOrder(fixTableId);
        return ResponseTemplate
                .<Orders>builder()
                .code(HttpServletResponse.SC_OK)
                .message("订单详情")
                .data(orders)
                .build();
    }


    @DeleteMapping("/order")
    @AuthToken
    public ResponseTemplate<JSONObject> deleteOrder(long fixTableId) {
        if (ordersService.deleteOrder(fixTableId) == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR);
        }

        JSONObject result = new JSONObject();
        result.put("status", CommonEnum.DELETED);
        return ResponseTemplate.<JSONObject>builder()
                .code(CommonEnum.DELETED.getResultCode())
                .message("取消订单成功")
                .data(result)
                .build();
    }

    @GetMapping("/select-worker")
    @AuthToken
    public ResponseTemplate<List<Worker>> getLeisureWorkerList() {
        List<Worker> leisureWorkerList = workerService.getLeisureWorkerList();
        return ResponseTemplate.<List<Worker>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("获取空闲工人列表")
                .data(leisureWorkerList)
                .build();
    }

    @PatchMapping("/select-worker")
    @AuthToken
    public ResponseTemplate<JSONObject> selectLeisureWorker(
            HttpServletRequest request,
            String workId,
            long fixTableId) {
        int i = workerService.selectLeisureWorker(userService.getId(request), workId, fixTableId);
        if (i == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR);
        }

        JSONObject result = new JSONObject();
        result.put("status", CommonEnum.SUCCESS);
        return ResponseTemplate.<JSONObject>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("指定工人成功")
                .data(result)
                .build();
    }

    @PatchMapping(value = "/user", produces = "application/json;charset=utf-8")
    @AuthToken
    public String updateUser(String workId, String phone, String gender) {
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
    public String getToolList() {
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

    @PatchMapping(value = "/tool", produces = "application/json;charset=utf-8")
    @AuthToken
    public String updateTool(long toolId, long toolCount) {
        // TODO: 等待实现
        return "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"修改成功\",\n" +
                "    \"data\": {\n" +
                "        \"status\": \"修改成功\"\n" +
                "    }\n" +
                "}";
    }

    @PostMapping(value = "/tool", produces = "application/json;charset=utf-8")
    @AuthToken
    public String createTool(String toolName, long toolCount) {
        // TODO: 等待实现
        return "{\n" +
                "    \"code\": 201,\n" +
                "    \"message\": \"创建成功\",\n" +
                "    \"data\": {\n" +
                "        \"status\": \"创建成功\"\n" +
                "    }\n" +
                "}";
    }

    @DeleteMapping(value = "/tool", produces = "application/json;charset=utf-8")
    @AuthToken
    public String deleteTool(long toolId) {
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
    public String getStudentList() {
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

    @GetMapping(value = "/worker-list", produces = "application/json;charset=utf-8")
    @AuthToken
    public String getWorkerList() {
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

    @PostMapping("/sign-up-w")
    @AuthToken
    public ResponseTemplate<JSONObject> signUpWorker(
            String workId,
            String name,
            String password,
            String phone,
            String gender,
            String details
    ) {
        int i = 0;
        try {
            // 在 Controller 里处理错误，而非在 Service
            i = workerService.signUpStudent(workId, name, password, phone, gender, details);
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
        }
        if (i == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR);
        }
        JSONObject result = new JSONObject();
        result.put("status", CommonEnum.CREATED);
        return ResponseTemplate.<JSONObject>builder()
                .code(CommonEnum.CREATED.getResultCode())
                .message("录入工人成功")
                .data(result)
                .build();
    }


    @GetMapping(value = "/worker", produces = "application/json;charset=utf-8")
    @AuthToken
    public String getWorker(String workId) {
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
    public String getStatistics() {
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
    public String getMassageList() {
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

    @PostMapping(value = "/massage", produces = "application/json;charset=utf-8")
    @AuthToken
    public String pushMassage(String workId, String massage) {
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
