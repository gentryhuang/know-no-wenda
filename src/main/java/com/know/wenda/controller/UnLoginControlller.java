package com.know.wenda.controller;

import com.know.wenda.service.IPassWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * UnLoginControlller
 *
 * @author hlb
 */
@Api(description = "找回密码")
@Controller
public class UnLoginControlller {
    @Resource
    private IPassWordService passWordService;

    /**
     * 无法登陆
     * @return
     */
    @ApiOperation(value = "无法登陆",notes = "无法登陆")
    @RequestMapping(path = "/unlogin",method = RequestMethod.GET)
    public String unLogin(){
        return "unLogin";
    }


    /**
     * 使用ajax异步操作
     *
     * 重置密码
     * @param email
     * @param captcha
     * @param captchaKey
     * @return
     */
    @ApiOperation(value = "重置密码",notes = "重置密码")
    @ResponseBody
    @RequestMapping(path = "reset_password",method = RequestMethod.POST)
    public String ResetPassword(@RequestParam("email")String  email,
                                @RequestParam("captcha") String captcha,
                                @CookieValue("captchaKey")String captchaKey){
      Map<String,String> result  = passWordService.resetPassword(email,captcha,captchaKey);
      return result.get("msg");
    }

}