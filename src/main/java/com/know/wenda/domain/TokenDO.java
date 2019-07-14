package com.know.wenda.domain;

import com.know.wenda.domain.base.Base;
import lombok.Data;

import java.util.Date;

/**
 * TokenDO
 *
 * @author shunhua
 */
@Data
public class TokenDO extends Base {
    private static final long serialVersionUID = 6808832090304373298L;
    /**
     * token id
     */
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 过期时间
     */
    private Date expired;
    /**
     * token
     */
    private String token;

}