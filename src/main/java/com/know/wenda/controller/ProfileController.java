package com.know.wenda.controller;

import com.know.wenda.configuration.component.AccountInfo;
import com.know.wenda.domain.CommentDO;
import com.know.wenda.domain.EntityTypeDO;
import com.know.wenda.domain.QuestionDO;
import com.know.wenda.domain.UserDO;
import com.know.wenda.service.*;
import com.know.wenda.vo.AnswerVO;
import com.know.wenda.vo.VoMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ProfileController
 *
 * @author hlb
 */

@Api(description = "用户操作")
@Controller
public class ProfileController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private FollowService followService;
    @Autowired
    private IQuestionService questionService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private AccountInfo accountInfo;
    private static int BASE_NUM_TWO = 2;
    private static int BASE_NUM_FIVE = 5;

    /**
     * 获取某个用户信息
     *
     * @param model
     * @param userId
     * @return
     */
    @ApiOperation(value = "用户信息",notes = "用户信息")
    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(@PathVariable("userId") int userId,
                            Model model) {
        model.addAttribute("vos", getQuestions(userId, 0, 10));
        UserDO userDO = userService.getUser(userId);
        VoMap vo = new VoMap();
        vo.set("userDO", userDO);
        vo.set("commentCount", commentService.getUserCommentCount(userId));
        vo.set("followerCount", followService.getFollowerCount(EntityTypeDO.ENTITY_USER, userId));
        vo.set("followeeCount", followService.getFolloweeCount(userId, EntityTypeDO.ENTITY_USER));
        if (!ObjectUtils.isEmpty(accountInfo.getUser())) {
            vo.set("followed", followService.isFollower(accountInfo.getUser().getId(), EntityTypeDO.ENTITY_USER, userId));
        } else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
        model.addAttribute("curUser",accountInfo.getUser());
        return "profile";
    }

    /**
     * 进行分页查找用户对应的问题
     *
     * @param userId
     * @param num
     * @param model
     * @return
     */
    @ApiOperation(value = "用户对应的问题",notes = "问题")
    @RequestMapping(path = {"/user"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userMore(
            @RequestParam(value = "userId") int userId,
            @RequestParam(value = "num", required = false) Integer num,
            Model model) {
        int count = questionService.findQuestionCountByUserId(userId);
        if (ObjectUtils.isEmpty(num)) {
            num = ProfileController.BASE_NUM_FIVE;
        } else {
            if (count >= num + ProfileController.BASE_NUM_FIVE) {
                num = num + ProfileController.BASE_NUM_FIVE;
            }
        }
        model.addAttribute("vos", getQuestions(userId, 0, num));
        UserDO userDO = userService.getUser(userId);
        VoMap vo = new VoMap();
        vo.set("userDO", userDO);
        vo.set("commentCount", commentService.getUserCommentCount(userId));
        vo.set("followerCount", followService.getFollowerCount(EntityTypeDO.ENTITY_USER, userId));
        vo.set("followeeCount", followService.getFolloweeCount(userId, EntityTypeDO.ENTITY_USER));
        if (!ObjectUtils.isEmpty(accountInfo.getUser())) {
            vo.set("followed", followService.isFollower(accountInfo.getUser().getId(), EntityTypeDO.ENTITY_USER, userId));
        } else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
        model.addAttribute("num", num);
        model.addAttribute("count", count);
        model.addAttribute("curUser",accountInfo.getUser());
        return "profile";
    }

    /**
     * 用户的回答信息
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "用户的回答",notes = "回答")
    @RequestMapping(path = {"/answer/{userId}"}, method = {RequestMethod.GET})
    public String answer(@PathVariable("userId") Integer userId,
                         @RequestParam(value = "num", required = false) Integer num,
                         Model model) {
        // 1 获取用户信息
        UserDO userDO = userService.getUser(userId);
        // 用户对问题评论列表
        List<CommentDO> questionComments = commentService.getQuestionCommentCountByUserId(userId, EntityTypeDO.ENTITY_QUESTION);
        // 用户评论的数量
        int count = questionComments.size();
        /*
           用户评论对应的问题question数(涉及的全部entity_id，包含多个评论对应同一个问题entity_id)，这是因为需要真实评论值作为分页的依据，
           真实分页是根据评论的数目进行分页，而非根据问题数目，如果一个问题中有多条评论也没有关系，去重即可
         */
        // TODO 分页有瑕疵，逻辑判断太复杂
        List<Integer> entityIds = questionComments.stream().map(CommentDO::getEntityId).collect(Collectors.toList());
        // 该用户回答涉及的问题数量
        int questionCount = entityIds.size();
        if (ObjectUtils.isEmpty(num)) {
            num = ProfileController.BASE_NUM_TWO;
        } else {
            if (questionCount >= num + ProfileController.BASE_NUM_TWO) {
                num = num + ProfileController.BASE_NUM_TWO;
            } else {
                num = questionCount;
            }
        }
        // 2 用户的回答（评论）列表
        List<CommentDO> commentDOS = commentService.getCommentByUserId(userId, EntityTypeDO.ENTITY_QUESTION, num);
        // 3 获取评论中评论的是问题的实体id，这个需要去重，只要唯一的entity_id即可
        Set<Integer> questionIds = commentDOS.stream().map(CommentDO::getEntityId).collect(Collectors.toSet());
        // 回答相关的结果集
        List<AnswerVO> answerVOS = new ArrayList<>();
        questionIds.stream().forEach(qid -> {
            AnswerVO answerVO = new AnswerVO();
            QuestionDO questionDO = questionService.getById(qid);
            answerVO.setQuestionDO(questionDO);
            // 获取问题对应的评论信息
            List<CommentDO> commentList = commentService.getCommentsByEntity(qid, EntityTypeDO.ENTITY_QUESTION);
            List<VoMap> comments = new ArrayList<VoMap>();
            for (CommentDO comment : commentList) {
                VoMap vo = new VoMap();
                vo.set("comment", comment);
                // 没有登录就不显示，即不显示，登录才显示当前用户是否喜欢
                if (accountInfo.getUser() == null) {
                    vo.set("liked", 0);
                } else {
                    vo.set("liked", likeService.getLikeStatus(accountInfo.getUser().getId(), EntityTypeDO.ENTITY_COMMENT, comment.getId()));
                }
                // 该评论被点赞情况
                vo.set("userDO", userService.getUser(comment.getUserId()));
                vo.set("likeCount", likeService.getLikeCount(EntityTypeDO.ENTITY_COMMENT, comment.getId()));
                comments.add(vo);
            }
            answerVO.setComments(comments);
            answerVOS.add(answerVO);
        });
        model.addAttribute("answers", answerVOS);
        model.addAttribute("curUser", userDO);
        model.addAttribute("answerNum",count);
        model.addAttribute("questionNum", questionCount);
        model.addAttribute("num", num);
        return "answer";
    }


    /**
     * 获取用户问题
     *
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    private List<VoMap> getQuestions(int userId, int offset, int limit) {
        List<QuestionDO> questionDOList = questionService.getLatestQuestions(userId, offset, limit);
        List<VoMap> vos = new ArrayList<>();
        for (QuestionDO questionDO : questionDOList) {
            VoMap vo = new VoMap();
            vo.set("questionDO", questionDO);
            vo.set("followCount", followService.getFollowerCount(EntityTypeDO.ENTITY_QUESTION, questionDO.getId()));
            vo.set("userDO", userService.getUser(questionDO.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

}