package com.know.wenda.controller;

import com.know.wenda.annotation.OperationLog;
import com.know.wenda.configuration.component.AccountInfo;
import com.know.wenda.domain.EntityTypeDO;
import com.know.wenda.domain.QuestionDO;
import com.know.wenda.domain.UserDO;
import com.know.wenda.service.FollowService;
import com.know.wenda.service.GratitudeService;
import com.know.wenda.service.IQuestionService;
import com.know.wenda.service.IUserService;
import com.know.wenda.vo.VoMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * HomeController
 *
 * @author hlb
 */
@Api(description = "主页面")
@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private IUserService userService;

    @Autowired
    private FollowService followService;

    @Autowired
    private GratitudeService gratitudeService;

    @Autowired
    private AccountInfo accountInfo;
    private static int BASE_NUM = 8;

    /**
     * 获取Question集
     *
     * @param model
     * @param num
     * @return
     */
    @ApiOperation(value = "首页入口", notes = "首页入口")
    @OperationLog(method = "首页入口")
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(
            @RequestParam(value = "num", required = false) Integer num,
            Model model) {
        int count = questionService.findQuestionCount();
        if (ObjectUtils.isEmpty(num)) {
            num = IndexController.BASE_NUM;
        } else {
            if (count >= num + IndexController.BASE_NUM) {
                num = num + IndexController.BASE_NUM;
            } else {
                num = count;
            }
        }
        UserDO userDO = accountInfo.getUser();
        model.addAttribute("vos", getQuestions(0, 0, num));
        model.addAttribute("num", num);
        model.addAttribute("curUser", userDO);
        model.addAttribute("count", count);
        return "index";
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
        // 获取当前用户感谢的话题ids
        List<Integer> questionIds = gratitudeService.getQuestionIds(accountInfo.getUser().getId());
        List<VoMap> vos = new ArrayList<>();
        for (QuestionDO questionDO : questionDOList) {
            if (!ObjectUtils.isEmpty(questionDO)) {
                VoMap vo = new VoMap();
                // 判断当前登陆用户是否感谢这个话题
                if(!CollectionUtils.isEmpty(questionIds)){
                    if(questionIds.contains(questionDO.getId())){
                        questionDO.setGratitudeFlag(Boolean.TRUE);
                    }else {
                        questionDO.setGratitudeFlag(Boolean.FALSE);
                    }
                }
                vo.set("questionDO", questionDO);
                vo.set("userDO", userService.getUser(questionDO.getUserId()));
                vo.set("followCount", followService.getFollowerCount(EntityTypeDO.ENTITY_QUESTION, questionDO.getId()));
                vos.add(vo);
            }
        }
        return vos;
    }


}
