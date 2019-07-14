package com.know.wenda;

import com.know.wenda.base.KnownoWendaApplicationTests;
import com.know.wenda.dao.QuestionDAO;
import com.know.wenda.domain.QuestionDO;
import com.know.wenda.service.IQuestionService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * UserServiceImplTest
 *
 * @author hlb
 */
public class QuestionServiceImplTest extends KnownoWendaApplicationTests {

    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private IQuestionService questionService;

    @Test
    public void addQuestion(){
        QuestionDO questionDO = new QuestionDO();
        questionDO.setCommentCount(1);
        questionDO.setUserId(1);
        questionDO.setTitle(String.format("TITLE%d",1));
        questionDO.setContent(String.format("这是一个比较难的算法 %d",1));
        int result = questionDAO.addQuestion(questionDO);
        Assert.assertEquals(1,result);
    }

    @Test
    public void selectLastestQuestion(){
        Map<String,Integer> map = new HashMap<>();
        map.put("userId",1);
        map.put("offset",1);
        map.put("limit",2);
        List<QuestionDO> questionDOS = questionDAO.selectLatestQuestions(map);
        Assert.assertNotNull(questionDOS);

    }

    @Test
    public void selectLastestQuestionByUserId(){
      int count =  questionService.findQuestionCountByUserId(16);
      Assert.assertEquals(count,3);
    }

    @Test
    public void deleteQuestionById(){
        int count = questionService.deleteQuestionById(16);
        Assert.assertEquals(count,1);
    }

}