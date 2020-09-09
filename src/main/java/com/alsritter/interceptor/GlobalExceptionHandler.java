package com.alsritter.interceptor;

import com.alsritter.utils.BizException;
import com.alsritter.utils.CommonEnum;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常处理类
 *
 * @author alsritter
 * @version 1.0
 **/
@ControllerAdvice
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     *
     * @param e Spring 会捕获 BizException 异常传入这个方法里
     */
    @ExceptionHandler(value = BizException.class)
    public void bizExceptionHandler(HttpServletResponse response, BizException e) throws IOException {
        log.error("发生业务异常！原因是：{}", e.getErrorMsg());
        JSONObject jsonObject = new JSONObject();
        PrintWriter out = null;
        try {
            //鉴权失败后返回的HTTP错误码，默认为401
            response.setStatus(e.getErrorCode());
            response.setContentType("application/json;charset=utf-8");
            jsonObject.put("code", e.getErrorCode());
            jsonObject.put("message", e.getErrorMsg());
            out = response.getWriter();
            out.println(jsonObject);
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * 处理空指针的异常
     *
     * @param e Spring 会捕获 BizException 异常传入这个方法里
     */
    @ExceptionHandler(value = NullPointerException.class)
    public void exceptionHandler(HttpServletResponse response, NullPointerException e) throws IOException {
        log.error("发生空指针异常！原因是: ", e);
        JSONObject jsonObject = new JSONObject();
        PrintWriter out = null;
        try {
            //鉴权失败后返回的HTTP错误码，默认为401
            response.setStatus(CommonEnum.BAD_REQUEST.getResultCode());
            response.setContentType("application/json;charset=utf-8");
            jsonObject.put("code", CommonEnum.BAD_REQUEST.getResultCode());
            jsonObject.put("message", e.getMessage() + CommonEnum.BAD_REQUEST);
            out = response.getWriter();
            out.println(jsonObject);
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * 处理其他异常
     *
     * @param e Spring 会捕获 BizException 异常传入这个方法里
     */
    @ExceptionHandler(value = Exception.class)
    public void exceptionHandler(HttpServletResponse response, Exception e) throws IOException {
        log.error("未知异常！原因是:", e);
        JSONObject jsonObject = new JSONObject();
        PrintWriter out = null;
        try {
            //鉴权失败后返回的HTTP错误码，默认为401
            response.setStatus(CommonEnum.INTERNAL_SERVER_ERROR.getResultCode());
            response.setContentType("application/json;charset=utf-8");
            jsonObject.put("code", CommonEnum.INTERNAL_SERVER_ERROR.getResultCode());
            jsonObject.put("message", CommonEnum.INTERNAL_SERVER_ERROR);
            out = response.getWriter();
            out.println(jsonObject);
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }
}
