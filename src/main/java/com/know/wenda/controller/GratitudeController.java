package com.know.wenda.controller;

import com.know.wenda.async.EventModel;
import com.know.wenda.async.EventProducer;
import com.know.wenda.configuration.component.AccountInfo;
import com.know.wenda.configuration.redis.RedisService;
import com.know.wenda.constant.EventType;
import com.know.wenda.constant.RedisConstant;
import com.know.wenda.constant.ResultCodeConstant;
import com.know.wenda.dto.GratitudeDTO;
import com.know.wenda.dto.JsonBean;
import com.know.wenda.service.GratitudeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;

/**
 * GratitudeController
 *
 * @author hlb
 */
@Controller
@Api("感谢")
public class GratitudeController {

    @Resource
    private GratitudeService gratitudeService;
    @Resource
    private AccountInfo accountInfo;
    @Resource
    private RedisService redisService;
    @Autowired
    private EventProducer eventProducer;

    /**
     * 新增感谢
     * @param gratitudeDTO
     * @return
     */
    @ApiOperation(value = "新增感谢",notes = "新增感谢")
    @RequestMapping(value = "/add_questionId",method = RequestMethod.GET)
    @ResponseBody
    public JsonBean addQuestionId(GratitudeDTO gratitudeDTO){
        // 加锁，防止点击过快
       long value = redisService.setnx(String.format(RedisConstant.RedisLockKey.REDIS_LOCK_KEY,gratitudeDTO.getQuestionId()),String.valueOf(gratitudeDTO.getQuestionId()),2);
        JsonBean jsonBean = new JsonBean();
       if(value == 0){
           jsonBean.setCode(ResultCodeConstant.Code.FAST);
           jsonBean.setResult(ResultCodeConstant.Result.FAST);
           return jsonBean;
       }
       Long result =  gratitudeService.addQuestionId(accountInfo.getUser().getId(),gratitudeDTO.getQuestionId());
       if(ObjectUtils.isEmpty(result)){
          jsonBean.setCode(ResultCodeConstant.Code.FALSE);
          jsonBean.setResult(ResultCodeConstant.Result.FALSE);
          return jsonBean;
       }
       // 发起感谢消息
        eventProducer.fireEvent(new EventModel(EventType.GRATITUDE)
                .setEntityOwnerId(gratitudeDTO.getUserId())
                .setExt("questionId",String.valueOf(gratitudeDTO.getQuestionId()))
                .setActorId(accountInfo.getUser().getId()));
       jsonBean.setCode(ResultCodeConstant.Code.CODE);
       jsonBean.setResult(ResultCodeConstant.Result.OK);
       return jsonBean;
    }

    /**
     * 删除感谢
     * @param gratitudeDTO
     * @return
     */
    @ApiOperation(value = "删除感谢",notes = "删除感谢")
    @RequestMapping(value = "/del_questionId",method = RequestMethod.GET)
    @ResponseBody
    public JsonBean delQuestion(GratitudeDTO gratitudeDTO){
        // 加锁，防止点击过快
        long value = redisService.setnx(String.format(RedisConstant.RedisLockKey.REDIS_LOCK_KEY,gratitudeDTO.getQuestionId()),String.valueOf(gratitudeDTO.getQuestionId()),2);
        JsonBean jsonBean = new JsonBean();
        if(value == 0){
            jsonBean.setCode(ResultCodeConstant.Code.FAST);
            jsonBean.setResult(ResultCodeConstant.Result.FAST);
            return jsonBean;
        }
        Long result = gratitudeService.delQuestionById(accountInfo.getUser().getId(),gratitudeDTO.getQuestionId());
        if(ObjectUtils.isEmpty(result)){
            jsonBean.setCode(ResultCodeConstant.Code.FALSE);
            jsonBean.setResult(ResultCodeConstant.Result.FALSE);
            return jsonBean;
        }
        jsonBean.setCode(ResultCodeConstant.Code.CODE);
        jsonBean.setResult(ResultCodeConstant.Result.OK);
        return jsonBean;
    }

}