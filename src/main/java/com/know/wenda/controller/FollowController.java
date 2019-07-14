package com.know.wenda.controller;

import com.know.wenda.async.EventModel;
import com.know.wenda.async.EventProducer;
import com.know.wenda.constant.EventType;
import com.know.wenda.configuration.component.AccountInfo;
import com.know.wenda.domain.*;
import com.know.wenda.service.ICommentService;
import com.know.wenda.service.IQuestionService;
import com.know.wenda.service.IUserService;
import com.know.wenda.service.FollowService;
import com.know.wenda.util.JsonUtil;
import com.know.wenda.vo.VoMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * FollowController
 *
 * @author hlb
 */
@Api(description = "关注")
@Controller
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private IUserService userService;

    @Autowired
    private AccountInfo accountInfo;

    @Autowired
    private EventProducer eventProducer;
    private static int BASE_NUM = 5;

    /**
     * 关注用户
     * @param userId
     * @return
     */
    @ApiOperation(value = "关注人",notes = "关注人")
    @RequestMapping(path = {"/followUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId) {
        if (ObjectUtils.isEmpty(accountInfo.getUser())) {
            // 如果没有登录的话，返回999
            return JsonUtil.getJSONString(999);
        }
        // 关注某个用户
        boolean ret = followService.follow(accountInfo.getUser().getId(), EntityTypeDO.ENTITY_USER, userId);
        // 关注某个用户事件放入队列中
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(accountInfo.getUser().getId())
                .setEntityId(userId)
                .setEntityType(EntityTypeDO.ENTITY_USER)
                .setEntityOwnerId(userId));

        // 返回关注的人数
        // 成功返回0 失败返回1
        return JsonUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(accountInfo.getUser().getId(), EntityTypeDO.ENTITY_USER)));
    }

    /**
     * 取消关注用户
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "取消关注人",notes = "取消关注人")
    @RequestMapping(path = {"/unfollowUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int userId) {
        if (ObjectUtils.isEmpty(accountInfo.getUser())) {
            // 如果没有登录的话，返回999
            return JsonUtil.getJSONString(999);
        }
        // 取消关注用户
        boolean ret = followService.unfollow(accountInfo.getUser().getId(), EntityTypeDO.ENTITY_USER, userId);
        // 取消关注某个用户事件放入队列中 （暂时不做处理）
      /*  eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(accountInfo.getUser().getId()).setEntityId(userId)
                .setEntityType(EntityTypeDO.ENTITY_USER).setEntityOwnerId(userId));*/

        // 返回关注的人数
        return JsonUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(accountInfo.getUser().getId(), EntityTypeDO.ENTITY_USER)));
    }

    /**
     * 关注问题
     * @param questionId
     * @return
     */
    @ApiOperation(value = "关注问题",notes = "关注问题")
    @RequestMapping(path = {"/followQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId) {
        if (ObjectUtils.isEmpty(accountInfo.getUser())) {
            // 如果没有登录的话，返回999
            return JsonUtil.getJSONString(999);
        }
        QuestionDO q = questionService.getById(questionId);
        if (ObjectUtils.isEmpty(q)) {
            return JsonUtil.getJSONString(1, "问题不存在");
        }
        boolean ret = followService.follow(accountInfo.getUser().getId(), EntityTypeDO.ENTITY_QUESTION, questionId);
        /**
         * 发布一个关注问题的消息
         */
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(accountInfo.getUser().getId())
                .setEntityId(questionId)
                .setEntityType(EntityTypeDO.ENTITY_QUESTION)
                .setEntityOwnerId(q.getUserId()));

        Map<String, Object> info = new HashMap<>(16);
        info.put("headUrl", accountInfo.getUser().getHeadUrl());
        info.put("name", accountInfo.getUser().getName());
        info.put("id", accountInfo.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityTypeDO.ENTITY_QUESTION, questionId));
        return JsonUtil.getJSONString(ret ? 0 : 1, info);
    }

    /**
     * 取消关注问题
     * @param questionId
     * @return
     */
    @ApiOperation(value = "取消问题",notes = "取消问题")
    @RequestMapping(path = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {
        if (ObjectUtils.isEmpty(accountInfo.getUser())) {
            // 如果没有登录的话，返回999
            return JsonUtil.getJSONString(999);
        }
        QuestionDO q = questionService.getById(questionId);
        if (ObjectUtils.isEmpty(q)) {
            return JsonUtil.getJSONString(1, "问题不存在");
        }
        boolean ret = followService.unfollow(accountInfo.getUser().getId(), EntityTypeDO.ENTITY_QUESTION, questionId);
        // 暂时不做处理
      /*  eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(accountInfo.getUser().getId())
                .setEntityId(questionId)
                .setEntityType(EntityTypeDO.ENTITY_QUESTION)
                .setEntityOwnerId(q.getUserId()));*/

        Map<String, Object> info = new HashMap<>(16);
        info.put("headUrl",accountInfo.getUser().getHeadUrl());
        info.put("name",accountInfo.getUser().getName());
        info.put("id", accountInfo.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityTypeDO.ENTITY_QUESTION, questionId));
        return JsonUtil.getJSONString(ret ? 0 : 1, info);
    }

    /**
     * 当前用户的粉丝
     * @param model
     * @param userId
     * @return
     */
    @ApiOperation(value = "粉丝",notes = "当前用户的粉丝")
    @RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId, @RequestParam(value = "num",required = false) Integer num) {
        // 获取用户粉丝数
        int count = (int) followService.getFollowerCount(userId,EntityTypeDO.ENTITY_USER);
        if(ObjectUtils.isEmpty(num)){
            num = FollowController.BASE_NUM;
        }else {
            if(count >= num + FollowController.BASE_NUM){
                num = num + FollowController.BASE_NUM;
            }else {
                num = count;
            }
        }
        List<Integer> followerIds = followService.getFollowers(EntityTypeDO.ENTITY_USER, userId, 0, num);
        if(CollectionUtils.isEmpty(followerIds)){
            // TODO 粉丝为0，直接返回
            model.addAttribute("curUser", userService.getUser(userId));
            model.addAttribute("followerCount",0);
            return "followers";
        }
        if (accountInfo.getUser() != null) {
            model.addAttribute("followers", getUsersInfo(accountInfo.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(EntityTypeDO.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        model.addAttribute("num",num);
        return "followers";
    }

    /**
     * 当前用户关注的人
     * @param model
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取关注的人",notes = "获取关注的人")
    @RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid") int userId, @RequestParam(value = "num",required = false) Integer num) {
        // 获取用户关注的人数
        int count = (int) followService.getFolloweeCount(userId,EntityTypeDO.ENTITY_USER);
        if(ObjectUtils.isEmpty(num)){
            num = FollowController.BASE_NUM;
        }else {
            if(count >= num + FollowController.BASE_NUM){
                num = num + FollowController.BASE_NUM;
            }else {
                num = count;
            }
        }
        List<Integer> followeeIds = followService.getFollowees(userId, EntityTypeDO.ENTITY_USER, 0, num);
        if(CollectionUtils.isEmpty(followeeIds)){
            model.addAttribute("curUser", userService.getUser(userId));
            model.addAttribute("followeeCount",0);
            return "followees";
        }
        if (accountInfo.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(accountInfo.getUser().getId(), followeeIds));
        } else { // 没有登录
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityTypeDO.ENTITY_USER));
        model.addAttribute("curUser", userService.getUser(userId));
        model.addAttribute("num",num);
        return "followees";
    }

    /**
     * 我的粉丝或我关注的
     * @param localUserId
     * @param userIds
     * @return
     */
    private List<VoMap> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<VoMap> userInfos = new ArrayList<VoMap>();
        for (Integer uid : userIds) {
            UserDO userDO = userService.getUser(uid);
            if (userDO == null) {
                continue;
            }
            VoMap vo = new VoMap();
            vo.set("userDO", userDO);
            // 用户评论数量
            vo.set("commentCount", commentService.getUserCommentCount(uid));
            // 用户粉丝量
            vo.set("followerCount", followService.getFollowerCount(EntityTypeDO.ENTITY_USER, uid));
            // 用户关注量
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityTypeDO.ENTITY_USER));
            if (localUserId != 0) {
                // 当前用户是否关注了对应的用户
                vo.set("followed", followService.isFollower(localUserId, EntityTypeDO.ENTITY_USER, uid));
            } else { // 没有登录
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}
