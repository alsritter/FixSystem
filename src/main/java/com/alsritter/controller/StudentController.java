package com.alsritter.controller;

import com.alsritter.annotation.AllParamNotNull;
import com.alsritter.annotation.AuthImageCode;
import com.alsritter.annotation.AuthToken;
import com.alsritter.annotation.ParamNotNull;
import com.alsritter.model.ResponseTemplate;
import com.alsritter.pojo.Equipment;
import com.alsritter.pojo.Orders;
import com.alsritter.pojo.Student;
import com.alsritter.pojo.Worker;
import com.alsritter.services.*;
import com.alsritter.utils.BizException;
import com.alsritter.utils.CommonEnum;
import com.alsritter.utils.ConstantKit;
import com.alsritter.utils.MyDBError;
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
@RequestMapping("/student")
public class StudentController {

    private StudentService studentService;
    private UserService userService;
    private OrdersService ordersService;
    private WorkerService workerService;
    private EquipmentService equipmentService;

    @Autowired
    public void setEquipmentService(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @Autowired
    public void setWorkerService(WorkerService workerService) {
        this.workerService = workerService;
    }

    @Autowired
    public void setOrdersService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/login")
    @AllParamNotNull
    @AuthImageCode
    public ResponseTemplate<JSONObject> login(
            String codevalue,
            String uuid,
            String studentId,
            String password
    ) {

        // 先查询数据
        Student user = studentService.loginStudent(studentId, password);
// 先创建对象，下面分别赋值
        JSONObject result = new JSONObject();
        String token = userService.createToken(user, ConstantKit.UserKey.STUDENT);
        result.put("studentId", user.getId());
        result.put("name", user.getName());
        result.put("gender", user.getGender());
        result.put("phone", user.getPhone());
        result.put("token", token);
        return ResponseTemplate.<JSONObject>builder()
                .code(HttpServletResponse.SC_OK)
                .message("登陆成功")
                .data(result)
                .build();

    }


    /**
     * 就是先通过前端传过来的 uuid 生成验证码，然后存到 redis 里面
     * 等注册请求过来时，要携带之前的那个 uuid 以及输入的验证码值去
     * 检查 redis 是否存在这个 code 存在则可以注册，否则报错
     * <p>
     * 然后还需要验证手机号码格式正确性
     */
    @PostMapping("/sign-up")
    @AllParamNotNull
    @AuthImageCode
    public ResponseTemplate<JSONObject> signUpStudent(
            String codevalue,
            String uuid,
            String studentId,
            String name,
            String password,
            String phone,
            String gender
    ) {
        int i = 0;
        try {
            // 因为事务的特性需要在 Controller 里处理错误，而非在 Service
            i = studentService.signUpStudent(studentId, name, password, phone, gender);
        } catch (RuntimeException e) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR, e);
        }

        if (i == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR);
        }

        // 如果成功插入则直接调用上面的登陆方法
        return login(codevalue, uuid, studentId, password);
    }

    // 取得自己
    @GetMapping("/user")
    @AllParamNotNull
    @AuthToken
    public ResponseTemplate<JSONObject> getSelf(HttpServletRequest request) {
        Student student = studentService.getStudent(userService.getId(request));
        JSONObject result = new JSONObject();
        result.put("status", "获取成功");
        result.put("id", student.getId());
        result.put("name", student.getName());
        result.put("gender", student.getGender());
        result.put("phone", student.getPhone());
        return ResponseTemplate.<JSONObject>builder()
                .code(200)
                .message("获取成功")
                .data(result)
                .build();
    }

    @PatchMapping("/user")
    @AllParamNotNull
    @AuthToken
    public ResponseTemplate<JSONObject> updateStudent(HttpServletRequest request, String gender, String phone, String name) {

        int i = studentService.updateStudent(userService.getId(request), gender, name, phone);
        JSONObject result = new JSONObject();

        if (i == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR);
        } else {
            result.put("status", "修改成功");
            return ResponseTemplate.<JSONObject>builder()
                    .code(200)
                    .message("修改成功")
                    .data(result)
                    .build();
        }
    }


    @PostMapping("/order")
    @ParamNotNull(params = {"faultClass", "address"})
    @AuthToken
    public ResponseTemplate<JSONObject> createOrder(
            HttpServletRequest request,
            String faultClass,
            String address,
            String contacts,
            String studentPhone,
            String faultDetails,
            String images,
            Integer eid
    ) {

        // 先检查 contacts 和 studentPhone 参数是否为空，如果为空则查找当前用户的账号密码给他
        Student student = studentService.getStudent(userService.getId(request));
        JSONObject result = new JSONObject();
        StringBuilder urls = new StringBuilder();
        // 大于 3字符才算有图片
        if (images.length() > 3) {
            log.debug(images);
            String[] strArr = images.split(",");


            // 这里处理成 http 地址
            for (String s : strArr) {
                s = s.replaceAll(" ", "");
                urls.append("/extStatic/").append(s).append(" ");
            }
            log.info(urls.toString());
        } else {
            urls.append(" ");
        }

        if (contacts == null) {
            contacts = student.getName();
        }

        if (studentPhone == null) {
            studentPhone = student.getPhone();
        }

        int i = 0;
        try {
            i = ordersService.createOrder(
                    (String) request.getAttribute(ConstantKit.REQUEST_CURRENT_KEY),
                    faultClass,
                    address,
                    contacts,
                    studentPhone,
                    faultDetails,
                    urls.toString().trim(), // 去掉首尾的空格
                    eid);
        } catch (MyDBError error) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR, error);
        }

        if (i == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR);
        } else {
            result.put("status", "订单发起成功");
            return ResponseTemplate.<JSONObject>builder()
                    .code(CommonEnum.CREATED.getResultCode())
                    .message(CommonEnum.CREATED.getResultMsg())
                    .data(result)
                    .build();
        }
    }


    @PostMapping("/order-qt")
    @ParamNotNull(params = {"id"})
    @AuthToken
    public ResponseTemplate<String> createEquipmentOrder(
            HttpServletRequest request,
            Integer id,
            String contacts,
            String studentPhone,
            String faultDetails,
            String images) {
        Equipment equipment = equipmentService.getEquipment(id);
        createOrder(request, faultDetails, equipment.getAddress(), contacts, studentPhone, faultDetails, images, id);

        return ResponseTemplate
                .<String>builder()
                .code(CommonEnum.CREATED.getResultCode())
                .message("报修设备成功")
                .data("报修设备成功")
                .build();
    }


    @GetMapping("/order")
    @AuthToken
    public ResponseTemplate<List<Orders>> getOrder(HttpServletRequest request) {
        List<Orders> ordersOfUser = ordersService.getOrdersOfStudent(userService.getId(request));

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
    public ResponseTemplate<JSONObject> endOrder(
            HttpServletRequest request,
            long fixTableId,
            String message,
            Integer grade) {
        // resultDetails 可以为空
        if (message == null || message.isEmpty()) {
            message = "学生未评价";
        }

        if (grade == null) {
            grade = 10;
        }

        if (grade > 10 || grade < 0) {
            grade = 10;
        }

        int i = ordersService.endOrder(userService.getId(request), fixTableId, message, grade);
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
    @AllParamNotNull
    public ResponseTemplate<List<Orders>> getHistoryOrder(HttpServletRequest request) {
        // 获取当前学生 id
        String id = userService.getId(request);
        List<Orders> studentHistoryListDetail = ordersService.getStudentHistoryList(id);
        return ResponseTemplate
                .<List<Orders>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("历史订单")
                .data(studentHistoryListDetail)
                .build();
    }

    @GetMapping("/order-pass")
    @AuthToken
    @AllParamNotNull
    public ResponseTemplate<Orders> getHistoryOrderDetail(long fixTableId) {
        Orders studentHistoryDetail = ordersService.getOrder(fixTableId);
        return ResponseTemplate.<Orders>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message(CommonEnum.SUCCESS.getResultMsg())
                .data(studentHistoryDetail)
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

    @GetMapping("/worker-info")
    @AllParamNotNull
    @AuthToken
    public ResponseTemplate<JSONObject> getWorker(String id) {
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


