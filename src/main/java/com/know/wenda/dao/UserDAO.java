package com.know.wenda.dao;


import com.know.wenda.dao.mapper.UserMapper;
import com.know.wenda.domain.UserDO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * UserDAO
 *
 * @author shunhua
 */
@Repository
public class UserDAO {

    @Resource
    private UserMapper userMapper;

    /**
     * 新增
     *
     * @param userDO
     * @return
     */
    public int insert(UserDO userDO) {
        int result = userMapper.insert(userDO);
        return result;

    }

    /**
     * 根据PK获取
     *
     * @param id
     * @return
     */
    public UserDO get(int id) {
        UserDO userDO = userMapper.get(id);
        return userDO;

    }

    /**
     * 根据账号查找
     *
     * @param account
     * @return
     */
    public UserDO findByAccount(String account) {
        UserDO userDO = userMapper.findByAccount(account);
        return userDO;
    }

    /**
     * 根据名称查找
     * @param name
     * @return
     */
    public UserDO findByName(String name){
        UserDO userDO = userMapper.findByName(name);
        return userDO;
    }



    /**
     * 修改密码
     *
     * @param userDO
     */
    public void updatePassword(UserDO userDO) {
        userMapper.updatePassword(userDO);

    }

    /**
     * 重置密码
     * @param userDO
     * @return
     */
    public int resetPassword(UserDO userDO){
        return userMapper.resetPassword(userDO);
    }

    /**
     * 软删除
     *
     * @param id
     */
    public void delete(int id) {
        userMapper.delete(id);
    }

    /**
     * 修改用户信息
     * @param userDO
     * @return
     */
    public int updateUser(UserDO userDO) {
        return userMapper.update(userDO);
    }

    /**
     * 根据邮箱查找用户
     * @param email
     * @return
     */
    public UserDO findByEmail(String email) {
        return userMapper.findByEmail(email);
    }
}
