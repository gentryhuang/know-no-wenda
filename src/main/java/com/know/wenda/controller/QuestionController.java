package com.know.wenda.controller;

import com.know.wenda.async.EventModel;
import com.know.wenda.async.EventProducer;
import com.know.wenda.configuration.component.SearchService;
import com.know.wenda.constant.EventType;
import com.know.wenda.configuration.component.AccountInfo;
import com.know.wenda.domain.*;
import com.know.wenda.execption.ConsumerException;
import com.know.wenda.execption.GlobalErrorEnum;
import com.know.wenda.service.*;
import com.know.wenda.system.logger.KnwLoggerFactory;
import com.know.wenda.system.logger.KnwLoggerMarkers;
import com.know.wenda.system.loggerfactory.LoggerUtil;
import com.know.wenda.util.JsonUtil;
import com.know.wenda.vo.VoMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * QuestionController
 *
 * @author hlb
 */
@Api(description = "问题")
@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private AccountInfo accountInfo;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private FollowService followService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private EventProducer eventProducer;

    /**
     *
     * 查询某条具体问题
     * @param model
     * @param qid
     * @return
     */
    @ApiOperation(value = "查询问题",notes = "查询问题")
    @RequestMapping(value = "/question/{qid}", method = {RequestMethod.GET})
    public String questionDetail(Model model, @PathVariable("qid") int qid) {
        QuestionDO questionDO = questionService.getById(qid);
        model.addAttribute("questionDO", questionDO);
        List<CommentDO> commentList = commentService.getCommentsByEntity(qid, EntityTypeDO.ENTITY_QUESTION);
        List<VoMap> comments = new ArrayList<VoMap>();
        for (CommentDO comment : commentList) {
            VoMap vo = new VoMap();
            vo.set("comment", comment);
            // 没有登录就不显示，即不显示，登录才显示当前用户是否喜欢
            if (ObjectUtils.isEmpty(accountInfo.getUser())) {
                vo.set("liked", 0);
            } else {
                vo.set("liked", likeService.getLikeStatus(accountInfo.getUser().getId(), EntityTypeDO.ENTITY_COMMENT, comment.getId()));
            }
            // 该评论被点赞情况
            vo.set("likeCount", likeService.getLikeCount(EntityTypeDO.ENTITY_COMMENT, comment.getId()));
            vo.set("userDO", userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments", comments);
        List<VoMap> followUsers = new ArrayList<VoMap>();
        // 获取关注的用户信息
        List<Integer> users = followService.getFollowers(EntityTypeDO.ENTITY_QUESTION, qid, 20);
        for (Integer userId : users) {
            VoMap vo = new VoMap();
            UserDO u = userService.getUser(userId);
            if (u == null) {
                continue;
            }
            vo.set("name", u.getName());
            vo.set("headUrl", u.getHeadUrl());
            vo.set("id", u.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if (accountInfo.getUser() != null) {
            model.addAttribute("followed", followService.isFollower(accountInfo.getUser().getId(), EntityTypeDO.ENTITY_QUESTION, qid));
        } else {
            model.addAttribute("followed", false);
        }

        return "detail";
    }

    /**
     * 发表问题
     *
     * 这个接口是由前端页面的ajax调用的，调用成功后进行刷新页面，回到主页面
     *
     * @param title
     * @param content
     * @return
     */
    @ApiOperation(value = "发表问题",notes = "发表问题")
    @RequestMapping(value = "/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            QuestionDO questionDO = new QuestionDO();
            questionDO.setContent(content);
            questionDO.setTitle(title);
            // 发表问题，只能是登陆状态，过滤器进行过滤了，Js文件中也进行了判断
            questionDO.setUserId(accountInfo.getUser().getId());
            questionDO.setCommentCount(0);
            if (questionService.addQuestion(questionDO) > 0) {
                // 把新增问题消息加入队列中
                eventProducer.fireEvent(new EventModel(EventType.ADD_QUESTION)
                        .setActorId(questionDO.getUserId())
                        .setEntityId(questionDO.getId())
                        .setExt("title", questionDO.getTitle())
                        .setExt("content", questionDO.getContent()));
                return JsonUtil.getJSONString(0);
            }
        } catch (Exception e) {
            logger.error("增加题目失败" + e.getMessage());
            LoggerUtil.error(KnwLoggerFactory.BUSINESS_LOGGER, KnwLoggerMarkers.BUSINESS_INFO,"提问失败失败",title,content);
        }
        return JsonUtil.getJSONString(1, "失败");
    }

    /**
     * 删除问题
     * @param questionId
     * @return
     */
    @ApiOperation(value = "删除问题",notes = "删除问题")
    @RequestMapping(value = "/delete_question/{questionId}",method = RequestMethod.GET)
    public String deleteQuestion(@PathVariable("questionId")Integer questionId){

        questionService.deleteQuestionById(questionId);

        //TODO 删除索引库失败，虽然最终依据数据库为准，但最好进行补偿处理
        // 删除索引库中数据(false为失败)
        boolean result = searchService.deleteQuestion(questionId);
        return "redirect:/";
    }

    /**
     * 删除我的主页发表的问题
     * @param questionId
     * @param userId
     * @return
     */
    @ApiOperation(value = "删除主页的问题",notes = "删除主页问题")
    @RequestMapping(value = "/del_profile_question/{questionId}",method = RequestMethod.GET)
    public String deleteProfileQuestion(@PathVariable("questionId")Integer questionId,
                                        @RequestParam("userId")Integer userId){
        questionService.deleteQuestionById(questionId);
        //TODO 删除索引库失败，虽然最终依据数据库为准，但最好进行补偿处理
        // 删除索引库中数据(false为失败)
        boolean result = searchService.deleteQuestion(questionId);
        return String.format("redirect:/user/%d",userId);
    }
}
