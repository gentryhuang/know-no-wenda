package com.know.wenda.domain;

import com.know.wenda.domain.base.Base;
import lombok.Data;

/**
 * UserDO
 *
 * @author shunhua
 */
@Data
public class UserDO extends Base {
    private static final long serialVersionUID = -4378373330912228727L;
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 密码
     */
    private String password;
    /**
     * 加密盐
     */
    private String salt;
    /**
     * 头像url
     */
    private String headUrl;
}