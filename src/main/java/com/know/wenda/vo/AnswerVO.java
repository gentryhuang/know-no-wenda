package com.know.wenda.vo;

import com.know.wenda.domain.QuestionDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * AnswerVO
 *
 * @author hlb
 */
@Data
public class AnswerVO implements Serializable {

    private static final long serialVersionUID = -5378741676934764203L;

    /**
     * 评论涉及到的问题
     */
    private QuestionDO questionDO;

    /**
     * 问题对应的评论
     */
    private List<VoMap> comments;
    /**
     * 对应的关注用户信息
     */
    private List<VoMap> followUsers;

    /**
     * 是否是关注状态
     */
    private boolean followed;

}