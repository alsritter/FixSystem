package com.alsritter.controller;

import com.alsritter.annotation.AllParamNotNull;
import com.alsritter.annotation.AuthImageCode;
import com.alsritter.annotation.AuthToken;
import com.alsritter.annotation.ParamNotNull;
import com.alsritter.model.ResponseTemplate;
import com.alsritter.pojo.Message;
import com.alsritter.pojo.Orders;
import com.alsritter.pojo.Worker;
import com.alsritter.services.*;
import com.alsritter.utils.BizException;
import com.alsritter.utils.CommonEnum;
import com.alsritter.utils.ConstantKit;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/worker")
public class WorkerController {

    private WorkerService workerService;
    private OrdersService ordersService;
    private UserService userService;
    private MessageService messageService;
    private AdminService adminService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrdersService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }


    @Autowired
    public void setWorkerService(WorkerService workerService) {
        this.workerService = workerService;
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
        String token = userService.createToken(user, ConstantKit.UserKey.WORKER);
        result.put("status", "登录成功");
        result.put("workId", user.getId());
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
    }


    @GetMapping("/order")
    @AuthToken
    public ResponseTemplate<List<Orders>> getOrder(HttpServletRequest request) {
        List<Orders> ordersOfUser = ordersService.getOrdersOfWorker(userService.getId(request));

        if (ordersOfUser == null) {
            throw new BizException(CommonEnum.NOT_FOUND);
        }

        return ResponseTemplate
                .<List<Orders>>builder()
                .code(HttpServletResponse.SC_OK)
                .message("订单详情")
                .data(ordersOfUser)
                .build();
    }

    @PatchMapping("/order-end")
    @AuthToken
    @ParamNotNull(params = {"fixTableId"})
    public ResponseTemplate<JSONObject> endOrder(HttpServletRequest request, long fixTableId, String resultDetails) {
        // 先检查是否是当前工人处理的订单 和 判断当前订单是否是正在处理的 1（如果不行会自动抛出错误）
        ordersService.isExist(userService.getId(request), fixTableId);

        // resultDetails 可以为空
        if (resultDetails == null) {
            resultDetails = "当前订单评价完成";
        }

        int i = ordersService.endOrder(fixTableId, resultDetails);
        JSONObject result = new JSONObject();

        if (i == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR);
        } else {
            result.put("status", CommonEnum.SUCCESS);
            return ResponseTemplate.<JSONObject>builder()
                    .code(200)
                    .message("处理结果评价成功")
                    .data(result)
                    .build();
        }
    }

    @GetMapping("/order-list")
    @AuthToken
    public ResponseTemplate<List<JSONObject>> getWorkerHistoryOrder(HttpServletRequest request) {
        List<Orders> workerHistoryList = ordersService.getWorkerHistoryList(userService.getId(request));
        List<JSONObject> jsonObjects = new ArrayList<>();
        workerHistoryList.forEach(x -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fixTableId", x.getFixTableId());
            jsonObject.put("faultClass", x.getFaultClass());
            jsonObject.put("endTime", x.getEndTime());
            jsonObject.put("grade", x.getGrade());
            jsonObjects.add(jsonObject);
        });

        return ResponseTemplate
                .<List<JSONObject>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("历史订单")
                .data(jsonObjects)
                .build();
    }

    //工人历史订单详情
    @GetMapping("/order-pass")
    @AuthToken
    @AllParamNotNull
    public ResponseTemplate<Orders> getWorkerHistoryOrderDetail(long fixTableId) {
        Orders workerHistoryDetail = ordersService.getOrder(fixTableId);
        return ResponseTemplate
                .<Orders>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("历史订单详情")
                .data(workerHistoryDetail)
                .build();

    }

    // //工人消息中心
    @GetMapping("/message-list")
    @AuthToken
    public ResponseTemplate<List<Message>> getMessageList() {
        List<Message> messageList = messageService.getMessageList();
        // 为了防止数据过大，这里大于 50 个字的则自动省略

        messageList.forEach(x ->{
            if (x.getMessageStr().length() > 50) {
                x.setMessageStr(x.getMessageStr().substring(0,50) + "...");
            }
        });

        return ResponseTemplate
                .<List<Message>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("消息列表")
                .data(messageList)
                .build();
    }

    @GetMapping("/message")
    @AuthToken
    @AllParamNotNull
    public ResponseTemplate<Message> getMessage(long messageId) {
        Message message = messageService.getMessage(messageId);
        return ResponseTemplate
                .<Message>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("消息")
                .data(message)
                .build();
    }


    @GetMapping("/get-admin-name")
    @AuthToken
    @AllParamNotNull
    public ResponseTemplate<String> getAdminName(String workId) {
        return ResponseTemplate
                .<String>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("管理员姓名")
                .data(adminService.getName(workId))
                .build();
    }

    //工人主页
    @GetMapping("/home")
    @AuthToken
    public ResponseTemplate<JSONObject> getWorker(HttpServletRequest request) {
        String id = userService.getId(request);
        Worker user = workerService.getWorker(id);
        List<Map<String, Object>> faultClassCount = ordersService.getFaultClassCount(id);
        List<Map<String, Object>> toMonthOrders = ordersService.getToMonthOrdersInWorker(id);
        List<Orders> todayOrdersList = ordersService.getTodayOrdersList(id);
        int ordersNumberToday = 0;
        if (todayOrdersList != null) {
            ordersNumberToday = todayOrdersList.size();
        }


        JSONObject result = new JSONObject();
        result.put("workId", user.getId());
        result.put("name", user.getName());
        result.put("gender", user.getGender());
        result.put("phone", user.getPhone());
        result.put("joinDate", user.getJoinDate());
        result.put("details", user.getDetails());
        result.put("ordersNumber", user.getOrdersNumber());
        result.put("avgGrade", user.getAvgGrade());
        result.put("address", user.getAddress());
        result.put("department", user.getDepartment());
        result.put("email", user.getEmail());
        result.put("place", user.getPlace());
        result.put("idnumber", user.getIdnumber());
        result.put("url", user.getUrl());
        result.put("ground", user.getGround());
        result.put("ordersNumberToday", ordersNumberToday);
        result.put("type", faultClassCount);
        result.put("thisMonth", toMonthOrders);

        return ResponseTemplate.<JSONObject>builder()
                .code(CommonEnum.CREATED.getResultCode())
                .message("工人详情页")
                .data(result)
                .build();
    }

}
