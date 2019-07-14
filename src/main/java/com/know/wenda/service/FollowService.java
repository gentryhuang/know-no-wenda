package com.know.wenda.service;

import com.know.wenda.system.logger.KnwLoggerFactory;
import com.know.wenda.system.logger.KnwLoggerMarkers;
import com.know.wenda.system.loggerfactory.LoggerUtil;
import com.know.wenda.util.JedisAdapter;
import com.know.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * 主要使用Redis优先队列
 *
 * FollowService
 *
 * @author hlb
 */
@Service
public class FollowService {

    @Autowired
    private JedisAdapter jedisAdapter;

    /**
     * 用户关注了某个实体,可以关注问题,关注用户,关注评论等任何实体
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean follow(int userId, int entityType, int entityId) {
        try {
            // 获取某个实体的粉丝key
            String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
            // 获取关注对象的key
            String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
            Date date = new Date();
            Jedis jedis = jedisAdapter.getJedis();
            // 开启事务
            Transaction tx = jedisAdapter.multi(jedis);
            // 实体的粉丝增加当前用户
            tx.zadd(followerKey, date.getTime(), String.valueOf(userId));
            // 当前用户对这类实体关注+1
            tx.zadd(followeeKey, date.getTime(), String.valueOf(entityId));
            // 执行事务
            List<Object> ret = jedisAdapter.exec(tx, jedis);
            // 判断是否成功
            return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.BUSINESS_LOGGER,KnwLoggerMarkers.BUSINESS_INFO,"用户关注行为失败",userId,entityId,entityType);
            return false;
        }
    }

    /**
     * 取消关注
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean unfollow(int userId, int entityType, int entityId) {
        try {
            String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
            String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
            Date date = new Date();
            Jedis jedis = jedisAdapter.getJedis();
            // 开启事务
            Transaction tx = jedisAdapter.multi(jedis);
            // 实体的粉丝删除当前用户
            tx.zrem(followerKey, String.valueOf(userId));
            // 当前用户对这类实体关注-1
            tx.zrem(followeeKey, String.valueOf(entityId));
            // 执行当前事务
            List<Object> ret = jedisAdapter.exec(tx, jedis);
            // 判断是否成功
            return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.BUSINESS_LOGGER,KnwLoggerMarkers.BUSINESS_INFO,KnwLoggerFactory.formatLog("取消关注行为失败",userId,entityId,entityType),e);
            return  false;
        }

    }

    /**
     * 获取实体粉丝的id集合
     * @param entityType
     * @param entityId
     * @param count
     * @return
     */
    public List<Integer> getFollowers(int entityType, int entityId, int count) {
        try{
            String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
            return getIdsFromSet(jedisAdapter.zrevrange(followerKey, 0, count));
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.BUSINESS_LOGGER,KnwLoggerMarkers.BUSINESS_INFO,KnwLoggerFactory.formatLog("获取粉丝id集合失败",entityId,entityType),e);
            return new ArrayList<>();
        }

    }

    /**
     * 支持分页
     * @param entityType
     * @param entityId
     * @param offset
     * @param count
     * @return
     */
    public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
        try {
            String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
            return getIdsFromSet(jedisAdapter.zrevrange(followerKey, offset, offset+count));
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.BUSINESS_LOGGER,KnwLoggerMarkers.BUSINESS_INFO,KnwLoggerFactory.formatLog("获取粉丝id集合失败",entityId,entityType),e);
            return new ArrayList<>();
        }

    }

    /**
     *
     * @param userId
     * @param entityType
     * @param count
     * @return
     */
    public List<Integer> getFollowees(int userId, int entityType, int count) {
        try {
            String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
            return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, 0, count));
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.BUSINESS_LOGGER, KnwLoggerMarkers.BUSINESS_INFO,KnwLoggerFactory.formatLog("从Redis中获取关注者失败",userId,entityType),e);
            return new ArrayList<>();
        }
    }

    /**
     * 支持分页
     * @param userId
     * @param entityType
     * @param offset
     * @param count
     * @return
     */
    public List<Integer> getFollowees(int userId, int entityType, int offset, int count) {
        try{
            String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
            // 返回有序集key中指定区间内的成员，它和zrange命令一样，除了是按照score值递减的次序排列
            return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, offset, offset+count));
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.BUSINESS_LOGGER,KnwLoggerMarkers.BUSINESS_INFO,KnwLoggerFactory.formatLog("从Redis中获取关注者失败",userId,entityType),e);
            return new ArrayList<>();
        }

    }

    /**
     * 获取实体粉丝量
     * @param entityType
     * @param entityId
     * @return
     */
    public long getFollowerCount(int entityType, int entityId) {
        try{
            String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
            return jedisAdapter.zcard(followerKey);
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.BUSINESS_LOGGER,KnwLoggerMarkers.BUSINESS_INFO,KnwLoggerFactory.formatLog("从Redis中获取实体粉丝量失败",entityId,entityType),e);
            return 0;
        }

    }

    /**
     * 获取关注量
     * @param userId
     * @param entityType
     * @return
     */
    public long getFolloweeCount(int userId, int entityType) {
        try{
            String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
            return jedisAdapter.zcard(followeeKey);
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.BUSINESS_LOGGER,KnwLoggerMarkers.BUSINESS_INFO,KnwLoggerFactory.formatLog("从redis中获取关注量失败",userId,entityType));
            return  0;
        }

    }

    /**
     *  判断用户是否关注了某个实体
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean isFollower(int userId, int entityType, int entityId) {
        try{
            String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
            // 只要分数有就
            return jedisAdapter.zscore(followerKey, String.valueOf(userId)) != null;
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.BUSINESS_LOGGER,KnwLoggerMarkers.BUSINESS_INFO,KnwLoggerFactory.formatLog("判断用户是否关注某个实体失败",userId,entityType,entityId));
            return false;
        }

    }


    /**
     * 封装id
     * @param idSet
     * @return
     */
    private List<Integer> getIdsFromSet(Set<String> idSet) {
        List<Integer> ids = new ArrayList<>();
        for (String str : idSet) {
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }
}
