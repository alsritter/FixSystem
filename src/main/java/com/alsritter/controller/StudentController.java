package com.alsritter.controller;

import com.alsritter.annotation.AllParamNotNull;
import com.alsritter.annotation.AuthImageCode;
import com.alsritter.annotation.AuthToken;
import com.alsritter.annotation.ParamNotNull;
import com.alsritter.model.ResponseTemplate;
import com.alsritter.pojo.Orders;
import com.alsritter.pojo.Student;
import com.alsritter.services.OrdersService;
import com.alsritter.services.StudentService;
import com.alsritter.services.UserService;
import com.alsritter.utils.*;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {


    @Resource
    StringRedisTemplate stringTemplate;
    private Md5TokenGenerator tokenGenerator;
    private StudentService studentService;
    private UserService userService;
    private OrdersService ordersService;

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


    @Autowired
    public void setTokenGenerator(Md5TokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
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
        if (user != null) {

            ValueOperations<String, String> valueOperations = stringTemplate.opsForValue();

            // 需要先检查当前是否已经有 Token 了,如果已经有 Token 了先销毁之前的 Token,再创建新的
            String beforeToken = valueOperations.get(studentId);
            if (beforeToken != null && !beforeToken.trim().equals("")) {
                // 只需删除用 token 当 key 存的 workId,因为 workId 当 key 的那个会给覆盖掉
                stringTemplate.delete(beforeToken);
                // 还需要把那个由 token + workId 组成的用来记录创建时间的 key 删掉
                stringTemplate.delete(beforeToken + studentId);
                log.debug("删除了之前的 Token: {}", beforeToken);
            }

            // 生成新的 Token
            String token = tokenGenerator.generate(studentId, password);

            valueOperations.set(studentId, token);
            //设置 key 生存时间，当 key 过期时，它会被自动删除，时间是秒
            stringTemplate.expire(studentId, ConstantKit.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

            valueOperations.set(token, studentId);
            stringTemplate.expire(token, ConstantKit.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

            // 这一步主要是记录创建的时间，拦截器通过创建时间计算还有多久过期
            valueOperations.set(token + studentId, Long.toString(System.currentTimeMillis()));
            stringTemplate.expire(token + studentId, ConstantKit.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);


            result.put("status", "登录成功");
            result.put("studentId", user.getStudentId());
            result.put("name", user.getName());
            result.put("gender", user.getGender());
            result.put("phone", user.getPhone());
            result.put("token", token);
            return ResponseTemplate.<JSONObject>builder()
                    .code(HttpServletResponse.SC_OK)
                    .message("登陆成功")
                    .data(result)
                    .build();
        } else {
            result.put("status", "当前学生不存在");
            return ResponseTemplate.<JSONObject>builder()
                    .code(404)
                    .message("当前学生不存在")
                    .data(result)
                    .build();
        }

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

        //验证学号是否正确(只能是数字和 "-")
        if (!Pattern.compile("^-?\\d+(\\.\\d+)?$").matcher(studentId).matches()) {
            throw new BizException(CommonEnum.BAD_REQUEST.getResultCode(), "学号格式错误");
        }

        //检验是否已经有这个学生了
        if (userService.isExistRedis(studentId)) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(), "学生已经存在");
        }


        //验证手机号码正确性
        String regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
        if (!Pattern.compile(regex).matcher(phone).matches()) {
            throw new BizException(CommonEnum.BAD_REQUEST.getResultCode(), "手机号错误");
        }

        int i = 0;

        try {
            // 在 Controller 里处理错误，而非在 Service
            i = studentService.signUpStudent(studentId, name, password, phone, gender);
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
        }

        if (i == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR);
        }

        // 如果成功插入则直接调用上面的登陆方法
        return login(codevalue, uuid, studentId, password);
    }

    @PatchMapping("/user")
    @AllParamNotNull
    @AuthToken
    public ResponseTemplate<JSONObject> updateStudent(HttpServletRequest request, String name, String phone) {

        int i = studentService.updateStudent(userService.getId(request), name, phone);
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
            String faultDetails
    ) {

        // 先检查 contacts 和 studentPhone 参数是否为空，如果为空则查找当前用户的账号密码给他
        Student student = studentService.getStudent(userService.getId(request));
        JSONObject result = new JSONObject();

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
                    faultDetails);
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


    @GetMapping("/order")
    @AuthToken
    public ResponseTemplate<List<Orders>> getOrder(HttpServletRequest request){
        // 直接通过 Token 来取得数据
        String id = (String) request.getAttribute(ConstantKit.REQUEST_CURRENT_KEY);
        if (id == null){
            throw new NullPointerException(CommonEnum.UNAUTHORIZED.getResultMsg());
        }

        List<Orders> ordersOfUser = ordersService.getOrdersOfStudent(id);

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
    public ResponseTemplate<JSONObject> endOrder(HttpServletRequest request, long fixTableId, String massage, Integer grade) {
        // 先检查是否是当前工人处理的订单 和 判断当前订单是否是正在处理的 2（如果不行会自动抛出错误）
        ordersService.isExistStudent(userService.getId(request), fixTableId);

        // resultDetails 可以为空
        if (massage == null) {
            massage = "";
        }

        if (grade == null) {
            grade = 10;
        }

        if (grade > 10 || grade < 0) {
            grade = 10;
        }

        int i = ordersService.endOrder(fixTableId, massage, grade);
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
}
