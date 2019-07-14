package com.know.wenda.service;

import com.know.wenda.system.logger.KnwLoggerFactory;
import com.know.wenda.system.logger.KnwLoggerMarkers;
import com.know.wenda.system.loggerfactory.LoggerUtil;
import com.know.wenda.util.JedisAdapter;
import com.know.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * GratitudeService
 *
 * @author hlb
 */
@Component
public class GratitudeService {

    @Autowired
    private JedisAdapter jedisAdapter;

    /**
     * 获取用户对应感谢的话题id列表
     *
     * @param userId
     * @return
     */
    public List<Integer> getQuestionIds(int userId) {

        String gratitudeKey = RedisKeyUtil.getGratitude(userId);
        Jedis jedis = jedisAdapter.getJedis();
        try {
            Set<String> questionIds = jedis.smembers(gratitudeKey);
            if (CollectionUtils.isEmpty(questionIds)) {
                return new ArrayList<>();
            }
            List<Integer> questions = new ArrayList<>();
            questionIds.stream().forEach(questionId -> {
                questions.add(Integer.parseInt(questionId));
            });
            return questions;
        } catch (Exception e) {
            LoggerUtil.error(KnwLoggerFactory.EXCEPTION_HANDLER_LOGGER, KnwLoggerMarkers.EXCEPTION_HANDLER, "获取感谢话题列表异常", userId);
            return new ArrayList<>();
        } finally {
            jedis.close();
        }
    }

    /**
     * 添加感谢话题id
     *
     * @param userId
     * @param questionId
     */
    public Long addQuestionId(int userId, int questionId) {
        String gratitudeKey = RedisKeyUtil.getGratitude(userId);
        Jedis jedis = jedisAdapter.getJedis();
        try {
            Long result = jedis.sadd(gratitudeKey, String.valueOf(questionId));
            return result;
        } catch (Exception e) {
            LoggerUtil.error(KnwLoggerFactory.EXCEPTION_HANDLER_LOGGER, KnwLoggerMarkers.EXCEPTION_HANDLER, "添加感谢话题列表异常", userId);
            return null;
        } finally {
            jedis.close();
        }
    }

    /**
     * 移出感谢话题id
     *
     * @param userId
     * @param questionId
     */
    public Long delQuestionById(int userId, int questionId) {

        String gratitudeKey = RedisKeyUtil.getGratitude(userId);
        Jedis jedis = jedisAdapter.getJedis();
        try {
            Long result = jedis.srem(gratitudeKey, String.valueOf(questionId));
            return result;
        } catch (Exception e) {
            LoggerUtil.error(KnwLoggerFactory.EXCEPTION_HANDLER_LOGGER, KnwLoggerMarkers.EXCEPTION_HANDLER, "移出感谢话题列表异常", userId);
            return null;
        } finally {
            jedis.close();
        }
    }


}