package com.know.wenda.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.know.wenda.execption.ConsumerException;
import com.know.wenda.system.logger.KnwLoggerFactory;
import com.know.wenda.system.logger.KnwLoggerMarkers;
import com.know.wenda.system.loggerfactory.LoggerUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.Serializable;


/**
 * LogAspect
 *
 * @author hlb
 */
@Aspect
@Component
public class TimeOutLogAspect {


    @Value("${method.timeout}")
    private int longTime;

    private Logger longTimeLogger = KnwLoggerFactory.TIME_OUT_LOGGER;

    private Logger loggerExceptionHandler = KnwLoggerFactory.EXCEPTION_HANDLER_LOGGER;

    /**
     * 对超时的方法记录日志
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("execution(public * com.know.wenda.*.*Service.*(..)) || execution(public * com.know.wenda.*.*Impl.*(..))")
    public Object aroundAll(ProceedingJoinPoint pjp) throws Throwable {
        return around(pjp, longTime);
    }

    /**
     * 记录方法的时间，捕获异常
     *
     * @param pjp      ProceedingJoinPoint
     * @param longTime 超时需记录日志的时间
     * @return 执行结果
     * @throws Throwable 异常
     */
    private Object around(ProceedingJoinPoint pjp, int longTime) throws Throwable {
        StopWatch clock = new StopWatch();
        String className = pjp.getSignature().getDeclaringTypeName();
        String methodName = pjp.getSignature().getName();
        String title = className + "." + methodName;
        Object obj = new Object();
        try {
            clock.start(); //计时开始
            obj = pjp.proceed();
            clock.stop();  //计时结束
        } catch (ConsumerException e) {
            LoggerUtil.warn(loggerExceptionHandler, KnwLoggerMarkers.EXCEPTION_HANDLER, KnwLoggerFactory.formatLog("ConsumerException", "method:", title), e);
            return obj;
        } catch (Exception e) {
            LoggerUtil.error(loggerExceptionHandler, KnwLoggerMarkers.EXCEPTION_HANDLER, KnwLoggerFactory.formatLog("Exception", "method:", title), e);
           return obj;
        }
        // 获取执行时间
        long time = clock.getTime();
        if (time >= longTime) {
            StringBuilder sb = fillParam(pjp);
            sb.append(time);
            LoggerUtil.warn(longTimeLogger, KnwLoggerMarkers.TIME_OUT, KnwLoggerFactory.formatLog("longTime", "method:", title,
                    "params:", sb.toString(), "cost_time:", time));
        }
        return obj;
    }

    /**
     * 最佳相关的参数
     * @param pjp
     * @return
     */
    private StringBuilder fillParam(ProceedingJoinPoint pjp) {
        // 拿到方法名
        String methodName = pjp.getSignature().getName();
        StringBuilder sb = new StringBuilder(200);
        // 参数列表
        Object[] params = pjp.getArgs();
        sb.append(pjp.getSignature().getDeclaringTypeName());
        sb.append(" ");
        sb.append(methodName);
        sb.append("(");
        // 封装参数
        if (params != null && params.length > 0) {
            StringBuilder sbParam = new StringBuilder();
            for (Object param : params) {
                sbParam.append(",");
                if (param instanceof Serializable) {
                    sbParam.append(JSON.toJSONString(param, SerializerFeature.IgnoreNonFieldGetter));
                } else {
                    sbParam.append(param);
                }
            }
            sb.append(sbParam.substring(1));
        }
        sb.append(")");
        return sb;
    }
}
