package com.know.wenda.configuration.component;

import com.know.wenda.system.logger.KnwLoggerFactory;
import com.know.wenda.system.logger.KnwLoggerMarkers;
import com.know.wenda.system.loggerfactory.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * AsyncService
 *
 * @author hlb
 */
@Component
public class AsyncService {
    private static final Logger logger = LoggerFactory.getLogger(AsyncService.class);

    @Resource
    private MailSenderUtil mailSenderUtil;

    /**
     * 新用户注册成功异步发送邮件
     * @param to
     * @param subject
     * @param template
     * @param model
     */
    @Async
    public void sendMail(String to, String subject, String template, Map<String, Object> model,String username){
        try {
            boolean flag = mailSenderUtil.sendWithHTMLTemplate(to, subject, template, model);
            if (flag) {
                logger.info("发送给新用户{}注册成功通知成功!", username);
            } else {
                logger.error("邮件发送失败");
            }
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.BUSINESS_LOGGER, KnwLoggerMarkers.BUSINESS_INFO,"邮件发送异常",to,subject,username);
        }
    }

}