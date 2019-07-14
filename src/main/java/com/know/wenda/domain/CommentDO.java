package com.know.wenda.domain;

import com.know.wenda.domain.base.Base;
import lombok.Data;

/**
 * CommentDO
 *
 * @author shunhua
 */
@Data
public class CommentDO extends Base {
    private static final long serialVersionUID = 2606866877974883646L;
    /**
     * 评论id
     */
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 对应记录类型
     */
    private Integer entityType;
    /**
     * 对应记录id
     */
    private Integer entityId;
    /**
     * 内容
     */
    private String content;


}