
package com.know.wenda.system.logger;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KnwLoggerFactory
 *
 * @author hlb
 */
public class KnwLoggerFactory {

    //----------------------------------------------------应用级日志----------------------------------------------------

    /**
     * 超时日志
     */
    public final static Logger TIME_OUT_LOGGER = LoggerFactory.getLogger("TIME_OUT");

    /**
     * 异常Handler日志
     */
    public final static Logger EXCEPTION_HANDLER_LOGGER = LoggerFactory.getLogger("EXCEPTION_HANDLER");

    /**
     * 业务日志
     */
    public final static Logger BUSINESS_LOGGER = LoggerFactory.getLogger("BUSINESS");
    /**
     * 请求日志
     */
    public final static Logger REQUEST_LOGGER = LoggerFactory.getLogger("REQUEST");

    /**
     * solr数据
     */
    public final static Logger SOLR_HANDLER = LoggerFactory.getLogger("SOLR_LOG");

    /**
     * 用户操作追踪
     */
    public final static Logger OPERATION_SAVE = LoggerFactory.getLogger("OPERATION_SAVE");


    private KnwLoggerFactory() throws IllegalAccessException {throw new IllegalAccessException("Utility class");}

    /**
     * logger格式化,如果是字符串,直接输出,是对象,输出JSON格式
     * @param title
     * @param message
     * @return
     */
    public static String formatLog(String title, Object... message) {
        StringBuilder sb = new StringBuilder();
        // alert monitor 格式
        if (StringUtils.isNotBlank(title)) {
            sb.append("[").append(title).append("]。");
        }
        if (!ArrayUtils.isEmpty(message)) {
            sb.append(JSON.toJSONString(message));
        }
        return sb.toString();
    }

    /**
     *
     * @param message
     * @return
     */
    public static String format(Object... message) {
        StringBuilder sb = new StringBuilder();
        if (!ArrayUtils.isEmpty(message)) {
            sb.append(JSON.toJSONString(message));
        }
        return sb.toString();
    }



}