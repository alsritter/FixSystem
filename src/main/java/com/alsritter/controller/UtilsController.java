package com.alsritter.controller;

import com.alsritter.annotation.AllParamNotNull;
import com.alsritter.model.ResponseTemplate;
import com.alsritter.services.UserService;
import com.alsritter.utils.ConstantKit;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/utils")
public class UtilsController {

    private Producer captchaProducer;

    @Autowired
    public void setCaptchaProducer(Producer captchaProducer) {
        this.captchaProducer = captchaProducer;
    }

    @Resource
    StringRedisTemplate stringTemplate;

    private UserService userService;

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
        // TODO: 处理下报错，添加事务
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
        }
    }


    @GetMapping("/is-exist")
    @AllParamNotNull
    public ResponseTemplate<Boolean> studentIsExist(@NonNull String id) {

        boolean flag = userService.isExistRedis(id);

        return ResponseTemplate.<Boolean>builder()
                .code(200)
                .message(flag ? "当前的学生存在" : "当前学生不存在")
                .data(flag)
                .build();
    }

}
