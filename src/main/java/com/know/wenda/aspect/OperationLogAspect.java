
package com.know.wenda.aspect;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.know.wenda.annotation.OperationLog;
import com.know.wenda.config.RequestManager;
import com.know.wenda.configuration.component.AccountInfo;
import com.know.wenda.domain.UserDO;
import com.know.wenda.dto.OpLogDTO;
import com.know.wenda.system.logger.KnwLoggerFactory;
import com.know.wenda.system.logger.KnwLoggerMarkers;
import com.know.wenda.system.loggerfactory.LoggerUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 记录操作日志
 * OperationLogAspect
 *
 * @author hlb
 */
@Component
@Aspect
public class OperationLogAspect {

    @Resource
    private AccountInfo accountInfo;

    @Pointcut(value = "@annotation(com.know.wenda.annotation.OperationLog) ")
    public void operationLog() {
    }

    @Around(value = "operationLog()")
    public Object aroundService(ProceedingJoinPoint pjp) throws Throwable {
        Object obj;
        try {
            obj = pjp.proceed();
            // 操作后记录日志
            saveOpLog(pjp);
        } catch (Throwable t) {
            throw t;
        }
        return obj;
    }

    /**
     * 记录操作日志
     * @param pjp
     */
    private void saveOpLog(ProceedingJoinPoint pjp) {
        //获取用户信息
        HttpServletRequest request = RequestManager.getHttpServletRequest();
        if (request == null) {
            return;
        }
        UserDO currentUser = accountInfo.getUser();
        if (currentUser == null) {
            return;
        }
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String method = this.getMethodAnnotation(methodSignature, pjp.getTarget().getClass());
        if (StringUtils.isBlank(method)) {
            return;
        }
        Object[] objects = pjp.getArgs();
        if (objects == null || objects.length == 0) {
            return;
        }
        List<Object> params = Lists.newArrayList();
        for(Object object : objects){
            if(object instanceof HttpServletRequest){
                continue;
            }
            params.add(object);
        }
        OpLogDTO opLogDTO = new OpLogDTO();
        opLogDTO.setOperator(accountInfo.getUser().getName());
        opLogDTO.setRequestUrl(request.getRequestURL().toString());
        opLogDTO.setMethod(method);
        String paramsStr = JSON.toJSONString(params);
        if(paramsStr.length() > 512){
            opLogDTO.setParam(paramsStr.substring(0, 500));
        }else{
            opLogDTO.setParam(paramsStr);
        }
        opLogDTO.setIp(request.getRemoteHost());
        LoggerUtil.info(KnwLoggerFactory.OPERATION_SAVE,KnwLoggerMarkers.OPERATION_SAVE,KnwLoggerFactory.formatLog("操作者信息",opLogDTO.getOperator(),opLogDTO.getIp(),opLogDTO.getRequestUrl(),opLogDTO.getMethod(),opLogDTO.getParam()));
    }

    /**
     * 取方法的注释信息.
     *
     * @param cls
     * @return the method annotation
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    String getMethodAnnotation(MethodSignature methodSignature, Class<? extends Object> cls) {
        Method method = methodSignature.getMethod();
        try {
            method = cls.getMethod(method.getName(), method.getParameterTypes());
            OperationLog operationLog = method.getAnnotation(OperationLog.class);
            if (operationLog != null) {
                return operationLog.method();
            }
        } catch (NoSuchMethodException e) {
        } catch (SecurityException e) {
        }
        return null;
    }

}