package com.know.wenda;

import com.know.wenda.base.KnownoWendaApplicationTests;
import com.know.wenda.util.JedisAdapter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * RedisTest
 *
 * @author hlb
 */
public class RedisTest extends KnownoWendaApplicationTests {

    @Autowired
    private JedisAdapter jedisAdapter;

    @Test
    public void  add(){
        Jedis jedis = jedisAdapter.getJedis();
        jedis.lpush("key_list","111");
        jedis.lpush("key_list","22");

        // brpop 第一个返回的是key
        List<String> key_list = jedis.brpop(0, "key_list");
        for (String str : key_list){
            System.out.println(str);
        }

    }

}