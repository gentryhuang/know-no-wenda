package com.know.wenda.service;

import com.know.wenda.domain.MessageDO;

import java.util.List;

/**
 * IMessageService
 *
 * @author shunhua
 */
public interface IMessageService {
    /**
     * 新增
     *
     * @param messageDO
     * @return
     */
    int addMessage(MessageDO messageDO);

    /**
     * 多个条件获取
     *
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    List<MessageDO> getConversationDetail(String conversationId, int offset, int limit);

    /**
     * 多条件获取
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<MessageDO> getConversationList(int userId, int offset, int limit);

    /**
     *
     * @param userId
     * @param conversationId
     * @return
     */
    int getConversationUnreadCount(int userId, String conversationId);

    /**
     * 修改读取状态
     * @param userId
     * @return
     */
    int modifyReadStatusByToId(int userId,String conversationId);

    /**
     * 删除用户会话列表
     * @param userId
     * @param conversationId
     * @return
     */
    int deleteConversation(int userId,String conversationId);

    /**
     * 删除会话中某条详情信息
     * @param id
     * @return
     */
    int deleteDetailConversationById(int id);
}
