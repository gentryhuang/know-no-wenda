package com.know.wenda.system.loggerfactory;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * ExceptionPicker
 *
 * @author hlb
 */
public class ExceptionPicker {

    /**
     * 错误信息提取方法
     *
     * @param e - 异常信息
     * @return 可输出的日志
     */
    public static String pickup(Throwable e) {
        try {
            return handleException(e, null);
        } catch (Exception e1) {
            return e1.getMessage();
        }
    }

    /**
     * 错误信息提取方法（Controller层异常提取）
     *
     * @param request 请求信息
     * @param e       - 异常信息
     * @return 可输出的日志
     */
    public static String pickup(HttpServletRequest request, Throwable e) {
        try {
            return handleException(e, request);
        } catch (Exception e1) {
            return e1.getMessage();
        }
    }

    /**
     * 处理异常
     *
     * @param e - 异常信息
     * @return 可输出的日志
     */
    private static String handleException(Throwable e, HttpServletRequest request) {
        String message = e.getMessage();
        if (StringUtils.isBlank(message)) {
            return assembleMsg(message, e);
        }

        if (message.contains(ExceptionMessage.REQUIRED_PARAMETER)) {
            // 没有请求参数异常
            return assembleMsg(requireParam(message, request), e);
        }
        // 其它异常：输出当前工程package下对应的错误栈信息
        return assembleMsg(message, e);
    }



    /**
     * 没有请求参数异常
     *
     * @param message the message
     * @param request the http servlet request
     * @return the sorted message
     */
    private static String requireParam(String message, HttpServletRequest request) {
        if (request == null) {
            return message;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(message).append(ExceptionMessage.SPLIT);
        // POST/GET 1.去空格, 2.转大写 字母
        String requestMethod = (request.getMethod() + StringUtils.EMPTY).trim().toUpperCase();
        StringBuilder urlParams = new StringBuilder();
        urlParams.append(requestMethod).append(ExceptionMessage.SPACE);
        urlParams.append(request.getRequestURI()).append(ExceptionMessage.QUESTION).append(request.getQueryString());
        if (ExceptionMessage.POST.equals(requestMethod)) {
            // POST 请求时，URL上包含参数
            if (StringUtils.isNotBlank(request.getQueryString())) {
                urlParams.append(ExceptionMessage.DUBBO_AMP);
            }
            // 以下是POST参数组装
            Map<String, String[]> params = request.getParameterMap();
            for (String key : params.keySet()) {
                urlParams.append(key).append(ExceptionMessage.EQUALS);
                String[] values = params.get(key);
                int length = values.length;
                for (int i = 0; i < length; i++) {
                    urlParams.append(values[i]);
                    // 判断是否最后一个参数
                    if (i + 1 < length) {
                        urlParams.append(ExceptionMessage.DUBBO_AMP);
                    }
                }
            }
        }
        return builder.append(urlParams.toString()).toString();
    }

    /**
     * 其它异常：输出当前工程package下对应的错误栈信息
     *
     * @param message the message
     * @param e       the exception
     * @return the sorted message
     */
    private static String assembleMsg(String message, Throwable e) {
        return e.getClass().getCanonicalName() + ExceptionMessage.COLON + message + getExceptionCauseLine(e);
    }

    /**
     * 获取异常位置
     *
     * @param e
     * @return the cause line of exception
     */
    private static String getExceptionCauseLine(Throwable e) {
        StringBuilder builder = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            if (isCompanyPackage(element.getClassName())) {
                builder.append(ExceptionMessage.EXCEPTION_GT_GT).append(element.toString()).append(System.lineSeparator());
            }
        }
        return System.lineSeparator() + builder.toString();
    }

    /**
     *
     * @param name 包名
     * @return true-是, false-否
     */
    private static boolean isCompanyPackage(String name) {
        return !StringUtils.isBlank(name)
                && !name.contains(ExceptionMessage.CGLIB)
                && !name.contains(ExceptionMessage.DO_FILTER)
                && !name.contains(ExceptionMessage.ASPECT_AROUND);
    }
}
