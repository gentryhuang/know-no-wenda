
package com.know.wenda.system.logger;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * KnwLoggerMarkers
 *
 * @author hlb
 */
public class KnwLoggerMarkers {
    //----------------------------------------------------应用级标签----------------------------------------------------

    /**
     * 超时警告
     */
    public static final Marker TIME_OUT = MarkerFactory.getMarker("timeOut");

    /**
     * 异常处理
     */
    public static final Marker EXCEPTION_HANDLER = MarkerFactory.getMarker("exceptionHandler");

    /**
     * 日常业务
     */
    public static final Marker BUSINESS_INFO = MarkerFactory.getMarker("Business");

    /**
     * solr marker
     */
    public static final Marker SOLR = MarkerFactory.getMarker("solr_log");

    /**
     * 用户追踪日志
     */
    public static final Marker OPERATION_SAVE = MarkerFactory.getMarker("OPERATION_SAVE");
}