package com.know.wenda.util;

import com.alibaba.fastjson.JSON;
import com.know.wenda.constant.RedisConstant;
import com.know.wenda.domain.TokenDO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;


/**
 * JedisAdapter
 *
 * @author hlb
 */
@Component
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool;

    /**
     * 初始化JedisPool
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet()  {

        JedisPoolConfig config = new JedisPoolConfig();

        /**
         * 设置最大空闲连接数，默认8
         */
        config.setMaxIdle(20);
        /**
         * 最小空闲连接数 默认0
         */
        config.setMinIdle(10);
        /**
         * 设置最大连接数，默认8
         */
        config.setMaxTotal(200);
        /**
         * 逐出扫描的时间间隔(毫秒)
         */
        config.setTimeBetweenEvictionRunsMillis(1000);

        //在获取连接时检查有效性，默认为false
        config.setTestOnBorrow(true);

        // Pool设置
        pool = new JedisPool(config, RedisConstant.RedisClient.REDIS_URL, RedisConstant.RedisClient.REDIS_PORT, Protocol.DEFAULT_TIMEOUT, null, 1);

       // pool = new JedisPool(RedisConstant.RedisClient.REDIS_URL);
    }
    /**
     * 从jedis池中取出redis
     *
     * @return
     */
    public Jedis getJedis() {
        return pool.getResource();
    }

    /**
     * 使用集合结构，添加元素
     *
     * @param key
     * @param value
     * @return
     */
    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    /**
     * 使用集合结构，删除元素
     *
     * @param key
     * @param value
     * @return
     */
    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    /**
     * @param key
     * @return
     */
    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    /**
     * 是否存在
     *
     * @param key
     * @param value
     * @return
     */
    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }


    /**
     * 向队列中放入数据
     *
     * @param key
     * @param value
     * @return
     */
    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    /**
     * 取出指定范围的值
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 从列表中取出值
     *
     * @param timeout
     * @param key
     * @return
     */
    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 向集合中加入
     *
     * @param key
     * @param score
     * @param value
     * @return
     */
    public long zadd(String key, double score, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key, score, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    /**
     * 把集合中元素移出
     *
     * @param key
     * @param value
     * @return
     */
    public long zrem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }



    /**
     * 开启redis的半事务
     *
     * @param jedis
     * @return
     */
    public Transaction multi(Jedis jedis) {
        try {
            return jedis.multi();
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
        }
        return null;
    }

    /**
     * 执行
     *
     * @param tx
     * @param jedis
     * @return
     */
    public List<Object> exec(Transaction tx, Jedis jedis) {
        try {
            return tx.exec();
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            // 出现异常需要回滚
            tx.discard();
        } finally {
            if (tx != null) {
                try {
                    tx.close();
                } catch (Exception ioe) {
                    // ..
                }
            }
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 取出指定范围的值
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 反向取出指定范围的值
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrevrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 集合中有多少个数据
     *
     * @param key
     * @return
     */
    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 设置验证码
     * @param catchKey
     * @param catche
     */
    public void setCacheObject(String catchKey, String catche) {
        Jedis jedis = null;
        String catcheKey = "catche:" + catchKey;
        try {
            jedis = pool.getResource();
            // 验证码存储在redis中，并且过期时间为3分钟
            jedis.setex(catcheKey, 60 * 3, catche);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 获取验证码
     * @param captchKey
     * @return
     */
    public String getCacheObject(String captchKey){
        Jedis jedis = null;
        String catch_key = "catche:" + captchKey;
        try {
            jedis = pool.getResource();
            String captcha = jedis.get(catch_key);
            return captcha;
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }


    /**
     * 清除验证码
     * @param captchKey
     */
    public void delete(String captchKey){
        Jedis jedis = null;
        String catch_key = "catche:" + captchKey;
        try {
            jedis = pool.getResource();
            jedis.del(catch_key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 存储Object
     * @param key
     * @param value
     * @param expireSecond
     */
    public void setObject(String key, Object value, int expireSecond) {
        if (null == value || null == key) {
            return;
        }
        Jedis jedis = pool.getResource();
        try {
            jedis.set(key.getBytes(), HessianUtil.serialize(value));
            if (expireSecond != -1) {
                jedis.expire(key.getBytes(), expireSecond);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            jedis.close();
        }
    }


    /**
     *
     * @param key
     * @param value
     * @param expireSecond
     */
    public void setToken(String key, TokenDO value, int expireSecond){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            // Token存储在redis中，并且过期时间为7天,存在的情况就覆盖
            jedis.setex(key, expireSecond, JSON.toJSONString(value));
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 获取Object
     * @param key
     * @return
     */
    public Object getObject(String key) {
        Object obj = null;
        if (StringUtils.isNotBlank(key)) {
            Jedis jedis = pool.getResource();
            try {
                byte[] value = jedis.get(key.getBytes());
                if (null != value) {
                    obj = HessianUtil.deserialize(value);
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            } finally {
                jedis.close();
            }
        }
        return obj;
    }

    /**
     *
     * @param key
     * @return
     */
    public TokenDO getToken(String key){
        TokenDO tokenDO = null;
        if (StringUtils.isNotBlank(key)) {
            Jedis jedis = pool.getResource();
            try {
               String tokenJson =  jedis.get(key);
               if(StringUtils.isEmpty(tokenJson)){
                   return tokenDO;
               }
               return JSON.parseObject(tokenJson,TokenDO.class);
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                jedis.close();
            }
        }
        return tokenDO;
    }


    /**
     * 删除key对应的值
     * @param key
     * @return
     */
    public void del(String key) {
        if(StringUtils.isEmpty(key)){
            return ;
        }
        Jedis jedis = pool.getResource();
        try {
             jedis.del(key.getBytes());
        } finally {
            jedis.close();
        }
    }

}
