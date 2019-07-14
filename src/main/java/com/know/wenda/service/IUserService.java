package com.know.wenda.service;

import com.know.wenda.domain.UserDO;
import java.util.Map;

/**
 * IUserService
 *
 * @author shunhua
 */
public interface IUserService {


    /**
     * 根据用户id查询
     *
     * @param id
     * @return
     */
    UserDO getUser(int id);


    /**
     * 根据用户名称查询
     *
     * @param name
     * @return
     */
    UserDO selectByName(String name);


    /**
     * 注册
     *
     * @param username
     * @param password
     * @return
     */
    Map<String, Object> register(String username, String password,String email,String captcha,String captchaKey);


    /**
     * 登录
     *
     * @param account
     * @param password
     * @return
     */
    Map<String, Object> login(String account, String password,String captcha,String captchaKey);


    /**
     * 登出
     *
     * @param token
     */
    void logout(String token);

    /**
     * 重置密码
     * @param userDO
     * @return
     */
    int resetPassword(UserDO userDO);

    /**
     * 修改用户信息
     * @param userDO
     * @return
     */
    int updateUser(UserDO userDO);


}
