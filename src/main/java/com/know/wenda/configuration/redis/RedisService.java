package com.know.wenda.configuration.redis;

import com.know.wenda.util.JedisAdapter;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import javax.annotation.Resource;

/**
 * RedisService
 *
 * @author hlb
 */
@Component
public class RedisService {

    @Resource
    private JedisAdapter jedisAdapter;

    private static final String STATUS_CODE = "OK";

    /**
     * 不存在则设置
     */
    public Long setnx(String key, String value, int expireSecond) {
        Long result;
        Jedis jedis = jedisAdapter.getJedis();
        try {
            if (expireSecond == -1) {
                result = jedis.setnx(key, value);
            } else {
                String statusCode = jedis.set(key, value, SetParams.setParams().nx().ex(expireSecond));
                //正确状态
                if (null != statusCode && statusCode.equals(STATUS_CODE)) {
                    result = 1L;
                } else {
                    result = 0L;
                }
            }
        } finally {
            close(jedis);
        }

        return result;
    }

    /**
     * 不存在则设置
     */
    public Long setnx(String key, String value) {
        return setnx(key, value, -1);
    }

    /**
     * 关闭redis
     * @param redis
     */
    public void close(Jedis redis) {
        if (redis != null) {
            redis.close();
        }
    }

}