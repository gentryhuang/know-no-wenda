package com.know.wenda.controller;

import com.know.wenda.configuration.component.AccountInfo;
import com.know.wenda.domain.MessageDO;
import com.know.wenda.domain.UserDO;
import com.know.wenda.util.JsonUtil;
import com.know.wenda.vo.VoMap;
import com.know.wenda.service.IMessageService;
import com.know.wenda.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


/**
 * MessageController
 *
 * @author hlb
 */
@Api(description = "消息")
@Controller
public class MessageController {
    @Autowired
    private AccountInfo accountInfo;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IUserService userService;
    String systemUrl = "https://zfw-avatar.oss-cn-hangzhou.aliyuncs.com/%s.jpg";

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    /**
     * 获取用户对应的消息列表
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "消息列表",notes = "消息列表")
    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String getConversationList(Model model) {
        if (ObjectUtils.isEmpty(accountInfo.getUser())) {
            return "redirect:/reglogin";
        }
        // 取出当前用户id
        int localUserId = accountInfo.getUser().getId();
        List<MessageDO> conversationList = messageService.getConversationList(localUserId, 0, Integer.MAX_VALUE);
        List<VoMap> conversations = new ArrayList<>();
        for (MessageDO messageDO : conversationList) {
            VoMap vo = new VoMap();
            // 加入消息
            vo.set("messageDO", messageDO);
            // 拿到对方id，不要自己的id，一条会话是相互的
            int targetId = messageDO.getFromId() == localUserId ? messageDO.getToId() : messageDO.getFromId();
            UserDO userDO = userService.getUser(targetId);
            if(ObjectUtils.isEmpty(userDO)){
                userDO = new UserDO();
                userDO.setHeadUrl(String.format(systemUrl,"system"));
            }
            vo.set("userDO", userDO);
            // 拿到未读的条数
            vo.set("unread", messageService.getConversationUnreadCount(localUserId, messageDO.getConversationId()));
            conversations.add(vo);
        }
        model.addAttribute("conversations", conversations);
        return "letter";
    }

    /**
     * 某条会话详情列表
     *
     * @param model
     * @param conversationId
     * @return
     */
    @ApiOperation(value = "会话详情",notes = "会话详情")
    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String getConversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
        if (ObjectUtils.isEmpty(accountInfo.getUser())) {
            return "redirect:/reglogin";
        }
        // 取出当前用户id
        int localUserId = accountInfo.getUser().getId();
        // 会话消息列表
        List<MessageDO> messageDOList = messageService.getConversationDetail(conversationId, 0, 10);
        // 返回数据的载体
        List<VoMap> messages = new ArrayList<VoMap>();
        for (MessageDO messageDO : messageDOList) {
            VoMap vo = new VoMap();
            vo.set("messageDO", messageDO);
            UserDO userDO = userService.getUser(messageDO.getFromId());
            if(ObjectUtils.isEmpty(userDO)){
                userDO = new UserDO();
                userDO.setHeadUrl(String.format(systemUrl,"system"));
            }
            vo.set("userDO", userDO);
            messages.add(vo);
        }
        model.addAttribute("messages", messages);
        // 把未读的标记已读
        messageService.modifyReadStatusByToId(localUserId, conversationId);

        return "letterDetail";
    }

    /**
     * 发一条消息
     *
     * @param toName
     * @param content
     * @return
     */
    @ApiOperation(value = "发站内信",notes = "发站内信")
    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {
        try {
            if (ObjectUtils.isEmpty(accountInfo.getUser())) {
                return JsonUtil.getJSONString(999, "未登录");
            }

            UserDO user = userService.selectByName(toName);
            if (ObjectUtils.isEmpty(user)) {
                return JsonUtil.getJSONString(1, "用户不存在");
            }

            MessageDO messageDO = new MessageDO();
            // 消息发送方
            messageDO.setFromId(accountInfo.getUser().getId());
            // 消息接受方
            messageDO.setToId(user.getId());
            messageDO.setContent(content);
            // 刚发，没有读取
            messageDO.setHasRead(0);
            messageService.addMessage(messageDO);
            // 0表示成功
            return JsonUtil.getJSONString(0);

        } catch (Exception e) {
            logger.error("发送消息失败" + e.getMessage());
            // 1 表示失败
            return JsonUtil.getJSONString(1, "发信失败");
        }
    }

    /**
     * 删除会话列表
     *
     * @return
     */
    @ApiOperation(value = "删除会话列表",notes = "删除会话列表")
    @RequestMapping(path = "/deleteConversation", method = RequestMethod.GET)
    public String deleteConversation(@RequestParam("conversationId") String conversationId) {
        try {
            if (ObjectUtils.isEmpty(accountInfo.getUser())) {
                return "redirect:/reglogin";
            }
            // 取出当前用户id
            int localUserId = accountInfo.getUser().getId();
            // 删出会话列表
            messageService.deleteConversation(localUserId, conversationId);
        } catch (Exception e) {
            logger.error("删除会话列表失败");
        }
        return "forward:/msg/list";
    }

    /**
     * 删除会话某条详情信息
     *
     * @param mId
     * @param conversationId
     * @return
     */
    @ApiOperation(value = "删除会话",notes = "删除会话")
    @RequestMapping(path = "/deleteDetailConversationBycId", method = RequestMethod.GET)
    public String deleteDetailConversationBycId(@RequestParam("mId") int mId,
                                                @RequestParam("conversationId") String conversationId) {
        try {
            if (StringUtils.isEmpty(mId)) {
                logger.error("会话详情id不存在");
            }
            messageService.deleteDetailConversationById(mId);
        } catch (Exception e) {
            logger.error("删除会话详情失败,会话id={}", mId);
        }
        return "redirect:/msg/detail?conversationId=" + conversationId;
    }

}
