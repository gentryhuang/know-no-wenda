package com.know.wenda.constant;

/**
 * TimeConstant
 *
 * @author hlb
 */
public class TimeConstant {

    /**
     * 缓存过期时间
     */
    public interface CacheExpireTime {

        /**
         * 7天
         */
        Integer EXPIRE_SECOND_HOME_TIME = 60 * 60 * 24 * 7;
        /**
         * 一天
         */
        int EXPIRE_TIME_ONE_DAY = 24 * 60 * 60;
        /**
         * 半天
         */
        int EXPIRE_TIME_HALF_DAY = 12 * 60 * 60;
        /**
         * 2小时
         */
        int TWO_HOUR = 2 * 60 * 60;
        /**
         * 10分钟
         */
        int TEN_MINUTES = 10 * 60;
        /**
         * 1分钟
         */
        int ONE_MINUTES = 60;
        /**
         * 2分钟
         */
        int TWO_MINUTES = 2 * 60;
        /**
         * 半分钟
         */
        int HELF_ONE_MINUTES = 30;
        /**
         * 30分钟
         */
        int THIRTY_MINUTES = 30 * 60;
        /**
         * 10s
         */
        int TEN_SECONDS = 10;

        /**
         * 1s
         */
        int ONE_SECONDS = 1;
    }

    /**
     * 阿里云图片存储时间
     */
    public interface AlyImageExpire{
        /**
         * 一年
         */
        Long ALIYUN_IMAGE = 3600 * 1000 * 24 * 365L;
    }

}