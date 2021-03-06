package com.qslion.authority.security.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qslion.authority.core.entity.AuUser;
import com.qslion.authority.core.util.LoginHelper;
import com.qslion.framework.exception.GlobalExceptionHandler;
import com.qslion.framework.util.IpUtil;
import com.qslion.framework.util.JSONUtils;
import com.qslion.framework.util.RequestContextUtil;

import java.lang.reflect.Method;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * 请求参数、响应体统一日志打印
 *
 * @author Gray.Z
 * @date 2018/9/23 9:50.
 */
@Aspect
@Component
public class RestControllerAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String HEADER_TOKEN_NAME = "X-TOKEN";
    private static final String HEADER_CLIENT_NAME = "X-CLIENT";
    private static final String HEADER_APP_VERSION = "APP-VERSION";
    private static final String HEADER_API_VERSION = "API-VERSION";
    private static final String HEADER_LANG_NAME = "X-LANG";
    private static final String TENANT_HEADER_NAME = "X-TENANT-ID";

    /**
     * 环绕通知
     *
     * @param joinPoint 连接点
     * @return 切入点返回值
     * @throws Throwable 异常信息
     */
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object apiLog(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        boolean logFlag = this.needToLog(method);
        if (!logFlag) {
            //return joinPoint.proceed();
        }

        HttpServletRequest request = RequestContextUtil.getRequest();
        AuUser loginUser = LoginHelper.getLoginUser();

        String ip = IpUtil.getRealIp(request);
        String methodName = this.getMethodName(joinPoint);
        String params = this.getParamsJson(joinPoint);
        String requester = loginUser == null ? "unknown" : loginUser.getLoginId();

        String callSource = request.getHeader(HEADER_CLIENT_NAME);
        String appVersion = request.getHeader(HEADER_APP_VERSION);
        String apiVersion = request.getHeader(HEADER_API_VERSION);
        String userAgent = request.getHeader("user-agent");

        logger.info("Started request requester 【{}】 method 【{}】\n Params:【{}】\n IP： 【{}】\n CallSource: 【{}】\n AppVersion: 【{}】\n ApiVersion: 【{}】\n UserAgent: 【{}】",
                requester, methodName, params, ip, callSource, appVersion, apiVersion, userAgent);
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        logger.info("Ended request requester 【{}】 method 【{}】\n Params:【{}】\n Response :【{}】\n Time cost: 【{}】 millis ",
                requester, methodName, params, JSONUtils.writeValueAsString(result), System.currentTimeMillis() - start);
        return result;
    }

    private String getMethodName(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        String shortMethodNameSuffix = "(..)";
        if (methodName.endsWith(shortMethodNameSuffix)) {
            methodName = methodName.substring(0, methodName.length() - shortMethodNameSuffix.length());
        }
        return methodName;
    }

    private String getParamsJson(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            //移除敏感内容
            String paramStr;
            if (arg instanceof HttpServletResponse) {
                paramStr = HttpServletResponse.class.getSimpleName();
            } else if (arg instanceof HttpServletRequest) {
                paramStr = HttpServletRequest.class.getSimpleName();
            } else if (arg instanceof MultipartFile) {
                long size = ((MultipartFile) arg).getSize();
                paramStr = MultipartFile.class.getSimpleName() + " size:" + size;
            } else {
                paramStr = this.deleteSensitiveContent(arg);
            }
            sb.append(paramStr).append(",");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    /**
     * 判断是否需要记录日志
     */
    private boolean needToLog(Method method) {
        return method.getAnnotation(GetMapping.class) == null
                && !method.getDeclaringClass().equals(GlobalExceptionHandler.class);
    }

    /**
     * 删除参数中的敏感内容
     *
     * @param obj 参数对象
     * @return 去除敏感内容后的参数对象
     */
    private String deleteSensitiveContent(Object obj) {
        if (obj == null || obj instanceof Exception) {
            return Maps.newHashMap().toString();
        }
        try {
            String param = JSONUtils.writeValueAsString(obj);
            List<String> sensitiveFieldList = this.getSensitiveFieldList();
            for (String sensitiveField : sensitiveFieldList) {
                if (param != null && param.contains(sensitiveField)) {
                    param = param.replace(sensitiveField, "******");
                }
            }
            return param;
        } catch (ClassCastException e) {
            return String.valueOf(obj);
        }
    }

    /**
     * 敏感字段列表（可配置的）
     */
    private List<String> getSensitiveFieldList() {
        List<String> sensitiveFieldList = Lists.newArrayList();
        sensitiveFieldList.add("pwd");
        sensitiveFieldList.add("password");
        return sensitiveFieldList;
    }
}
