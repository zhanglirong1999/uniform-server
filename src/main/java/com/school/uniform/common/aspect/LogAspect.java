package com.school.uniform.common.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.school.uniform.util.WebUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Aspect
@Component
@Order(2)
public class LogAspect {


    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("@within(com.school.uniform.common.annotation.Log)")
    public void logType() {}

    @Around("logType()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

        Object res;
        String basicInfo = getBasicInformation(pjp);

        long startTime = System.currentTimeMillis();
        System.out.println("这是请求开始时间"+startTime);
        res = pjp.proceed();
        long endTime = System.currentTimeMillis();
        int spendTime = (int) (endTime - startTime);

        logger.info(String.format("请求成功... 请求开始时间: %d; 请求消耗时间: %d; %s", startTime, spendTime, basicInfo));
        return res;
    }

    @AfterThrowing(throwing = "e", pointcut = "logType()")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        String basicInfo = getBasicInformation(joinPoint);
        String errorMessage = e.getMessage();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();
        logger.error(String.format("请求失败... %s\n错误信息: %s\n错误StackTrace: %s", basicInfo, errorMessage, stackTrace));
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private Object getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.size() == 0) {
            return null;
        } else if (argList.size() == 1) {
            return argList.get(0);
        } else {
            return argList;
        }
    }

    private String getBasicInformation(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String urlString = request.getRequestURL().toString();
        String basePath = StrUtil.removeSuffix(urlString, URLUtil.url(urlString).getPath());
        String ip = WebUtil.getRequestIP(request);
        String requestMethod = request.getMethod();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        Object parameters = getParameter(method, joinPoint.getArgs());
        String uri = request.getRequestURI();
        String parametersString = (parameters == null)? "null": ("\n" + JSONUtil.parse(parameters).toStringPretty());
        return String.format(
                "IP: %s; 根路径: %s; 请求URL: %s; 请求URI: %s; 请求方法: %s; 请求函数名: %s; 请求参数:%s",
                ip, basePath, urlString, uri, requestMethod, methodName, parametersString
        );
    }
}
