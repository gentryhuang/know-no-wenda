
package com.know.wenda.dao.mapper;

import com.know.wenda.domain.QuestionDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


/**
 *  QuestionMapper
 *
 * @author shunhua
 *
 */
@Mapper
public interface QuestionMapper {

    /**
     * 新增
     * @param questionDO
     * @return
     */
    int addQuestion(QuestionDO questionDO);

    /**
     * 多条件查询
     * @param map
     * @return
     */
    List<QuestionDO> selectLatestQuestions(Map<String,Integer> map);

    /**
     * 根据PK获取
     * @param id
     * @return
     */
    QuestionDO get(int id);

    /**
     * 更新问题条数
     * @param map
     * @return
     */
    int updateCommentCount(Map<String,Integer>  map);

    /**
     * 查询当前问题的条数
     * @return
     */
    int getQuestionCount();

    /**
     * 根据用户id查看问题条数
     * @param userId
     * @return
     */
    int getQuestionCountByUserId(int userId);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteQuestionById(Integer id);
}
