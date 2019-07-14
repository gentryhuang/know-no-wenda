package com.know.wenda.async.handler;

import com.know.wenda.async.EventHandler;
import com.know.wenda.async.EventModel;
import com.know.wenda.constant.EventType;
import com.know.wenda.configuration.component.MailSenderUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * LoginExceptionHandler
 *
 * @author hlb
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Resource
    private MailSenderUtil mailSenderUtil;

    @Override
    public void doHandle(EventModel model) {
        //  判断发现这个用户登陆异常，发送邮件
        Map<String, Object> map = new HashMap<String, Object>(5);
        map.put("username", model.getExt("username"));
        mailSenderUtil.sendWithHTMLTemplate(model.getExt("email"), "登陆IP异常", "mails/login_exception.html", map);
    }

    /**
     * 只关心登录事件
     * @return
     */
    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
