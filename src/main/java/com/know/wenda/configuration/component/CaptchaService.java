package com.know.wenda.configuration.component;

import com.know.wenda.util.CaptchaUtil;
import com.know.wenda.util.JedisAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;


/**
 * CaptchaService
 *
 * @author hlb
 */
@Component
public class CaptchaService {

    private static final Logger logger = LoggerFactory.getLogger(CaptchaService.class);
    public static final int CAPTCHA_WIDTH = 120;
    public static final int CAPTCHA_HEIGHT = 30;

    @Autowired
    private JedisAdapter jedisAdapter;

    public String createCaptcha(){
        // 用来生成验证码（4位）
        String captcha = CaptchaUtil.generateCaptchaCode(4);
        //使用uuid作为captchaKey
        String captchaKey = UUID.randomUUID().toString().replace("-", "");
        //存储到redis并设置过期时间为3分钟
        jedisAdapter.setCacheObject(captchaKey, captcha);
        return captchaKey;
    }

    public String getCaptcha(String captchaKey){
        String captcha = jedisAdapter.getCacheObject(captchaKey);
        return captcha;
    }

    public void sendCaptchaJPG(String captcha, OutputStream os){
        try {
            // 图片化渲染验证码
            CaptchaUtil.outputImage(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, os, captcha);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

}