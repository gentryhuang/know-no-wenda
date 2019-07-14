package com.know.wenda.constant;

/**
 * RedisConstant
 *
 * @author hlb
 */
public class RedisConstant {


    /**
     * Redis 连接配置
     */
    public interface RedisClient{

        /**
         * redis的url
         * host:192.168.25.100
         * port:6379
         * database:1
         */
        String REDIS_URL = "192.168.25.100";

        /**
         * redis url端口
         */
        int REDIS_PORT = 6379;
    }

    public interface RedisLockKey{
        /**
         * 分布式锁
         */
        String REDIS_LOCK_KEY = "redis.lock.%s";
    }

}

