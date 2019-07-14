package com.know.wenda.async.handler;

import com.know.wenda.async.EventHandler;
import com.know.wenda.async.EventModel;
import com.know.wenda.constant.EventType;
import com.know.wenda.constant.StringConstant;
import com.know.wenda.domain.EntityTypeDO;
import com.know.wenda.domain.MessageDO;
import com.know.wenda.domain.UserDO;
import com.know.wenda.service.IMessageService;
import com.know.wenda.service.IUserService;
import com.know.wenda.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


/**
 * FollowHandler
 *
 * @author hlb
 */
@Component
public class FollowHandler implements EventHandler {
    @Autowired
    private IMessageService messageService;
    @Autowired
    private IUserService userService;
    @Value("${host.ip}")
    private String ip;
    @Value("${host.port}")
    private int port;

    @Override
    public void doHandle(EventModel model) {
        MessageDO messageDO = new MessageDO();
        messageDO.setFromId(JsonUtil.SYSTEM_USERID);
        messageDO.setToId(model.getEntityOwnerId());
        messageDO.setHasRead(0);
        UserDO user = userService.getUser(model.getActorId());

        // 关注的是问题
        if (model.getEntityType() == EntityTypeDO.ENTITY_QUESTION) {
            messageDO.setContent(String.format(StringConstant.MessageString.QUESTION_URL,ip,port,model.getEntityId(),user.getName()));
            // 关注的是人
        } else if (model.getEntityType() == EntityTypeDO.ENTITY_USER) {
            messageDO.setContent(String.format(StringConstant.MessageString.USER_URL,ip,port,model.getActorId(),user.getName()));
        }
        // 增加消息
        messageService.addMessage(messageDO);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
