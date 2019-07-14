package com.know.wenda.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * ExceptionVO
 *
 * @author hlb
 */
@Data
public class ExceptionVO implements Serializable {

    private static final long serialVersionUID = -7138702671931100183L;

    /**
     * 异常状态码
     */
    private Integer code;
    /**
     * 异常信息
     */
    private String msg;
}