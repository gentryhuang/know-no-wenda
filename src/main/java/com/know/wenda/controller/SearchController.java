package com.know.wenda.controller;

import com.know.wenda.domain.EntityTypeDO;
import com.know.wenda.domain.QuestionDO;
import com.know.wenda.execption.ConsumerException;
import com.know.wenda.execption.GlobalErrorEnum;
import com.know.wenda.vo.VoMap;
import com.know.wenda.service.FollowService;
import com.know.wenda.service.IQuestionService;
import com.know.wenda.service.IUserService;
import com.know.wenda.configuration.component.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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


/**
 * SearchController
 *
 * @author hlb
 */
@Api(description = "检索")
@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    @Autowired
    private SearchService searchService;

    @Autowired
    private FollowService followService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IQuestionService questionService;

    /**
     * @param keyword
     * @param offset
     * @param count
     * @param model
     * @return
     */
    @ApiOperation(value = "搜索问题", notes = "搜索问题")
    @RequestMapping(path = {"/search"}, method = {RequestMethod.GET})
    public String search(@RequestParam("q") String keyword,
                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "count", defaultValue = "20") int count,
                         Model model) throws Exception {
        if (StringUtils.isBlank(keyword)) {
            throw new ConsumerException(GlobalErrorEnum.SEARCH_CONDITION_NULL_EXCEPTION.getCode(), GlobalErrorEnum.SEARCH_CONDITION_NULL_EXCEPTION.getMessage());
        }
        List<QuestionDO> questionDOList = searchService.searchQuestion(keyword, offset, count);
        List<VoMap> vos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(questionDOList)) {
            for (QuestionDO questionDO : questionDOList) {
                // 以数据库为依据
                QuestionDO q = questionService.getById(questionDO.getId());
                VoMap vo = new VoMap();
                // 数据库没有就不进行设置
                if (!ObjectUtils.isEmpty(q)) {
                    if (!ObjectUtils.isEmpty(questionDO.getContent())) {
                        q.setContent(questionDO.getContent());
                    }
                    if (!ObjectUtils.isEmpty(questionDO.getTitle())) {
                        q.setTitle(questionDO.getTitle());
                    }
                    vo.set("questionDO", q);
                    vo.set("followCount", followService.getFollowerCount(EntityTypeDO.ENTITY_QUESTION, questionDO.getId()));
                    vo.set("userDO", userService.getUser(q.getUserId()));
                    vos.add(vo);
                }
            }
        }
        model.addAttribute("vos", vos);
        model.addAttribute("keyword", keyword);
        return "result";
    }
}
