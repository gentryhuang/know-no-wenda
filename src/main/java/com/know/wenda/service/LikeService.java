package com.know.wenda.service;

import com.know.wenda.system.logger.KnwLoggerFactory;
import com.know.wenda.system.logger.KnwLoggerMarkers;
import com.know.wenda.system.loggerfactory.LoggerUtil;
import com.know.wenda.util.JedisAdapter;
import com.know.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * LikeService
 *
 * @author hlb
 */
@Service
public class LikeService {

    @Autowired
    JedisAdapter jedisAdapter;


    /**
     * 点赞
     * @param userId 用户id
     * @param entityType 对应的类型 如：评论
     * @param entityId 对应id  如： 评论id
     * @return
     */
    public long like(int userId, int entityType, int entityId) {
        try{
            // 加入到喜欢的集合中
            String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
            jedisAdapter.sadd(likeKey, String.valueOf(userId));
            // 需要把不喜欢的集合中的数据删除
            String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
            jedisAdapter.srem(disLikeKey, String.valueOf(userId));
            // 返回人数
            return jedisAdapter.scard(likeKey);
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.BUSINESS_LOGGER, KnwLoggerMarkers.BUSINESS_INFO,KnwLoggerFactory.formatLog("点赞失败",userId,entityType,entityId),e);
            return 0;
        }

    }

    /**
     * 踩
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public long disLike(int userId, int entityType, int entityId) {
        try{
            // 加入到不喜欢集合中
            String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
            jedisAdapter.sadd(disLikeKey, String.valueOf(userId));
            // 需要把对应的喜欢集合中数据删除
            String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
            jedisAdapter.srem(likeKey, String.valueOf(userId));
            return jedisAdapter.scard(likeKey);
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.BUSINESS_LOGGER, KnwLoggerMarkers.BUSINESS_INFO,KnwLoggerFactory.formatLog("踩失败",userId,entityType,entityId),e);
            return 0;
        }

    }

    /**
     * 某个用户点赞/踩 状态信息
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public int getLikeStatus(int userId, int entityType, int entityId) {
        try{
            // 获取喜欢的key
            String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
            // 判断该用户是否喜欢，喜欢就返回1
            if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
                return 1;
            }
            // 获取不喜欢的key
            String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
            // 判断该用户是否不喜欢，不喜欢返回 -1 ，喜欢返回0
            return jedisAdapter.sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.BUSINESS_LOGGER,KnwLoggerMarkers.BUSINESS_INFO,KnwLoggerFactory.formatLog("用户点赞/踩状态失败",userId,entityId,entityType),e);
            // TODO 状态获取失败
            return Integer.MIN_VALUE;
        }

    }

    /**
     * 获取喜欢的人数
     * @param entityType
     * @param entityId
     * @return
     */
    public long getLikeCount(int entityType, int entityId) {
        try{
            String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
            return jedisAdapter.scard(likeKey);
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.BUSINESS_LOGGER,KnwLoggerMarkers.BUSINESS_INFO,KnwLoggerFactory.formatLog("获取喜欢的人数失败",entityId,entityType),e);
            return 0;
        }

    }


}
