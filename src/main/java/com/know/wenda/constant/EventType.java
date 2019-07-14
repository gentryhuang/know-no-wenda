package com.know.wenda.constant;


/**
 * EventType
 *
 * @author hlb
 */

public enum EventType  {
    /**
     * 点赞事件
     */
    LIKE(0),
    /**
     * 评论事件
     */
    COMMENT(1),
    /**
     * 登陆事件
     */
    LOGIN(2),
    /**
     * 邮件事件
     */
    MAIL(3),
    /**
     * 关注事件
     */
    FOLLOW(4),
    /**
     * 取消关注事件
     */
    UNFOLLOW(5),
    /**
     * 新增话题事件
     */
    ADD_QUESTION(6),
    /**
     * 感谢事件
     */
    GRATITUDE(7);
    /**
     * 事件对应标识值
     */
    private int value;
    EventType(int value) { this.value = value; }
    public int getValue() { return value; }
}
