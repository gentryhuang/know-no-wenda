package com.know.wenda.constant;

/**
 * ThreadPoolConstant
 *
 * @author hlb
 */
public class ThreadPoolConstant {

    /**
     * 线程池配置
     */
    public interface ThreadPool{
        /**
         * 核心线程数
         */
        int corePoolSize = 2;

        /**
         * 最大线程数
         */
        int maximumPoolSize = 5;
        /**
         * 最大存活时间
         */
        int keepAliveTime = 10;
        /**
         * 阻塞队列数
         */
        int blockingQueue = 5;
    }

}