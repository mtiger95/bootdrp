package com.bootdo.core.aspect;

import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.json.JSONUtil;
import com.bootdo.core.annotation.Log;
import com.bootdo.core.utils.HttpServletUtil;
import com.bootdo.core.utils.SecurityUtils;
import com.bootdo.modular.system.dao.LogDao;
import com.bootdo.modular.system.domain.LogDO;
import com.bootdo.modular.system.domain.UserDO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author L
 */
@Aspect
@Component
public class LogAspect {
    @Resource
    private LogDao logDao;

    @Pointcut("@annotation(com.bootdo.core.annotation.Log)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = point.proceed();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        // 保存日志
        saveLog(point, time);
        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogDO sysLog = new LogDO();
        Log syslog = method.getAnnotation(Log.class);
        if (syslog != null) {
            // 注解上的描述
            sysLog.setOperation(syslog.value());
        }
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");
        // 请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = JSONUtil.toJsonStr(args[0]).substring(0, 4999);
            sysLog.setParams(params);
        } catch (Exception ignored) {

        }
        // 获取 request
        HttpServletRequest request = HttpServletUtil.getRequest();
        // 设置 IP地址
        sysLog.setIp(JakartaServletUtil.getClientIP(request));
        // 用户名
        UserDO currUser = SecurityUtils.getUser();
        if (null == currUser) {
            if (null != sysLog.getParams()) {
                sysLog.setUserId(-1L);
                sysLog.setUsername(sysLog.getParams());
            } else {
                sysLog.setUserId(-1L);
                sysLog.setUsername("获取用户信息为空");
            }
        } else {
            sysLog.setUserId(SecurityUtils.getUserId());
            sysLog.setUsername(SecurityUtils.getUser().getUsername());
        }
        sysLog.setTime((int) time);
        // 系统当前时间
        Date date = new Date();
        sysLog.setGmtCreate(date);
        // 保存系统日志
        logDao.insert(sysLog);
    }
}
