package com.know.wenda.domain;

import com.know.wenda.domain.base.Base;
import lombok.Data;

/**
 * QuestionDO
 *
 * @author shunhua
 */
@Data
public class QuestionDO extends Base {

    private static final long serialVersionUID = 7856472945608526217L;
    /**
     * 问题id
     */
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 评论数目
     */
    private Integer commentCount;

    /**
     * 标识感谢的人
     */
    private Boolean gratitudeFlag = Boolean.FALSE;

}