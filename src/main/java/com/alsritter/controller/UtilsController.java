package com.alsritter.controller;

import com.alibaba.fastjson.JSON;
import com.alsritter.annotation.AllParamNotNull;
import com.alsritter.model.ResponseTemplate;
import com.alsritter.pojo.Equipment;
import com.alsritter.services.*;
import com.alsritter.utils.BizException;
import com.alsritter.utils.CommonEnum;
import com.alsritter.utils.ConstantKit;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/utils")
public class UtilsController {

    private RestTemplate restTemplate;

    public UtilsController() {
        this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory()); // 使用HttpClient，支持GZIP
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8)); // 支持中文编码
    }


    private Producer captchaProducer;

    private FaultService faultService;
    private UserService userService;
    private StudentService studentService;
    private OrdersService ordersService;
    private WorkerService workerService;
    private ToolService toolService;
    private EquipmentService equipmentService;

    @Autowired
    public void setEquipmentService(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @Autowired
    public void setToolService(ToolService toolService) {
        this.toolService = toolService;
    }

    @Resource
    StringRedisTemplate stringTemplate;

    @Autowired
    public void setWorkerService(WorkerService workerService) {
        this.workerService = workerService;
    }

    @Autowired
    public void setOrdersService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @Autowired
    public void setFaultService(FaultService faultService) {
        this.faultService = faultService;
    }

    @Autowired
    public void setCaptchaProducer(Producer captchaProducer) {
        this.captchaProducer = captchaProducer;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 通过前端传过来的 uuid 生成验证码，然后存到 redis 里面
     */
    @GetMapping("/code")
    @AllParamNotNull
    public void getImageCode(HttpServletResponse response, String uuid) {
        //禁止缓存
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");

        //设置响应格式为png图片
        response.setContentType("image/png");

        //为验证码创建一个文本
        String codeText = captchaProducer.createText();
        //将验证码存到redis
        stringTemplate.opsForValue().set(ConstantKit.IMAGE_CODE + uuid, codeText);
        //设置验证码 5 分钟后到期
        stringTemplate.expire(ConstantKit.IMAGE_CODE + uuid, ConstantKit.IMAGE_CODE_EXPIRE_TIME, TimeUnit.SECONDS);

        try (ServletOutputStream out = response.getOutputStream()) {
            // 用创建的验证码文本生成图片
            BufferedImage bi = captchaProducer.createImage(codeText);
            //写出图片
            ImageIO.write(bi, "png", out);
            out.flush();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR.getResultCode(), "验证码生成错误");
        }
    }


    @GetMapping("/is-exist")
    @AllParamNotNull
    public ResponseTemplate<Boolean> studentIsExist(String id) {
        boolean flag = userService.isExistRedis(id);

        return ResponseTemplate.<Boolean>builder()
                .code(200)
                .message(flag ? "当前的学生存在" : "当前学生不存在")
                .data(flag)
                .build();
    }

    @GetMapping("/fault-class")
    public ResponseTemplate<List<String>> getFaultClassList() {
        List<String> faultClassList = faultService.getFaultClassList();
        return ResponseTemplate.<List<String>>builder()
                .code(200)
                .message("错误类型列表")
                .data(faultClassList)
                .build();
    }

    @GetMapping("/get-info")
    public ResponseTemplate<JSONObject> getInfo() {
        int studentCount = studentService.getCount();
        int workerCount = workerService.getCount();
        int endOrderCount = ordersService.getEndOrderCount();
        int waitOrderCount = ordersService.getWaitOrderCount();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("studentCount", studentCount);
        jsonObject.put("workerCount", workerCount);
        jsonObject.put("endOrderCount", endOrderCount);
        jsonObject.put("waitOrderCount", waitOrderCount);

        return ResponseTemplate.<JSONObject>builder()
                .code(200)
                .message("信息")
                .data(jsonObject)
                .build();
    }


    @GetMapping("/get-month-year-count")
    public ResponseTemplate<List<Map<String, Object>>> getMonthAndYearCount() {
        List<Map<String, Object>> monthAndYearCount = ordersService.getMonthAndYearCount();
        return ResponseTemplate
                .<List<Map<String, Object>>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("获取成功")
                .data(monthAndYearCount)
                .build();
    }

    @GetMapping("/get-equipment")
    @AllParamNotNull
    public ResponseTemplate<Equipment> getEquipment(Integer id) {
        Equipment equipment = equipmentService.getEquipment(id);
        // 前面如果找不到会报 404 的错，通过这个来避免用户扫码的是其它二维码
        return ResponseTemplate
                .<Equipment>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("取得数据")
                .data(equipment)
                .build();
    }


    @GetMapping("/get-tool-number")
    public ResponseTemplate<Integer> getToolNumber() {
        return ResponseTemplate.<Integer>builder()
                .code(200)
                .message("总数")
                .data(toolService.getToolNumber())
                .build();
    }


    @GetMapping("/get-equipment-number")
    public ResponseTemplate<Integer> getEquipmentNumber() {
        return ResponseTemplate.<Integer>builder()
                .code(200)
                .message("总数")
                .data(equipmentService.getEquipmentNumber())
                .build();
    }


    @GetMapping("/get-tool-number-ratio")
    public ResponseTemplate<List<Map<String, Integer>>> getToolNumberRatio() {
        List<Map<String, Integer>> toolNumberRatio = toolService.getToolNumberRatio();
        return ResponseTemplate
                .<List<Map<String, Integer>>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("获取成功")
                .data(toolNumberRatio)
                .build();
    }

    @GetMapping("/get-tool-price-ratio")
    public ResponseTemplate<List<Map<String, Object>>> getToolPriceRatio() {
        List<Map<String, Object>> toolPriceRatio = toolService.getToolPriceRatio();
        return ResponseTemplate
                .<List<Map<String, Object>>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("获取成功")
                .data(toolPriceRatio)
                .build();
    }

    // 错误的api ，禁用
    @GetMapping("/get-tool-sum-price")
    public ResponseTemplate<Float> getToolSumPrice() {
        return ResponseTemplate.<Float>builder()
                .code(200)
                .message("总数")
                .data(toolService.getToolSumPrice())
                .build();
    }

    @GetMapping("/get-all-tool-sum-price")
    public ResponseTemplate<Double> getAllToolSumPrice() {
        return ResponseTemplate
                .<Double>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("获取成功")
                .data(toolService.getAllToolSumPrice())
                .build();
    }

    @GetMapping("/get-tool-sumprice-ratio")
    public ResponseTemplate<List<Map<String, Object>>> getToolSumPriceRatio() {
        return ResponseTemplate
                .<List<Map<String, Object>>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("获取成功")
                .data(toolService.getToolSumPriceRatio())
                .build();
    }

    @GetMapping("/get-equipment-state-ratio")
    public ResponseTemplate<List<Map<String, Object>>> getEquipmentStateRatio() {
        return ResponseTemplate
                .<List<Map<String, Object>>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("获取成功")
                .data(equipmentService.getEquipmentStateRatio())
                .build();
    }

    @GetMapping("/get-equipment-grade-ratio")
    public ResponseTemplate<List<Map<String, Object>>> getEquipmentGradeRatio() {
        return ResponseTemplate
                .<List<Map<String, Object>>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("获取成功")
                .data(equipmentService.getEquipmentGradeRatio())
                .build();
    }

    @GetMapping("/get-equipment-class-ratio")
    public ResponseTemplate<List<Map<String, Object>>> getEquipmentClassRatio() {
        return ResponseTemplate
                .<List<Map<String, Object>>>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("获取成功")
                .data(equipmentService.getEquipmentClassRatio())
                .build();
    }

    @GetMapping("/get-weather")
    public ResponseTemplate<JSONObject> getWeather() {
        Double temp = null;
        Double humidity = null;

        String url = "https://devapi.heweather.net/v7/weather/now?location={location}&key={key}";
        Map<String, Object> map = new HashMap<>();
        map.put("location", "101010100");
        map.put("key", "4298410860e442dda4620d0a3c0456bd");
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, map);
        if (response.getStatusCodeValue() == 200) {
            String now = JSON.parseObject(response.getBody()).getString("now");
            // 温度
            temp = JSON.parseObject(now).getDouble("temp");
            // 湿度
            humidity = JSON.parseObject(now).getDouble("humidity");

        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("temp", temp);
        jsonObject.put("humidity", humidity);

        return ResponseTemplate
                .<JSONObject>builder()
                .code(CommonEnum.SUCCESS.getResultCode())
                .message("获取成功")
                .data(jsonObject)
                .build();
    }

}
