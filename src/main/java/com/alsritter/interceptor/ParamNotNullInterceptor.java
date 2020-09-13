package com.alsritter.interceptor;

import com.alsritter.annotation.AllParamNotNull;
import com.alsritter.annotation.ParamNotNull;
import com.alsritter.utils.BizException;
import com.alsritter.utils.CommonEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 检查参数非空拦截器
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
public class ParamNotNullInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 先打印一下请求路径
        log.debug("请求路径为：{}", request.getRequestURI());
        // 这个 HandlerMethod 可以用来匹配 Controller，如果不是 Controller 则跳过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();


        // 如果打上了 AllParamNotNull
        if (method.getAnnotation(AllParamNotNull.class) != null ||
                handlerMethod.getBeanType().getAnnotation(AllParamNotNull.class) != null) {
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                String name = parameter.getName();

                // 注意：一些非指定的参数需要屏蔽
                if (request.getParameter(name) == null &&
                        parameter.getType() != HttpServletResponse.class &&
                        parameter.getType() != HttpServletRequest.class
                ) {
                    throw new BizException(CommonEnum.BAD_REQUEST.getResultCode(), "请求参数不能为空！！");
                }
                log.debug("参数名为：{}  参数类型为：{} 参数的值为：{}", name, parameter.getType(), request.getParameter(name));
            }
            return true;
        }

        if (method.getAnnotation(ParamNotNull.class) != null) {
            ParamNotNull noNullAnnotation = method.getAnnotation(ParamNotNull.class);

            String[] params = noNullAnnotation.params();
            String param = noNullAnnotation.param();

            // 先检查 params 是否为空，如果为空则只判断 param，否则遍历 params
            if (params.length != 0) {
                for (String s : params) {
                    String parameter = request.getParameter(s);
                    log.debug(parameter);
                    if (parameter == null) {
                        throw new BizException(CommonEnum.BAD_REQUEST.getResultCode(), "请求参数不能为空！！");
                    }
                }
                return true;
            }

            if (!param.equals("")) {
                //从httpServletRequest获取注解上指定的参数
                String obj = request.getParameter(param);
                if (null != obj) {
                    return true;
                }
                throw new BizException(CommonEnum.BAD_REQUEST.getResultCode(), "请求参数不能为空！！");
            }
        }

        return true;
    }
}
