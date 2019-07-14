package com.know.wenda.service.impl;

import com.know.wenda.dao.QuestionDAO;
import com.know.wenda.domain.QuestionDO;
import com.know.wenda.service.IQuestionService;
import com.know.wenda.configuration.component.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * QuestionServiceImpl
 *
 * @author hlb
 */
@Service
public class QuestionServiceImpl  implements IQuestionService {
    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    public QuestionDO getById(int id) {
        return questionDAO.getById(id);
    }

    @Override
    public int addQuestion(QuestionDO questionDO) {
        // 发布信息需要对发布的信息进行防脚本攻击处理
        questionDO.setTitle(HtmlUtils.htmlEscape(questionDO.getTitle()));
        questionDO.setContent(HtmlUtils.htmlEscape(questionDO.getContent()));
        // 敏感词过滤
        questionDO.setTitle(sensitiveFilter.filter(questionDO.getTitle()));
        questionDO.setContent(sensitiveFilter.filter(questionDO.getContent()));
        return questionDAO.addQuestion(questionDO) > 0 ? questionDO.getId() : 0;
    }

    @Override
    public List<QuestionDO> getLatestQuestions(int userId, int offset, int limit) {
        Map<String,Integer> map = new HashMap<>(3);
        map.put("userId",userId);
        map.put("offset",offset);
        map.put("limit",limit);
        return questionDAO.selectLatestQuestions(map);
    }

    @Override
    public int updateCommentCount(int id, int count) {
        Map<String,Integer> map = new HashMap<>(2);
        map.put("id",id);
        map.put("count",count);
        return questionDAO.updateCommentCount(map);
    }

    @Override
    public int findQuestionCount() {
        return questionDAO.getQuestionCount();
    }

    @Override
    public int findQuestionCountByUserId(int userId) {
        return questionDAO.findQuestionCountByUserId(userId);
    }

    @Override
    public int deleteQuestionById(Integer questionId) {
        return questionDAO.deleteQuestionById(questionId);
    }
}
