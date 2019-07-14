package com.know.wenda.configuration.component;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.internet.MimeMessage;
import java.util.Map;



/**
 * MailSender
 *
 * @author hlb
 */
@Component
public class MailSenderUtil{

    private static final Logger logger = LoggerFactory.getLogger(MailSenderUtil.class);

    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private VelocityEngine velocityEngine;

    /**
     * 出错了也不能影响正常功能
     * @param to
     * @param subject
     * @param template
     * @param model
     * @return
     */
    public boolean sendWithHTMLTemplate(String to, String subject, String template, Map<String, Object> model) {
        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setSubject(subject);
            // 使用Velocity模版定制个性化
            String result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);
            mimeMessageHelper.setText(result,true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom("1379375209@qq.com");
            mailSender.send(mimeMessage);
            logger.info("邮件发送成功了");
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }

}
