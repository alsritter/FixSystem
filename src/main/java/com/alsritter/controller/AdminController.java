package com.alsritter.controller;

import com.alsritter.annotation.AllParamNotNull;
import com.alsritter.annotation.AuthImageCode;
import com.alsritter.annotation.AuthToken;
import com.alsritter.annotation.ParamNotNull;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private EquipmentService equipmentService;

    @Autowired
    public void setEquipmentService(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

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
    public ResponseTemplate<Admin> getSelf(HttpServletRequest request) {
        Admin admin = adminService.getSelf(userService.getId(request));
        // 密码不能暴露
        admin.setPassword(null);
        return ResponseTemplate.<Admin>builder()
                .code(200)
                .message("获取成功")
                .data(admin)
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

    // 这个可以不做强要求（除了名字用来占位）
    @PatchMapping("/user")
    @AuthToken
    @ParamNotNull(params = {"name"})
    public ResponseTemplate<JSONObject> updateUser(
            HttpServletRequest request,
            String phone,
            String gender,
            String name,
            String details,
            String address,
            String department,
            String email,
            String place,
            String ground,
            String idnumber
    ) {
        String id = userService.getId(request);
        int i = adminService.updateUser(new Admin(address, department, email, place, idnumber, ground, id, name, gender, null, null, phone, details));
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
    public ResponseTemplate<JSONObject> updateTool(int toolId, String toolName, int toolCount, float price) {
        int i = toolService.updateTool(toolId, toolName, toolCount, price);
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
    public ResponseTemplate<JSONObject> createTool(String toolName, int toolCount, float price) {
        int i = toolService.createTool(toolName, toolCount, price);
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
    public ResponseTemplate<String> deleteTool(int toolId) {
        int i = toolService.deleteTool(toolId);
        if (i == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR.getResultCode(), "删除工具失败（可能是 Tool 不存在）");
        }

        return ResponseTemplate
                .<String>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("删除成功")
                .data("删除成功")
                .build();
    }

    @GetMapping("/student-list")
    @AuthToken
    public ResponseTemplate<List<Student>> getStudentList() {
        List<Student> studentList = studentService.getStudentList();
        return ResponseTemplate
                .<List<Student>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("获取学生列表")
                .data(studentList)
                .build();
    }

    @GetMapping("/worker-list")
    @AuthToken
    public ResponseTemplate<List<Worker>> getWorkerList() {
        List<Worker> workersList = workerService.getWorkerList();

        return ResponseTemplate
                .<List<Worker>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("获取职工列表")
                .data(workersList)
                .build();
    }

    @PostMapping("/sign-up-w")
    @AuthToken
    @ParamNotNull(params = {"workId", "name", "password", "phone", "gender"})
    public ResponseTemplate<JSONObject> signUpWorker(
            String workId,
            String name,
            String password,
            String phone,
            String gender,
            String details,
            String url,
            String address,
            String department,
            String email,
            String place,
            String ground,
            String idnumber
    ) {

        if (url != null || url.length() > 3) {
            url = url.replaceAll(" ", "");
            url = "/extStatic/" + url;
            log.info(url);
        }

        int i = 0;
        try {
            // 在 Controller 里处理错误，而非在 Service
            i = workerService.signUpStudent(new Worker(
                    address,
                    department,
                    email,
                    place,
                    idnumber,
                    ground,
                    url,
                    workId,
                    name,
                    gender,
                    password,
                    null,
                    phone,
                    details,
                    0L,
                    0.0,
                    0
            ));
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
        result.put("address", user.getAddress());
        result.put("department", user.getDepartment());
        result.put("email", user.getEmail());
        result.put("place", user.getPlace());
        result.put("idnumber", user.getIdnumber());
        result.put("url", user.getUrl());
        result.put("ground", user.getGround());

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
        orderClassNumber.forEach(o -> {
            if ((int) o.get("state") == 0) {
                result.put("ordersNumber_0", o.get("ordersNumber"));
            } else if ((int) o.get("state") == 1) {
                result.put("ordersNumber_1", o.get("ordersNumber"));
            } else if ((int) o.get("state") == 2) {
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
    @GetMapping("/message-list")
    @AuthToken
    public ResponseTemplate<List<Message>> getMessageList() {
        List<Message> messageList = messageService.getMessageList();

        // 为了防止数据过大，这里大于 50 个字的则自动省略

        messageList.forEach(x -> {
            if (x.getMessageStr().length() > 50) {
                x.setMessageStr(x.getMessageStr().substring(0, 50) + "...");
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


    @PostMapping("/message")
    @AuthToken
    @AllParamNotNull
    public ResponseTemplate<String> pushMessage(HttpServletRequest request, String message) {
        String id = userService.getId(request);
        int i = messageService.pushMessage(id, message);
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

    @GetMapping("/search-order")
    @AuthToken
    public ResponseTemplate<List<Orders>> searchOrder(String workerId, String studentId, String name, String phone, String faultClass) {
        log.info("{}-{}-{}-{}-{}", workerId, studentId, name, phone, faultClass);
        List<Orders> orders = ordersService.searchOrder(workerId, studentId, name, phone, faultClass);
        return ResponseTemplate
                .<List<Orders>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("搜索成功")
                .data(orders)
                .build();
    }

    @GetMapping("/search-worker")
    @AuthToken
    public ResponseTemplate<List<Worker>> searchWorker(String id, String name, String phone) {
        List<Worker> workers = workerService.searchWorker(id, name, phone);
        return ResponseTemplate
                .<List<Worker>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("搜索成功")
                .data(workers)
                .build();
    }

    @GetMapping("/search-student")
    @AuthToken
    public ResponseTemplate<List<Student>> searchStudent(String id, String name, String phone) {
        List<Student> orders = studentService.searchStudent(id, name, phone);
        return ResponseTemplate
                .<List<Student>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("搜索成功")
                .data(orders)
                .build();
    }

    @PostMapping("/upload")
    public ResponseTemplate<String> uploadImage(@RequestParam("file") MultipartFile file) {
        //判断目标文件所在的目录是否存在，不存在则创建
        File folder = new File("../images/");
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
            log.info("创建文件夹 ../images/");
        }

        if (!file.isEmpty()) {
            String name = System.currentTimeMillis() +
                    file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            try (
                    // 这里随机生成名称
                    BufferedOutputStream out = new BufferedOutputStream(
                            new FileOutputStream(new File("../images/", name)))) {

                log.info(file.getName());
                out.write(file.getBytes());
                out.flush();
            } catch (IOException e) {
                throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR.getResultCode(), "上传失败", e);
            }

            log.info(name);

            return ResponseTemplate.
                    <String>builder()
                    .code(CommonEnum.SUCCESS.getResultCode())
                    .message("上传成功")
                    .data(name)
                    .build();

        } else {
            throw new BizException(CommonEnum.BAD_REQUEST.getResultCode(), "上传失败，因为文件是空的.");
        }
    }


    /**
     * 注：接收参数的 Int 类型要使用 Integer，避免不必要的错误
     * <b>
     * 注册设备的 API
     * </b>
     * <br>
     *
     * @param ename        :
     * @param eclass       :
     * @param egrade       : 设备的等级（无影响）1-5 递增
     * @param eweightGrade : 设备报修时间间隔
     * @param eworkerId    : 对接的工人ID
     * @param usingUnit    : 使用单位
     * @param etype        : 设备的型号
     * @param url          :
     * @param address      : 不能为空，因为需要扫码填表
     * @return : com.alsritter.model.ResponseTemplate<java.lang.Integer>
     */
    @PostMapping("/equipment")
    @AuthToken
    @ParamNotNull(params = {"ename", "eclass", "egrade", "eweightGrade", "eworkerId"})
    public ResponseTemplate<Integer> addEquipment(
            String ename,
            String eclass,
            Integer egrade,
            Integer eweightGrade,
            String eworkerId,
            String usingUnit,
            String etype,
            String url,
            String address) {
        int id = equipmentService.addEquipment(ename, eclass, egrade, eweightGrade, eworkerId, usingUnit, etype, url, address);
        return ResponseTemplate
                .<Integer>builder()
                .code(CommonEnum.CREATED.getResultCode())
                .message("注册成功")
                .data(id)
                .build();
    }

    @PatchMapping("/equipment")
    @AuthToken
    @ParamNotNull(params = {"id","ename"})
    public ResponseTemplate<String> updateEquipment(
            Integer id,
            String ename,
            String eclass,
            Integer egrade,
            Integer eweightGrade,
            String eworkerId,
            String usingUnit,
            String etype,
            String address
    ){
        equipmentService.updateEquipment(id,ename, eclass, egrade, eweightGrade, eworkerId, usingUnit, etype, address);
        return ResponseTemplate
                .<String>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("修改成功")
                .data("修改成功")
                .build();
    }

    @GetMapping("/equipment")
    @AuthToken
    @ParamNotNull(params = {"id"})
    public ResponseTemplate<Equipment> getEquipment(Integer id) {
        Equipment equipment = equipmentService.getEquipment(id);
        return ResponseTemplate
                .<Equipment>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("设备列表")
                .data(equipment)
                .build();
    }

    @GetMapping("/equipment-list")
    @AuthToken
    public ResponseTemplate<List<Equipment>> getEquipmentList() {
        List<Equipment> equipmentList = equipmentService.getEquipmentList();
        return ResponseTemplate
                .<List<Equipment>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("设备列表")
                .data(equipmentList)
                .build();
    }

    @GetMapping("/equipment-class")
    @AuthToken
    public ResponseTemplate<List<String>> getEquipmentClass() {
        List<String> equipmentClassList = equipmentService.getEquipmentClass();
        return ResponseTemplate
                .<List<String>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("设备类型")
                .data(equipmentClassList)
                .build();
    }

}
