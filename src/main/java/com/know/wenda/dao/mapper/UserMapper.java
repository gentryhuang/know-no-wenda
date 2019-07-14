
package com.know.wenda.dao.mapper;

import com.know.wenda.domain.UserDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * UserMapper
 *
 * @author shunhua
 *
 */

@Mapper
public interface UserMapper {

    /**
     * 新增
     * @param userDO
     * @return
     */
    int insert(UserDO userDO);

    /**
     * 根据PK获取
     * @param id
     * @return
     */
    UserDO get(int id);


    /**
     * 根据姓名查找
     * @param name
     * @return
     */
    UserDO findByName(String name);


    /**
     * 根据账号查找
     * @param account
     * @return
     */
    UserDO findByAccount(String account);

    /**
     * 修改密码
     * @param userDO
     * @return
     */
    int updatePassword(UserDO userDO);

    /**
     * 重置密码
     * @param userDO
     * @return
     */
    int resetPassword(UserDO userDO);

    /**
     * 软删除
     * @param id
     */
    int delete(int id);

    /**
     * 修改用户信息
     * @param userDO
     * @return
     */
    int update(UserDO userDO);

    /**
     * 根据邮箱查找用户
     * @param email
     * @return
     */
    UserDO findByEmail(String email);
}
