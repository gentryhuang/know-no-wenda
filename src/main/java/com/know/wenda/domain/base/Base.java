package com.know.wenda.domain.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Base
 *
 * @author shunhua
 */
@Data
public abstract class Base implements Serializable {

    private static final long serialVersionUID = 4219218340575977972L;

    /**
     * 是否有效  0 无效  1 有效
     */
    private int isValid = 1;
    /**
     * 版本号
     */
    private int lastVer = 1;
    /**
     * 创建时间
     */
    private Date createDate = new Date();
    /**
     * 操作时间
     */
    private Date updateDate = new Date();

}