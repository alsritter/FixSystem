package com.alsritter.controller;

import com.alsritter.annotation.AllParamNotNull;
import com.alsritter.annotation.AuthImageCode;
import com.alsritter.annotation.AuthToken;
import com.alsritter.model.ResponseTemplate;
import com.alsritter.pojo.*;
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

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService;
    private StudentService studentService;
    private OrdersService ordersService;
    private WorkerService workerService;
    private UserService userService;
    private ToolService toolService;
    private MessageService messageService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Autowired
    public void setToolService(ToolService toolService) {
        this.toolService = toolService;
    }

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


    // 取得自己
    @GetMapping("/user")
    @AuthToken
    public ResponseTemplate<JSONObject> getSelf(HttpServletRequest request) {
        Admin admin = adminService.getSelf(userService.getId(request));
        JSONObject result = new JSONObject();
        result.put("status", "获取成功");
        result.put("id",admin.getId());
        result.put("name",admin.getName());
        result.put("gender",admin.getGender());
        result.put("phone",admin.getPhone());
        result.put("joinDate",admin.getJoinDate());
        result.put("details",admin.getDetails());
        return ResponseTemplate.<JSONObject>builder()
                .code(200)
                .message("获取成功")
                .data(result)
                .build();
    }

    @GetMapping("/order-list")
    @AllParamNotNull
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
    @AllParamNotNull
    @AuthToken
    public ResponseTemplate<Orders> getOrder(long fixTableId) {
        Orders orders = ordersService.getOrder(fixTableId);
        return ResponseTemplate
                .<Orders>builder()
                .code(HttpServletResponse.SC_OK)
                .message("订单详情")
                .data(orders)
                .build();
    }


    @DeleteMapping("/order")
    @AllParamNotNull
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
    @AllParamNotNull
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

    @PatchMapping("/user")
    @AuthToken
    @AllParamNotNull
    public ResponseTemplate<JSONObject> updateUser(HttpServletRequest request, String phone, String gender) {
        String id = userService.getId(request);
        int i = adminService.updateUser(id, phone, gender);
        if (i == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR);
        }
        JSONObject result = new JSONObject();
        result.put("status", CommonEnum.SUCCESS);
        return ResponseTemplate.<JSONObject>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("更新成功")
                .data(result)
                .build();
    }

    @GetMapping("/tool-list")
    @AuthToken
    public ResponseTemplate<List<Tool>> getToolList() {
        List<Tool> toolList = toolService.getToolList();
        return ResponseTemplate
                .<List<Tool>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("获取耗材列表")
                .data(toolList)
                .build();
    }

    @PatchMapping("/tool")
    @AuthToken
    @AllParamNotNull
    public ResponseTemplate<JSONObject> updateTool(int toolId, int toolCount) {
        int i = toolService.updateTool(toolId, toolCount);
        if (i == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR);
        }

        JSONObject result = new JSONObject();
        result.put("status", CommonEnum.SUCCESS);
        return ResponseTemplate.<JSONObject>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("更新成功")
                .data(result)
                .build();
    }


    @PostMapping("/tool")
    @AuthToken
    @AllParamNotNull
    public ResponseTemplate<JSONObject> createTool(String toolName, int toolCount) {
        int i = toolService.createTool(toolName, toolCount);
        if (i == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR);
        }

        JSONObject result = new JSONObject();
        result.put("status", CommonEnum.CREATED);
        return ResponseTemplate.<JSONObject>builder()
                .code(CommonEnum.CREATED.getResultCode())
                .message("更新成功")
                .data(result)
                .build();
    }

    @DeleteMapping("/tool")
    @AllParamNotNull
    @AuthToken
    public void deleteTool(int toolId) {
        int i = toolService.deleteTool(toolId);
        if (i == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR.getResultCode(), "删除工具失败（可能是 Tool 不存在）");
        }
    }

    @GetMapping("/student-list")
    @AuthToken
    public ResponseTemplate<List<JSONObject>> getStudentList() {
        List<Student> studentList = studentService.getStudentList();
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (Student student : studentList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("studentId",student.getId());
            jsonObject.put("name", student.getName());
            jsonObject.put("gender", student.getGender());
            jsonObject.put("phone", student.getPhone());
            jsonObjectList.add(jsonObject);
        }


        return ResponseTemplate
                .<List<JSONObject>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("获取学生列表")
                .data(jsonObjectList)
                .build();
    }

    @GetMapping("/worker-list")
    @AuthToken
    public ResponseTemplate<List<JSONObject>> getWorkerList() {
        List<Worker> workersList =  workerService.getWorkerList();
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (Worker worker : workersList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("workId",worker.getId());
            jsonObject.put("name", worker.getName());
            jsonObject.put("gender", worker.getGender());
            jsonObject.put("phone", worker.getPhone());
            jsonObject.put("joinDate", worker.getJoinDate());
            jsonObject.put("orderNumber", worker.getOrdersNumber());
            jsonObject.put("details", worker.getDetails());
            jsonObject.put("avgGrade", worker.getAvgGrade());
            jsonObject.put("state", worker.getState());
            jsonObjectList.add(jsonObject);
        }


        return ResponseTemplate
                .<List<JSONObject>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("获取职工列表")
                .data(jsonObjectList)
                .build();
    }

    @PostMapping("/sign-up-w")
    @AuthToken
    @AllParamNotNull
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


    @GetMapping("/worker")
    @AuthToken
    @AllParamNotNull
    public ResponseTemplate<JSONObject> getWorker(String workId) {
        Worker user = workerService.getWorker(workId);
        List<Map<String, Object>> faultClassCount = ordersService.getFaultClassCount(workId);
        List<Map<String, Object>> toMonthOrders = ordersService.getToMonthOrdersInWorker(workId);
        List<Orders> todayOrdersList = ordersService.getTodayOrdersList(workId);
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
        result.put("ordersNumberToday", ordersNumberToday);
        result.put("type", faultClassCount);
        result.put("thisMonth", toMonthOrders);

        return ResponseTemplate.<JSONObject>builder()
                .code(CommonEnum.CREATED.getResultCode())
                .message("工人详情页")
                .data(result)
                .build();
    }

    @GetMapping("/statistics")
    @AuthToken
    public ResponseTemplate<JSONObject> getStatistics() {
        List<Map<String, Object>> orderClassNumber = ordersService.getOrderClassNumber();
        List<Map<String, Object>> toMonthOrders = ordersService.getToMonthOrders();
        int orderNumber = ordersService.getOrderNumber();
        JSONObject result = new JSONObject();
        result.put("ordersNumber", orderNumber);
        orderClassNumber.forEach(o ->{
            if ((int)o.get("state") == 0) {
                result.put("ordersNumber_0", o.get("ordersNumber"));
            }else if((int)o.get("state") == 1) {
                result.put("ordersNumber_1", o.get("ordersNumber"));
            }else if((int)o.get("state") == 2) {
                result.put("ordersNumber_2", o.get("ordersNumber"));
            }
        });
        result.put("thisMonth", toMonthOrders);

        return ResponseTemplate.<JSONObject>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("统计数据")
                .data(result)
                .build();
    }

    // //工人消息中心
    @GetMapping("/massage-list")
    @AuthToken
    public ResponseTemplate<List<Message>> getMessageList(){
        List<Message> messageList = messageService.getMessageList();
        return ResponseTemplate
                .<List<Message>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("消息列表")
                .data(messageList)
                .build();
    }

    @PostMapping("/massage")
    @AuthToken
    @AllParamNotNull
    public ResponseTemplate<String> pushMassage(HttpServletRequest request,String massage) {
        String id = userService.getId(request);
        int i = messageService.pushMassage(id,massage);
        if (i == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR);
        }

        return ResponseTemplate
                .<String>builder()
                .code(CommonEnum.CREATED.getResultCode())
                .message("推送消息成功")
                .data("推送消息成功")
                .build();
    }
}
