package com.know.wenda.controller;

import com.know.wenda.configuration.component.AccountInfo;
import com.know.wenda.domain.CommentDO;
import com.know.wenda.domain.EntityTypeDO;
import com.know.wenda.execption.ConsumerException;
import com.know.wenda.execption.GlobalErrorEnum;
import com.know.wenda.service.ICommentService;
import com.know.wenda.service.IQuestionService;
import com.know.wenda.async.EventModel;
import com.know.wenda.async.EventProducer;
import com.know.wenda.constant.EventType;
import com.know.wenda.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * CommentController
 *
 * @author hlb
 */
@Controller
@Api(description = "评论")
public class CommentController {

    @Autowired
    private AccountInfo accountInfo;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private EventProducer eventProducer;


    /**
     * 新增评论
     * @param questionId
     * @param content
     * @return
     */
    @ApiOperation(value = "评论",notes = "新增评论")
    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {

        CommentDO comment = new CommentDO();
        comment.setContent(content);
        if (accountInfo.getUser() != null) {
            comment.setUserId(accountInfo.getUser().getId());
        } else {
            // TODO 已经修改为必须登录才能评论，这里已经废除
            comment.setUserId(JsonUtil.ANONYMOUS_USERID);
        }
        comment.setEntityType(EntityTypeDO.ENTITY_QUESTION);
        comment.setEntityId(questionId);
        int result = commentService.addComment(comment);
        if(result == 0){
            throw new ConsumerException(GlobalErrorEnum.ADD_COMMENT_EXCEPTION.getCode(),GlobalErrorEnum.ADD_COMMENT_EXCEPTION.getMessage());
        }
        int count = commentService.getCommentCountByEntity(comment.getEntityId(), comment.getEntityType());
        questionService.updateCommentCount(comment.getEntityId(), count);
        // 发起一个增加评论的消息
        eventProducer.fireEvent(new EventModel(EventType.COMMENT).setEntityId(questionId).setActorId(comment.getUserId()));
        // 评论后重新定向到问题页面
        return String.format("redirect:/question/%d",questionId);
    }
}
