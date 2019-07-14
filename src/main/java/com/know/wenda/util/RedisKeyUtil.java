package com.know.wenda.util;


/**
 * RedisKeyUtil
 *
 * @author hlb
 */
public class RedisKeyUtil {
    /**
     * 分隔符
     */
    private static String SPLIT = ":";
    /**
     * 喜欢
     */
    private static String BIZ_LIKE = "LIKE";
    /**
     * 不喜欢
     */
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";
    /**
     * 粉丝
     */

    private static String BIZ_FOLLOWER = "FOLLOWER";
    /**
     * 关注对象
     */
    private static String BIZ_FOLLOWEE = "FOLLOWEE";
    /**
     * 时间轴
     */
    private static String BIZ_TIMELINE = "TIMELINE";

    /**
     * 感谢
     */
    private static String BIZ_GRATITUDE = "GRATITUDE";

    /**
     * 获取喜欢的key
     *
     * @param entityType 某个对象
     * @param entityId   对应id
     * @return
     */
    public static String getLikeKey(int entityType, int entityId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }

    /**
     * 某个实体的粉丝key 如某条评论的粉丝
     */
    public static String getFollowerKey(int entityType, int entityId) {
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    /**
     * 用户对某类实体的关注key 如关注评论
     *
     * @param userId
     * @param entityType
     * @return
     */
    public static String getFolloweeKey(int userId, int entityType) {
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }

    /**
     * 获取用户时间轴的key
     * @param userId
     * @return
     */
    public static String getTimelineKey(int userId) {
        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }


    /**
     * 获取用户感谢的key
     * @param userId
     * @return
     */
    public static String getGratitude(int userId){
        return BIZ_GRATITUDE + SPLIT + String.valueOf(userId);
    }
}
