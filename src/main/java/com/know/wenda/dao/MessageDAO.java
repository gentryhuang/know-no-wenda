package com.know.wenda.dao;

import com.know.wenda.dao.mapper.MessageMapper;
import com.know.wenda.domain.MessageDO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * MessageDAO
 *
 * @author shunhua
 */
@Repository
public class MessageDAO {

    @Resource
    private MessageMapper messageMapper;

    /**
     * 新增
     *
     * @param messageDO
     * @return
     */
    public int insert(MessageDO messageDO) {
        int result = messageMapper.insert(messageDO);
        return result;

    }

    /**
     * 获取会话详情列表
     * @param map
     * @return
     */
    public List<MessageDO> getConversationDetail(Map<String, Object> map) {
        List<MessageDO> conversationDetail = messageMapper.getConversationDetail(map);
        return conversationDetail;

    }

    /**
     * 获取会话列表
     * @param map
     * @return
     */
    public List<MessageDO> getConversationList(Map<String, Integer> map) {
        List<MessageDO> conversationList = messageMapper.getConversationList(map);
        return conversationList;
    }


    /**
     * 获取未读数量
     * @param map
     * @return
     */
    public int getConversationUnreadCount(Map<String, Object> map) {
        int count = messageMapper.getConversationUnreadCount(map);
        return count;
    }

    /**
     * 修改会话读取状态
     * @param userId
     * @return
     */
    public int updateReadStatusByToId(int userId,String conversationId){
       return messageMapper.updateReadStatusByToId(userId,conversationId);
    }

    /**
     * 删除用户会话列表
     * @param userId
     * @return
     */
    public int deleteConversation(int userId,String conversationId){
        return messageMapper.deleteConversation(userId,conversationId);
    }

    /**
     * 删除某条详情信息
     * @param id
     * @return
     */
    public int deleteDetailById(int id){
        return messageMapper.deleteDetailConversationById(id);
    }

}
