package com.know.wenda.util;

import com.know.wenda.constant.ThreadPoolConstant;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolUtil
 *
 * @author hlb
 */
public class ThreadPoolUtil {

    /**
     * 线程池配置
     */
    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            ThreadPoolConstant.ThreadPool.corePoolSize,
            ThreadPoolConstant.ThreadPool.maximumPoolSize,
            ThreadPoolConstant.ThreadPool.keepAliveTime,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(ThreadPoolConstant.ThreadPool.blockingQueue),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    public static synchronized ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }
}