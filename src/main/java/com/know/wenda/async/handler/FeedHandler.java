package com.know.wenda.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.know.wenda.async.EventHandler;
import com.know.wenda.async.EventModel;
import com.know.wenda.constant.EventType;
import com.know.wenda.domain.EntityTypeDO;
import com.know.wenda.domain.FeedDO;
import com.know.wenda.domain.QuestionDO;
import com.know.wenda.domain.UserDO;
import com.know.wenda.service.FollowService;
import com.know.wenda.service.IFeedService;
import com.know.wenda.service.IQuestionService;
import com.know.wenda.service.IUserService;
import com.know.wenda.util.JedisAdapter;
import com.know.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * FeedHandler
 *
 * @author hlb
 */
@Component
public class FeedHandler implements EventHandler {

    @Autowired
    private FollowService followService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IFeedService feedService;
    @Autowired
    private JedisAdapter jedisAdapter;
    @Autowired
    private IQuestionService questionService;

    @Override
    public void doHandle(EventModel model) {

        // 当有人评论时，就构造一个新鲜事
        FeedDO feed = new FeedDO();
        // 类型
        feed.setType(model.getType().getValue());
        // 谁触发的
        feed.setUserId(model.getActorId());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null) {
            // 不支持的feed
            return;
        }
        // 新增Feed
        feedService.addFeed(feed);
        // 获得所有粉丝id，即触发事件的实体的粉丝
        List<Integer> followers = followService.getFollowers(EntityTypeDO.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);
        // 系统队列
        followers.add(0);
        // 给所有粉丝推事件
        for (int follower : followers) {
            // 每一个实体对应唯一的timelinekey
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            // 把新鲜事id加入队列中，key和粉丝的id绑定，即某个粉丝就可以获取推送的事件了
            jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
            // 限制最长长度，如果timelineKey的长度过大，就删除后面的新鲜事
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        //这里是关注行为关注行为和评论行为
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }

    /**
     * 构建基础数据
     * @param model
     * @return
     */
    private String buildFeedData(EventModel model) {
        Map<String, String> map = new HashMap<String, String>();
        // 触发用户,这个必须有的，没有就是异常情况
        UserDO actor = userService.getUser(model.getActorId());
        if (actor == null) {
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());
        // TODO 如果是评论或关注问题 （这里两种情况都针对问题，所以进行&&判断），后期业务扩展的需要进行别的操作，不仅需要修改这里，还应该修改feed页面
        if (model.getType() == EventType.COMMENT || (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityTypeDO.ENTITY_QUESTION)) {
            // 如果是评论，获取对应的问题
            QuestionDO questionDO = questionService.getById(model.getEntityId());
            if (questionDO == null) {
                return null;
            }
            map.put("questionId", String.valueOf(questionDO.getId()));
            map.put("questionTitle", questionDO.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }
}
