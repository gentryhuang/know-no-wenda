
package com.know.wenda.dao.mapper;

import com.know.wenda.domain.MessageDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * MessageMapper
 *
 * @author shunhua
 *
 */
@Mapper
public interface MessageMapper {

    /**
     * 新增
     * @param messageDO
     * @return
     */
    int insert(MessageDO messageDO);

    /**
     *获取会话详情列表
     * @param map
     * @return
     */
    List<MessageDO> getConversationDetail(Map<String,Object> map);

    /**
     * 获取会话列表
     *
     * @param map
     * @return
     */
    List<MessageDO> getConversationList(Map<String,Integer> map);

    /**
     *获取会话未读数
     * @param map
     * @return
     */
    int getConversationUnreadCount(Map<String,Object> map);

    /**
     * 修改会话状态信息
     * @param userId
     * @param conversationId
     * @return
     */
    int updateReadStatusByToId(@Param("userId") int userId, @Param("conversationId") String conversationId);

    /**
     *  删除当前用户会话列表
     * @param userId
     * @param conversationId
     * @return
     */
    int deleteConversation(@Param("userId") int userId, @Param("conversationId") String conversationId);

    /**
     * 删除当前会话中某条详情信息
     * @param id
     * @return
     */
    int deleteDetailConversationById(int id);

}
