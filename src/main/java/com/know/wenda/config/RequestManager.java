package com.know.wenda.config;

import javax.servlet.http.HttpServletRequest;

/**
 * RequestManager
 *
 * @author hlb
 */
public class RequestManager {

    private static ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<HttpServletRequest>();

    /**
     * 当前线程加入request
     *
     * @param request
     */
    public static void setHttpServletRequest(HttpServletRequest request) {
        if (request != null) {
            threadLocal.set(request);
        }
    }

    /**
     * 当前线程获取request
     */
    public static HttpServletRequest getHttpServletRequest() {
        return threadLocal.get();
    }

    /**
     * 清理request，释放空间
     */
    public static void removeHttpServletRequest() {
        threadLocal.remove();
    }

}