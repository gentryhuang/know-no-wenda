
package com.know.wenda.system.loggerfactory;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.Marker;

import static com.know.wenda.system.loggerfactory.ExceptionPicker.pickup;
import static com.know.wenda.system.loggerfactory.LogMarker.getMarker;

/**
 * LoggerUtil
 *
 * @author hlb
 */
public class LoggerUtil {

    //warn、error级别
    private static final String EXCEPTION_TAG = "Exception: ";
    //异常拆分符号
    private static final String EXCEPTION_SPLIT = " ==> ";


    //-----------------------------------  统一规范的日志级别方法 ---------------------------------------

    /**
     * 超时默认方法
     *
     * @param msg 信息
     */
    public static void timeOut(String msg) {
        LogFactory.TIME_OUT_LOGGER.warn(LogMarker.TIME_OUT, msg);
    }

    /**
     * 全局捕获异常默认方法
     *
     * @param msg 信息
     */
    public static void handleException(String msg) {
        LogFactory.EXCEPTION_HANDLER_LOGGER.error(LogMarker.EXCEPTION_HANDLER, append(EXCEPTION_TAG, msg));
    }

    /**
     * 全局捕获异常默认方法
     *
     * @param e 异常信息
     */
    public static void handleException(Throwable e) {
        LogFactory.EXCEPTION_HANDLER_LOGGER.error(LogMarker.EXCEPTION_HANDLER, append(EXCEPTION_TAG, pickup(e)));
    }

    /**
     * 全局捕获异常默认方法
     *
     * @param msg 信息
     * @param e   异常信息
     */
    public static void handleException(String msg, Throwable e) {
        LogFactory.EXCEPTION_HANDLER_LOGGER.error(LogMarker.EXCEPTION_HANDLER, append(EXCEPTION_TAG, msg, EXCEPTION_SPLIT, pickup(e)));
    }

    //-----------------------------------  没有Marker参数（marker默认用方法名，找不到则用business） ---------------------------------------

    /**
     * debug级别日志
     *
     * @param log 日志
     * @param msg 信息
     */
    public static void debug(Logger log, String msg) {
        log.debug(getMarker(getMethodName()), msg);
    }

    /**
     * info级别日志
     *
     * @param log 日志
     * @param msg 信息
     */
    public static void info(Logger log, String msg) {
        log.info(getMarker(getMethodName()), msg);
    }

    /**
     * info级别日志
     *
     * @param log    日志
     * @param format the format string
     * @param arg    the first argument
     */
    public static void info(Logger log, String format, Object arg) {
        log.info(getMarker(getMethodName()), format, arg);
    }

    /**
     * info级别日志
     *
     * @param log    日志
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     */
    public static void info(Logger log, String format, Object arg1, Object arg2) {
        log.info(getMarker(getMethodName()), format, arg1, arg2);
    }

    /**
     * info级别日志
     *
     * @param log       日志
     * @param format    the format string
     * @param arguments a list of 3 or more arguments
     */
    public static void info(Logger log, String format, Object... arguments) {
        log.info(getMarker(getMethodName()), format, arguments);
    }

    /**
     * warn级别日志
     *
     * @param log 日志
     * @param msg 信息
     */
    public static void warn(Logger log, String msg) {
        log.warn(getMarker(getMethodName()), msg);
    }

    /**
     * warn级别日志
     *
     * @param log 日志
     * @param e   异常
     */
    public static void warn(Logger log, Throwable e) {
        log.warn(getMarker(getMethodName()), append(EXCEPTION_TAG, pickup(e)));
    }

    /**
     * warn级别日志
     *
     * @param log 日志
     * @param msg 信息
     * @param e   异常
     */
    public static void warn(Logger log, String msg, Throwable e) {
        log.warn(getMarker(getMethodName()), append(EXCEPTION_TAG, msg, EXCEPTION_SPLIT, pickup(e)));
    }

    /**
     * warn级别日志
     *
     * @param log    日志
     * @param format the format string
     * @param arg    the first argument
     */
    public static void warn(Logger log, String format, Object arg) {
        log.warn(getMarker(getMethodName()), format, arg);
    }

    /**
     * warn级别日志
     *
     * @param log    日志
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     */
    public static void warn(Logger log, String format, Object arg1, Object arg2) {
        log.warn(getMarker(getMethodName()), format, arg1, arg2);
    }

    /**
     * warn级别日志
     *
     * @param log       日志
     * @param format    the format string
     * @param arguments a list of 3 or more arguments
     */
    public static void warn(Logger log, String format, Object... arguments) {
        log.warn(getMarker(getMethodName()), format, arguments);
    }

    /**
     * error级别日志
     *
     * @param log 日志
     * @param msg 信息
     */
    public static void error(Logger log, String msg) {
        log.error(getMarker(getMethodName()), append(EXCEPTION_TAG, msg));
    }

    /**
     * error级别日志
     *
     * @param log 日志
     * @param e   异常
     */
    public static void error(Logger log, Throwable e) {
        log.error(getMarker(getMethodName()), append(EXCEPTION_TAG, pickup(e)));
    }

    /**
     * error级别日志
     *
     * @param log 日志
     * @param msg 信息
     * @param e   异常
     */
    public static void error(Logger log, String msg, Throwable e) {
        log.error(getMarker(getMethodName()), append(EXCEPTION_TAG, msg, EXCEPTION_SPLIT, pickup(e)));
    }

    /**
     * error级别日志
     *
     * @param log    日志
     * @param format the format string
     * @param arg    the first argument
     */
    public static void error(Logger log, String format, Object arg) {
        log.error(getMarker(getMethodName()), append(EXCEPTION_TAG, format), arg);
    }

    /**
     * error级别日志
     *
     * @param log    日志
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     */
    public static void error(Logger log, String format, Object arg1, Object arg2) {
        log.error(getMarker(getMethodName()), append(EXCEPTION_TAG, format), arg1, arg2);
    }

    /**
     * error级别日志
     *
     * @param log       日志
     * @param format    the format string
     * @param arguments a list of 3 or more arguments
     */
    public static void error(Logger log, String format, Object... arguments) {
        log.error(getMarker(getMethodName()), append(EXCEPTION_TAG, format), arguments);
    }

    /**
     * error级别日志
     *
     * @param log       日志
     * @param e         异常
     * @param format    the format string
     * @param arguments a list of 3 or more arguments
     */
    public static void error(Logger log, Throwable e, String format, Object... arguments) {
        log.error(getMarker(getMethodName()), append(EXCEPTION_TAG, format, EXCEPTION_SPLIT, pickup(e)), arguments);
    }

    //-----------------------------------  带Marker参数 ---------------------------------------

    /**
     * debug级别日志
     *
     * @param log 日志
     * @param msg 信息
     */
    public static void debug(Logger log, Marker marker, String msg) {
        log.debug(marker, msg);
    }

    /**
     * info级别日志
     *
     * @param log    日志
     * @param marker 标签
     * @param msg    信息
     */
    public static void info(Logger log, Marker marker, String msg) {
        log.info(marker, msg);
    }

    /**
     * info级别日志
     *
     * @param log    日志
     * @param marker 标签
     * @param format the format string
     * @param arg    the first argument
     */
    public static void info(Logger log, Marker marker, String format, Object arg) {
        log.info(marker, format, arg);
    }

    /**
     * info级别日志
     *
     * @param log    日志
     * @param marker 标签
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     */
    public static void info(Logger log, Marker marker, String format, Object arg1, Object arg2) {
        log.info(marker, format, arg1, arg2);
    }

    /**
     * info级别日志
     *
     * @param log       日志
     * @param marker    标签
     * @param format    the format string
     * @param arguments a list of 3 or more arguments
     */
    public static void info(Logger log, Marker marker, String format, Object... arguments) {
        log.info(marker, format, arguments);
    }

    /**
     * warn级别日志
     *
     * @param log    日志
     * @param marker 标签
     * @param msg    信息
     */
    public static void warn(Logger log, Marker marker, String msg) {
        log.warn(marker, msg);
    }

    /**
     * warn级别日志
     *
     * @param log    日志
     * @param marker 标签
     * @param e      异常
     */
    public static void warn(Logger log, Marker marker, Throwable e) {
        log.warn(marker, append(EXCEPTION_TAG, pickup(e)));
    }

    /**
     * warn级别日志
     *
     * @param log    日志
     * @param marker 标签
     * @param msg    信息
     * @param e      异常
     */
    public static void warn(Logger log, Marker marker, String msg, Throwable e) {
        log.warn(marker, append(EXCEPTION_TAG, msg, EXCEPTION_SPLIT, pickup(e)));
    }

    /**
     * warn级别日志
     *
     * @param log    日志
     * @param marker 标签
     * @param format the format string
     * @param arg    the first argument
     */
    public static void warn(Logger log, Marker marker, String format, Object arg) {
        log.warn(marker, format, arg);
    }

    /**
     * warn级别日志
     *
     * @param log    日志
     * @param marker 标签
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     */
    public static void warn(Logger log, Marker marker, String format, Object arg1, Object arg2) {
        log.warn(marker, format, arg1, arg2);
    }

    /**
     * warn级别日志
     *
     * @param log       日志
     * @param marker    标签
     * @param format    the format string
     * @param arguments a list of 3 or more arguments
     */
    public static void warn(Logger log, Marker marker, String format, Object... arguments) {
        log.warn(marker, format, arguments);
    }

    /**
     * error级别日志
     *
     * @param log    日志
     * @param marker 标签
     * @param msg    信息
     */
    public static void error(Logger log, Marker marker, String msg) {
        log.error(marker, append(EXCEPTION_TAG, msg));
    }

    /**
     * error级别日志
     *
     * @param log    日志
     * @param marker 标签
     * @param e      异常
     */
    public static void error(Logger log, Marker marker, Throwable e) {
        log.error(marker, append(EXCEPTION_TAG, pickup(e)));
    }

    /**
     * error级别日志
     *
     * @param log    日志
     * @param marker 标签
     * @param msg    信息
     * @param e      异常
     */
    public static void error(Logger log, Marker marker, String msg, Throwable e) {
        log.error(marker, append(EXCEPTION_TAG, msg, EXCEPTION_SPLIT, pickup(e)));
    }

    /**
     * error级别日志
     *
     * @param log    日志
     * @param marker 标签
     * @param format the format string
     * @param arg    the first argument
     */
    public static void error(Logger log, Marker marker, String format, Object arg) {
        log.error(marker, append(EXCEPTION_TAG, format), arg);
    }

    /**
     * error级别日志
     *
     * @param log    日志
     * @param marker 标签
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     */
    public static void error(Logger log, Marker marker, String format, Object arg1, Object arg2) {
        log.error(marker, append(EXCEPTION_TAG, format), arg1, arg2);
    }

    /**
     * error级别日志
     *
     * @param log       日志
     * @param marker    标签
     * @param format    the format string
     * @param arguments a list of 3 or more arguments
     */
    public static void error(Logger log, Marker marker, String format, Object... arguments) {
        log.error(marker, append(EXCEPTION_TAG, format), arguments);
    }

    /**
     * error级别日志
     *
     * @param log       日志
     * @param marker    标签
     * @param e         异常
     * @param format    the format string
     * @param arguments a list of 3 or more arguments
     */
    public static void error(Logger log, Marker marker, Throwable e, String format, Object... arguments) {
        log.error(marker, append(EXCEPTION_TAG, format, EXCEPTION_SPLIT, pickup(e)), arguments);
    }

    /**
     * 打印白名单信息
     *
     * @param log       日志
     * @param whiteList  白名单列表
     * @param name      需要简要的参数
     * @param obj       需要打印的对象
     */
    public static void printWhiteListInfo(Logger log, String whiteList, String name, Object... obj) {
        try {
            if (StringUtils.contains(whiteList, name)) {
                log.info(getMarker(getMethodName()), JSON.toJSONString(obj));
            }
        } catch (Exception e) {
            //不处理
        }
    }

    //------------------------- private method -------------------------

    /**
     * 获取调用当前方法的名称
     *
     * @return 调用日志方法的对象信息（类:行数 方法名）
     */
    private static String getMethodName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length >= 4) {
            return stackTraceElements[3].getMethodName();
        } else {
            return "";
        }
    }

    /**
     * 获取调用当前方法的对象信息
     *
     * @return 调用日志方法的对象信息（类:行数 方法名）
     */
    private static String append(String... strings) {
        StringBuilder builder = new StringBuilder();
        if (strings == null) {
            return "";
        }
        for (String str : strings) {
            if (StringUtils.isNotEmpty(str)) {
                builder.append(str);
            }
        }
        return builder.toString();
    }

}