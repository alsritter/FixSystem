package com.alsritter.interceptor;

import com.alsritter.utils.BizException;
import com.alsritter.utils.CommonEnum;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
     * api 请求类型不符异常
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public void exceptionHandler(HttpServletResponse response, HttpRequestMethodNotSupportedException e) throws IOException {
        log.error("api 请求类型不符合 当前请求的方法是: {}", e.getMethod());
        JSONObject jsonObject = new JSONObject();
        PrintWriter out = null;
        try {
            //鉴权失败后返回的HTTP错误码，默认为401
            response.setStatus(CommonEnum.FORBIDDEN.getResultCode());
            response.setContentType("application/json;charset=utf-8");
            jsonObject.put("code", CommonEnum.FORBIDDEN.getResultCode());
            jsonObject.put("message"," 请求类型不符！ 错误原因为：" + e.getMessage());
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
     * 参数读取异常
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public void exceptionHandler(HttpServletResponse response, HttpMessageNotReadableException e) throws IOException {
        log.error("参数读取异常 HTTP 请求的是: {}", e.getHttpInputMessage());
        JSONObject jsonObject = new JSONObject();
        PrintWriter out = null;
        try {
            //鉴权失败后返回的HTTP错误码，默认为401
            response.setStatus(CommonEnum.BAD_REQUEST.getResultCode());
            response.setContentType("application/json;charset=utf-8");
            jsonObject.put("code", CommonEnum.BAD_REQUEST.getResultCode());
            jsonObject.put("message"," 参数读取异常！ 错误原因为：" + e.getMessage());
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
     * 请求参数绑定到 java bean 上失败时抛出
     */
    @ExceptionHandler(value = BindException.class)
    public void exceptionHandler(HttpServletResponse response, BindException e) throws IOException {
        log.error("参数绑定到 Bean 上异常，来源于：{}  要绑定的属性名是：{}", e.getOrigin(), e.getProperty());
        JSONObject jsonObject = new JSONObject();
        PrintWriter out = null;
        try {
            //鉴权失败后返回的HTTP错误码，默认为401
            response.setStatus(CommonEnum.BAD_REQUEST.getResultCode());
            response.setContentType("application/json;charset=utf-8");
            jsonObject.put("code", CommonEnum.BAD_REQUEST.getResultCode());
            jsonObject.put("message","参数绑定异常！ 错误原因为：" + e.getMessage());
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
     * 参数转换错误，因为 Spring 会自动把前端的请求参数转成对应的数据返回到形参上，所以当前端传入错误的参数过来转换不了就会报这个错
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public void exceptionHandler(HttpServletResponse response, MethodArgumentTypeMismatchException e) throws IOException {
        log.error("参数转换错误，可能是传入的参数名和 value 位置写反了 访问的方法是：{}  参数名称为：{}", e.getParameter(), e.getName());
        JSONObject jsonObject = new JSONObject();
        PrintWriter out = null;
        try {
            //鉴权失败后返回的HTTP错误码，默认为401
            response.setStatus(CommonEnum.BAD_REQUEST.getResultCode());
            response.setContentType("application/json;charset=utf-8");
            jsonObject.put("code", CommonEnum.BAD_REQUEST.getResultCode());
            jsonObject.put("message", "参数转换错误！(可能是参数和 value 写反了)  错误的参数为：" + e.getName());
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
