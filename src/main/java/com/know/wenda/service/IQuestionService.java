package com.know.wenda.service;

import com.know.wenda.domain.QuestionDO;


import java.util.List;

/**
 * IQuestionService
 *
 * @author shunhua
 */
public interface IQuestionService {

    /**
     * 根据Pk 查询
     *
     * @param id
     * @return
     */
    QuestionDO getById(int id);

    /**
     * 新增
     *
     * @param questionDO
     * @return
     */
    int addQuestion(QuestionDO questionDO);

    /**
     * 多条件查询列表
     *
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<QuestionDO> getLatestQuestions(int userId, int offset, int limit);

    /**
     * 更新条目数
     *
     * @param id
     * @param count
     * @return
     */
    int updateCommentCount(int id, int count);

    /**
     * 查询问题的条数
     * @return
     */
    int findQuestionCount();

    /**
     * 根据用户id查询对应多少条记录
     * @param userId
     * @return
     */
    int findQuestionCountByUserId(int userId);

    /**
     * 根据questionID删除对应的question
     * @param questionId
     * @return
     */
    int deleteQuestionById(Integer questionId);
}
