package com.know.wenda.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * JsonBean
 *
 * @author hlb
 */
@Data
public class JsonBean implements Serializable {

    private static final long serialVersionUID = -7491662753620873719L;
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 结果
     */
    private String result;
}