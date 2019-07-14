package com.know.wenda;

import com.know.wenda.base.KnownoWendaApplicationTests;
import com.know.wenda.domain.CommentDO;
import com.know.wenda.service.ICommentService;
import org.junit.Test;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * CommentServiceTest
 *
 * @author hlb
 */
public class CommentServiceTest extends KnownoWendaApplicationTests {

    @Resource
    private ICommentService commentService;

    @Test
    public void getCommentByUserId(){
        List<CommentDO> commentDOS = commentService.getCommentByUserId(15,1,3);
        Assert.isTrue(commentDOS.size() == 3);
    }

    @Test
    public void findQuestionCommentCouontByUserId(){
        int count = commentService.getQuestionCommentCountByUserId(15,1).size();
        Assert.isTrue(count == 5);
    }

}