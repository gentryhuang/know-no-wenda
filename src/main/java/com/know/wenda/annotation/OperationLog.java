package com.know.wenda.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 主要用来记录登陆与注册的用户的主机信息
 * OperationLog
 *
 * @author hlb
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {
    /**
     * 方法
     *
     * @return
     */
    String method();
}