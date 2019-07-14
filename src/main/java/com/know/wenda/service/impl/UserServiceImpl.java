package com.know.wenda.service.impl;

import com.alibaba.fastjson.JSON;
import com.know.wenda.configuration.component.AsyncService;
import com.know.wenda.constant.StringConstant;
import com.know.wenda.constant.TimeConstant;
import com.know.wenda.dao.TokenDAO;
import com.know.wenda.dao.UserDAO;
import com.know.wenda.domain.TokenDO;
import com.know.wenda.domain.UserDO;
import com.know.wenda.service.IUserService;
import com.know.wenda.constant.AliOssConstant;
import com.know.wenda.util.JedisAdapter;
import com.know.wenda.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * UserServiceImpl
 *
 * @author hlb
 */
@Service
public class UserServiceImpl implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private TokenDAO tokenDAO;

    @Autowired
    private JedisAdapter jedisAdapter;

    @Autowired
    private AsyncService asyncService;

    @Override
    public UserDO selectByName(String name) {
        return userDAO.findByName(name);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> register(String username, String password,String email,String captcha,String captchaKey) {
        Map<String, Object> map = new HashMap<>(16);

        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if(StringUtils.isBlank(email)){
            map.put("msg","邮箱不能为空");
            return map;
        }

        if(!matche(email)){
            map.put("msg","邮箱格式错误");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        if(!captcha.equalsIgnoreCase(jedisAdapter.getCacheObject(captchaKey))){
            map.put("msg","验证码错误");
            return map;
        }
        // 验证码用过后不能再次使用
        jedisAdapter.delete(captchaKey);
        UserDO user = userDAO.findByName(username);
        if (!ObjectUtils.isEmpty(user)) {
            map.put("msg", "用户名被占用");
            return map;
        }
        // 判断邮箱是否别占用
        UserDO userEmail = userDAO.findByEmail(email);
        if(!ObjectUtils.isEmpty(userEmail)){
            map.put("msg", "邮箱被占用");
            return map;
        }
        // 用户信息
        user = new UserDO();
        user.setName(username);
        user.setEmail(email);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        String head = String.format(AliOssConstant.IMAGE_URL, new Random().nextInt(17)+1);
        user.setHeadUrl(head);
        user.setPassword(MD5Util.getMD5String(password + user.getSalt()));
        userDAO.insert(user);
        // 注册成功发送邮件
        String to = email;
        String subject = "注册成功通知";
        String template = "mails/register_success.html";
        Map<String, Object> model = new HashMap<>(16);
        model.put("username",username);
        model.put("email",email);
        asyncService.sendMail(to,subject,template,model,username);
        // 注册成功后自动登录
        String token = addLoginToken(user.getId());
        map.put("token", token);
        return map;
    }


    @Override
    public Map<String, Object> login(String account, String password,String captcha,String captchaKey) {
        Map<String, Object> map = new HashMap<>(16);
        if(!captcha.equalsIgnoreCase(jedisAdapter.getCacheObject(captchaKey))){
            map.put("msg","验证码错误");
            return map;
        }
        // 验证码用过后不能再次使用 (无论成功还是失败，最后都会重新刷新页面获取新的验证码，后退得到的验证码不能使用)
       jedisAdapter.delete(captchaKey);
        if (StringUtils.isBlank(account)) {
            map.put("msg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        UserDO user = userDAO.findByAccount(account);
        if (ObjectUtils.isEmpty(user)) {
            map.put("msg", "账号不存在");
            return map;
        }

        if (!MD5Util.getMD5String(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码不正确");
            return map;
        }
        // 记录登录信息
        String token = addLoginToken(user.getId());
        map.put("token", token);
        map.put("userId", user.getId());
        return map;
    }


    @Override
    public UserDO getUser(int id) {
        return userDAO.get(id);
    }

    /**
     * 直接让token失效
     * @param token
     */
    @Override
    public void logout(String token) {
        // 清除对应的缓存
        String token_key = String.format(StringConstant.Token.TOKEN,token);
        jedisAdapter.del(token_key);
        tokenDAO.delete(token);
    }

    /**
     * 重置密码
     * @param userDO
     * @return
     */
    @Override
    public int resetPassword(UserDO userDO) {
        return userDAO.resetPassword(userDO);
    }



    @Override
    public int updateUser(UserDO userDO) {
        return userDAO.updateUser(userDO);
    }

    /**
     * 登录的时候进行记录
     * @param userId
     * @return
     */
    private String addLoginToken(int userId) {
        TokenDO token = new TokenDO();
        token.setUserId(userId);
        Date date = new Date();
        // 注意setTime的参数是毫秒  1s = 1000ms
        date.setTime(date.getTime() + 7 * 3600 * 24 * 1000);
        // 设置过期时间,七天
        token.setExpired(date);
        String key_token = UUID.randomUUID().toString().replaceAll("-", "");
        token.setToken(key_token);
        tokenDAO.insert(token);
        // 把token缓存起来 7天，减轻数据库压力
        jedisAdapter.setToken(String.format(StringConstant.Token.TOKEN,key_token), token, TimeConstant.CacheExpireTime.EXPIRE_SECOND_HOME_TIME);
        return token.getToken();
    }

    /**
     *  mailRegex是整体邮箱正则表达式，mailName是@前面的名称部分，mailDomain是后面的域名部分
     * @param email
     * @return
     */
    private boolean matche(String email){
        String mailName="^[0-9a-z]+([._\\\\-]*[a-z0-9])*";
        String mailDomain="([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
        //邮箱正则表达式      ^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$
        String mailRegex=mailName+"@"+mailDomain;
        Pattern pattern= Pattern.compile(mailRegex);
        Matcher matcher=pattern.matcher(email);
        return matcher.matches();
    }

}
