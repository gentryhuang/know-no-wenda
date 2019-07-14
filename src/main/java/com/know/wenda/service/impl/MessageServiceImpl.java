package com.know.wenda.service.impl;

import com.know.wenda.dao.MessageDAO;
import com.know.wenda.domain.MessageDO;
import com.know.wenda.service.IMessageService;
import com.know.wenda.configuration.component.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * MessageServiceImpl
 *
 * @author hlb
 */
@Service
public class MessageServiceImpl implements IMessageService {

    @Autowired
    MessageDAO messageDAO;

    @Autowired
    SensitiveFilter sensitiveFilter;

    @Override
    public int addMessage(MessageDO messageDO) {
        messageDO.setContent(sensitiveFilter.filter(messageDO.getContent()));
        return messageDAO.insert(messageDO) > 0 ? messageDO.getId() : 0;
    }

    @Override
    public List<MessageDO> getConversationDetail(String conversationId, int offset, int limit) {
        Map<String,Object> map = new HashMap<>(3);
        map.put("conversationId",conversationId);
        map.put("offset",offset);
        map.put("limit",limit);
        return  messageDAO.getConversationDetail(map);
    }

    @Override
    public List<MessageDO> getConversationList(int userId, int offset, int limit) {
        Map<String,Integer> map = new HashMap<>(3);
        map.put("userId",userId);
        map.put("offset",offset);
        map.put("limit",limit);
        return  messageDAO.getConversationList(map);
    }

    @Override
    public int getConversationUnreadCount(int userId, String conversationId) {
        Map<String,Object> map = new HashMap<>(2);
        map.put("conversationId",conversationId);
        map.put("userId",userId);
        return messageDAO.getConversationUnreadCount(map);
    }

    @Override
    public int modifyReadStatusByToId(int userId,String conversationId) {
        return messageDAO.updateReadStatusByToId(userId,conversationId);
    }

    @Override
    public int deleteConversation(int userId,String conversationId) {
        return messageDAO.deleteConversation(userId,conversationId);
    }

    @Override
    public int deleteDetailConversationById(int id) {
        return messageDAO.deleteDetailById(id);
    }
}
