package com.know.wenda.dao;

import com.know.wenda.dao.mapper.QuestionMapper;
import com.know.wenda.domain.QuestionDO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * QuestionDAO
 *
 * @author hlb
 */
@Repository
public class QuestionDAO {

    @Resource
    private QuestionMapper questionMapper;

    /**
     * 新增
     *
     * @param questionDO
     * @return
     */
    public int addQuestion(QuestionDO questionDO) {
        return questionMapper.addQuestion(questionDO);
    }

    /**
     * 多条件查询
     *
     * @param map
     * @return
     */
    public List<QuestionDO> selectLatestQuestions(Map<String, Integer> map) {
        return questionMapper.selectLatestQuestions(map);
    }

    /**
     * 根据PK获取
     *
     * @param id
     * @return
     */
    public QuestionDO getById(int id) {
        return questionMapper.get(id);
    }

    /**
     * 更新问题条数
     *
     * @param map
     * @return
     */
    public int updateCommentCount(Map<String, Integer> map) {
        return questionMapper.updateCommentCount(map);
    }

    /**
     * 查询问题的数目
     * @return
     */
    public int getQuestionCount(){
        return questionMapper.getQuestionCount();
    }

    /**
     * 根据用户获取对应的问题数
     * @param userId
     * @return
     */
    public int findQuestionCountByUserId(int userId) {
        return questionMapper.getQuestionCountByUserId(userId);
    }

    /**
     * 根据问题id删除对应的question
     * @param questionId
     */
    public int deleteQuestionById(Integer questionId) {
        return questionMapper.deleteQuestionById(questionId);
    }
}
