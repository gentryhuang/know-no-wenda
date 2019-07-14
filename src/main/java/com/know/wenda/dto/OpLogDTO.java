
package com.know.wenda.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * OpLogDTO
 *
 * @author hlb
 */
@Data
public class OpLogDTO implements Serializable {

    /**
     * 操作者
     */
    private String operator;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 参数
     */
    private String param;

    /**
     * 请求IP
     */
    private String ip;
}