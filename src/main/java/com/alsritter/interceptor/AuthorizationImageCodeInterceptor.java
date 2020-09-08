package com.alsritter.interceptor;

import com.alsritter.annotation.AuthImageCode;
import com.alsritter.utils.ConstantKit;
import net.minidev.json.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * 用来检查验证码的拦截器
 *
 * @author alsritter
 * @version 1.0
 **/
public class AuthorizationImageCodeInterceptor implements HandlerInterceptor {
    @Resource
    StringRedisTemplate stringTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 这个 HandlerMethod 可以用来匹配 Controller，如果不是 Controller 则跳过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 如果打上了 AuthImageCode 的注解先检查是否带了 validImageCode 和 uuid 参数
        // 如果带了就验证一下
        if (method.getAnnotation(AuthImageCode.class) != null ||
                handlerMethod.getBeanType().getAnnotation(AuthImageCode.class) != null) {

            String codevalue = request.getParameter("codevalue");
            String uuid = request.getParameter("uuid");

            // 参数为空
            if (codevalue == null || uuid == null) {
                sendError(response, "The parameter cannot be null", 400);
                return false;
            }

            // 参数为空
            if (codevalue.equals("") || uuid.equals("")) {
                sendError(response, "The parameter cannot be null", 400);
                return false;
            }
            // 先验证这个 uuid 是否存在
            if (stringTemplate.opsForValue().get(ConstantKit.IMAGE_CODE + uuid) == null) {
                sendError(response, "The UUID timeout or does not exist", HttpServletResponse.SC_NOT_FOUND);
                return false;
            }

            //先判断验证码是否正确
            if (!stringTemplate.opsForValue().get(ConstantKit.IMAGE_CODE + uuid).trim().equals(codevalue)) {
                sendError(response, "image code error", HttpServletResponse.SC_FORBIDDEN);
                return false;
            }

            return true;
        }

        return true;
    }

    private void sendError(HttpServletResponse response, String massage, int code) {
        JSONObject jsonObject = new JSONObject();
        PrintWriter out = null;

        try {
            // 参数有空的 返回 400 错误
            response.setStatus(400);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            jsonObject.put("code", code);
            jsonObject.put("message", massage);
            out = response.getWriter();
            out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }
}
