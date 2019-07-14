
package com.know.wenda.system.loggerfactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * LogMarker
 *
 * @author hlb
 */
public class LogMarker {

    //----------------------------------------------------应用级标签----------------------------------------------------
    /**
     * 超时警告
     */
    public static final Marker TIME_OUT = MarkerFactory.getMarker("timeOut");
    /**
     * 异常处理
     */
    public static final Marker EXCEPTION_HANDLER = MarkerFactory.getMarker("exceptionHandler");

    //----------------------------------------------------业务标签----------------------------------------------------
    /**
     * 日常业务
     */
    public static final Marker BUSINESS = MarkerFactory.getMarker("business");


    /**
     * 将参数设置成Marker
     *
     * @param markerName the marker name
     * @return the Marker Instance
     */
    public static Marker getMarker(String markerName) {
        if (StringUtils.isEmpty(markerName)) {
            return BUSINESS;
        }
        return MarkerFactory.getMarker(markerName);
    }

}