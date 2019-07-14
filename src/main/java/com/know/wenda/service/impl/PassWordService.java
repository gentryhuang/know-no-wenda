package com.know.wenda.service.impl;

import com.know.wenda.configuration.component.MailSenderUtil;
import com.know.wenda.dao.UserDAO;
import com.know.wenda.domain.UserDO;
import com.know.wenda.service.IPassWordService;
import com.know.wenda.service.IUserService;
import com.know.wenda.util.JedisAdapter;
import com.know.wenda.util.MD5Util;
import com.know.wenda.util.RandPasswordUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PassWordService
 *
 * @author hlb
 */
@Service
public class PassWordService implements IPassWordService {
    private static final Logger logger = LoggerFactory.getLogger(PassWordService.class);

    @Autowired
    private JedisAdapter jedisAdapter;
    @Autowired
    private IUserService userService;
    @Autowired
    private MailSenderUtil mailSenderUtil;
    @Autowired
    private UserDAO userDAO;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public  Map<String, String> resetPassword(String email, String captcha, String captchaKey) {
        Map<String, String> map = new HashMap<>(16);
        if(StringUtils.isBlank(email)){
            map.put("msg","邮箱不能为空");
            return map;
        }

        if(!matche(email)){
            map.put("msg","邮箱格式错误");
            return map;
        }

        if(!captcha.equalsIgnoreCase(jedisAdapter.getCacheObject(captchaKey))){
            map.put("msg","验证码错误");
            return map;
        }
        // 验证码用过后不能再次使用
        jedisAdapter.delete(captchaKey);
        // 生成随机密码
        String password = RandPasswordUtil.getPassword();
        // 重置密码
        UserDO userDO = userDAO.findByAccount(email);
        if(ObjectUtils.isEmpty(userDO)){
            map.put("msg","该邮箱没有绑定的用户，请联系管理员！");
            return map;
        }
        userDO.setEmail(email);
        String md5Password = MD5Util.getMD5String(password + userDO.getSalt());
        userDO.setPassword(md5Password);
        userService.resetPassword(userDO);

        // 发送邮件
        Map<String, Object> model = new HashMap<String, Object>(16);
        model.put("email", email);
        model.put("password",password);
        boolean flag = mailSenderUtil.sendWithHTMLTemplate(email,"重置密码","mails/reset_password.html",model);
        if(!flag){
            // 发送邮件成功后修改数据库,失败就回滚
            try {
                throw new Exception("邮件发送失败");
            }catch (Exception e){
                logger.error("邮件发送失败");
            }finally {
                map.put("msg","服务器异常，请重新操作！");
                return map;
            }
        }
        map.put("msg","OK");
        return map;

    }
    /**
     *  mailRegex是整体邮箱正则表达式，mailName是@前面的名称部分，mailDomain是后面的域名部分
     * @param email
     * @return
     */
    private boolean matche(String email){
        String mailDomain="([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
        String mailName="^[0-9a-z]+([._\\\\-]*[a-z0-9])*";
        String mailRegex=mailName+"@"+mailDomain;
        Pattern pattern= Pattern.compile(mailRegex);
        Matcher matcher=pattern.matcher(email);
        return matcher.matches();
    }
}