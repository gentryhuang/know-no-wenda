package com.know.wenda.async.handler;

import com.know.wenda.async.EventHandler;
import com.know.wenda.async.EventModel;
import com.know.wenda.constant.EventType;
import com.know.wenda.constant.StringConstant;
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
 * LikeHandler
 * <p>
 * 点赞
 *
 * @author hlb
 */
@Component
public class LikeHandler implements EventHandler {
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
        // 谁发的消息
        messageDO.setFromId(JsonUtil.SYSTEM_USERID);
        // 接收者
        messageDO.setToId(model.getEntityOwnerId());
        messageDO.setHasRead(0);
        // 事件触发者id
        UserDO user = userService.getUser(model.getActorId());
        messageDO.setContent(String.format(StringConstant.MessageString.LIKE_URL, ip, port, model.getExt("questionId"), user.getName()));
        // 处理消息
        messageService.addMessage(messageDO);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
