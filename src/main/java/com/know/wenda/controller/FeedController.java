package com.know.wenda.controller;

import com.know.wenda.configuration.component.AccountInfo;
import com.know.wenda.domain.EntityTypeDO;
import com.know.wenda.domain.FeedDO;
import com.know.wenda.service.IFeedService;
import com.know.wenda.service.FollowService;
import com.know.wenda.util.JedisAdapter;
import com.know.wenda.util.RedisKeyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


/**
 * FeedController
 *
 * @author hlb
 */
@Api(description = "发现")
@Controller
public class FeedController {

    @Autowired
    private IFeedService feedService;

    @Autowired
    private FollowService followService;

    @Autowired
    private AccountInfo accountInfo;

    @Autowired
    private JedisAdapter jedisAdapter;
    private static int BASE_NUM = 8;

    /**
     * 推送模式
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "推送",notes = "推送新鲜事")
    @RequestMapping(path = {"/pushfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    private String getPushFeeds(Model model) {
        // 当前没有登录就取系统0
        int localUserId = accountInfo.getUser() != null ? accountInfo.getUser().getId() : 0;
        // 直接从队列中取出当前登录用户被推送的新鲜事，这里取出10条
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0, 10);
        List<FeedDO> feeds = new ArrayList<FeedDO>();
        for (String feedId : feedIds) {
            FeedDO feedDO = feedService.getById(Integer.parseInt(feedId));
            if (!ObjectUtils.isEmpty(feedDO)) {
                feeds.add(feedDO);
            }
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

    /**
     * 拉取模式
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "拉取",notes = "拉取新鲜事")
    @RequestMapping(path = {"/pullfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    private String getPullFeeds(@RequestParam(value = "num", required = false) Integer num, Model model) {

        int localUserId = !ObjectUtils.isEmpty(accountInfo.getUser()) ? accountInfo.getUser().getId() : 0;
        List<Integer> followees = new ArrayList<>();
        // 登录了就获取关注的人
        if (localUserId != 0) {
            followees = followService.getFollowees(localUserId, EntityTypeDO.ENTITY_USER, Integer.MAX_VALUE);
        }
        int count = feedService.findFeedCount(followees);
        if (ObjectUtils.isEmpty(num)) {
            num = FeedController.BASE_NUM;
        } else {
            // 查询数据库当前用户关心的新鲜事的条数
            if (count >= num + FeedController.BASE_NUM) {
                num = num + FeedController.BASE_NUM;
            } else {
                num = count;
            }
        }
        // 如果数据足够就取出10条
        /** 注意这里优先根据关注的人拉取数据，如果没有关注的人(followees为空)，就从所有的新鲜事中取 */
        List<FeedDO> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, num);
        model.addAttribute("feeds", feeds);
        model.addAttribute("num", num);
        model.addAttribute("count", count);
        return "feeds";
    }
}
