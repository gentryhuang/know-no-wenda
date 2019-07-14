package com.know.wenda.controller;

import com.know.wenda.configuration.component.AccountInfo;
import com.know.wenda.domain.CommentDO;
import com.know.wenda.domain.EntityTypeDO;
import com.know.wenda.service.ICommentService;
import com.know.wenda.service.LikeService;
import com.know.wenda.async.EventModel;
import com.know.wenda.async.EventProducer;
import com.know.wenda.constant.EventType;
import com.know.wenda.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * LikeController
 *
 * @author hlb
 */
@Api(description = "点赞")
@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;

    @Autowired
    private AccountInfo accountInfo;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private EventProducer eventProducer;

    /**
     * 对评论点赞
     * @param commentId
     * @return
     */
    @ApiOperation(value = "评论点赞",notes = "评论点赞")
    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        if (ObjectUtils.isEmpty(accountInfo.getUser())) {
            // 返回999,代表没有登录
            return JsonUtil.getJSONString(999);
        }
        // 获取评论
        CommentDO comment = commentService.getCommentById(commentId);
        if(!ObjectUtils.isEmpty(comment)){
            // 把点赞事件推进redis消息队列中
            eventProducer.fireEvent(new EventModel(EventType.LIKE)
                    .setActorId(accountInfo.getUser().getId())
                    .setEntityId(commentId)
                    .setEntityType(EntityTypeDO.ENTITY_COMMENT)
                    .setEntityOwnerId(comment.getUserId())
                    .setExt("questionId", String.valueOf(comment.getEntityId())));
        }

        // 喜欢评论
        long likeCount = likeService.like(accountInfo.getUser().getId(), EntityTypeDO.ENTITY_COMMENT, commentId);
        // 返回数量
        return JsonUtil.getJSONString(0, String.valueOf(likeCount));
    }

    /**
     * 对评论踩
     * @param commentId
     * @return
     */
    @ApiOperation(value = "评论踩",notes = "评论踩")
    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        if (ObjectUtils.isEmpty(accountInfo.getUser())) {
            // 返回999,代表没有登录
            return JsonUtil.getJSONString(999);
        }
        long likeCount = likeService.disLike(accountInfo.getUser().getId(), EntityTypeDO.ENTITY_COMMENT, commentId);
        return JsonUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
