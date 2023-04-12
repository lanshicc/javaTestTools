package com.zhaobh.test.springboottesttool.aop;

import com.zhaobh.test.springboottesttool.entity.SysLogEntity;
import com.zhaobh.test.springboottesttool.service.SysLogService;
import com.zhaobh.test.springboottesttool.utils.HttpContextUtils;
import com.zhaobh.test.springboottesttool.utils.IPUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 系统日志，切面处理类
 */
@Aspect
@Component
public class SysLogAspect {
    @Autowired
    private SysLogService sysLogService;

    // 定义切点，切点表达式指向SysLog注解，在业务方法上加上SysLog注解，标注的方法都能进行日志记录
    @Pointcut("@annotation(com.zhaobh.test.springboottesttool.aop.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = point.proceed();
        long time = System.currentTimeMillis() - beginTime;
        // 保存日志和方法执行时长
        saveSysLog(point, time);
        return  result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLogEntity sysLogEntity = new SysLogEntity();
        // 获取方法的注释
        SysLog sysLog = method.getAnnotation(SysLog.class);
        if (sysLog != null) {
            // 实体类的用户操作设置为注释方法的value
            sysLogEntity.setOperation(sysLog.value());
        }

        // 获取目标对象
        Object target = joinPoint.getTarget();
        // 获取对象的类名称
        String className = target.getClass().getName();
        // 获取方法名称
        String methodName = signature.getName();
        // 实体类存入方法
        sysLogEntity.setMethod(className + "." + methodName + "()");

        // 获取请求的参数
        Object[] args = joinPoint.getArgs();
        String params = Arrays.toString(args);
        sysLogEntity.setParams(params);

        // 获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        // 设置IP地址
        String ipAddr = IPUtils.getIpAddr(request);

        sysLogEntity.setIp(ipAddr);
        sysLogEntity.setTime(time);
        sysLogService.save(sysLogEntity);
    }
}
