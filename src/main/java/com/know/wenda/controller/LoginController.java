package com.know.wenda.controller;

import com.know.wenda.annotation.OperationLog;
import com.know.wenda.configuration.component.CaptchaService;
import com.know.wenda.constant.StringConstant;
import com.know.wenda.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


/**
 * LoginController
 *
 * @author hlb
 */

@Api(description = "用户")
@Controller
public class LoginController {


    @Autowired
    private IUserService userService;

    @Autowired
    private CaptchaService captchaService;


    /**
     * 用户注册
     *
     * @param username
     * @param password
     * @param email
     * @param next
     * @param remember
     * @param model
     * @param response
     * @return
     */
    @ApiOperation(value = "注册",notes = "用户进行注册")
    @OperationLog(method = "注册")
    @RequestMapping(path = "/register", method = {RequestMethod.POST})
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("email") String email,
                           @RequestParam("captcha") String captcha,
                           @CookieValue("captchaKey") String captchaKey,
                           @RequestParam(value = "next", required = false) String next,
                           @RequestParam(value = "remember", defaultValue = "false") boolean remember,
                           Model model,
                           HttpServletResponse response) {

        Map<String, Object> map = userService.register(username, password, email, captcha, captchaKey);
        if (map.containsKey(StringConstant.Token.KEY_TOKNE)) {
            Cookie cookie = new Cookie(StringConstant.Token.KEY_TOKNE, map.get(StringConstant.Token.KEY_TOKNE).toString());
            cookie.setPath("/");
            if (remember) {
                cookie.setMaxAge(3600 * 24 * 5);
            }
            response.addCookie(cookie);
            if (StringUtils.isNotBlank(next)) {
                return "redirect:" + next;
            }
            return "redirect:/";
        } else {
            model.addAttribute("msg", map.get("msg"));
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            // index为0表示注册
            model.addAttribute("index", 0);
            return "login";
        }
    }


    /**
     * 登录
     *
     * @param model
     * @param account
     * @param password
     * @param next
     * @param remember
     * @param response
     * @return
     */

    @ApiOperation(value = "登陆",notes = "用户登陆")
    @OperationLog(method = "登录")
    @RequestMapping(path = "/login", method = {RequestMethod.POST})
    public String login(@RequestParam("account") String account,
                        @RequestParam("password") String password,
                        @RequestParam("captcha") String captcha,
                        @CookieValue("captchaKey") String captchaKey,
                        @RequestParam(value = "next", required = false) String next,
                        @RequestParam(value = "remember", defaultValue = "false") boolean remember,
                        Model model,
                        HttpServletResponse response) {

        Map<String, Object> map = userService.login(account, password, captcha, captchaKey);
        if (map.containsKey(StringConstant.Token.KEY_TOKNE)) {
            Cookie cookie = new Cookie(StringConstant.Token.KEY_TOKNE, map.get(StringConstant.Token.KEY_TOKNE).toString());
            cookie.setPath("/");
            // 如果选择记住我，就设置cookie一个星期
            if (remember) {
                cookie.setMaxAge(3600 * 24 * 7);
            }
            response.addCookie(cookie);
            // 如果带有next，说明是从没有权限的页面跳转过来的
            if (StringUtils.isNotBlank(next)) {
                return "redirect:" + next;
            }
            return "redirect:/";
        } else {
            model.addAttribute("msg", map.get("msg"));
            model.addAttribute("account", account);
            // index 为1 表示登录
            model.addAttribute("index", 1);
            return "login";
        }
    }


    /**
     * 登录注册页
     *
     * @param model
     * @param next
     * @return
     */
    @ApiOperation(value = "临时跳转",notes = "没有权限跳转接口为了保留路径")
    @OperationLog(method = "没有权限跳转到登录")
    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String regloginPage(Model model, @RequestParam(value = "next", required = false) String next) {
        model.addAttribute("next", next);
        return "login";
    }

    /**
     * 登出
     *
     * @param key_token
     * @return
     */
    @ApiOperation(value = "登出",notes = "用户登出")
    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue(StringConstant.Token.KEY_TOKNE) String key_token, HttpServletResponse response) {
        // 让redis中的token失效
        userService.logout(key_token);
        // 清除对应的Cookie
        Cookie cookie = new Cookie(StringConstant.Token.KEY_TOKNE, null);
        // 0代表立即删除
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/reglogin";
    }

    /**
     * 获取验证码
     *
     * @param response
     */
    @ApiOperation(value = "获取验证码",notes = "验证码用户登陆和注册")
    @RequestMapping(value = "/getCaptcha", method = RequestMethod.GET)
    public void getCaptcha(HttpServletResponse response) throws IOException {
        // 获取存储在Redis中的验证的key
        String captchaKey = captchaService.createCaptcha();
        // 把验证码的对应的key保存在浏览器的Cookie中
        String captcha = captchaService.getCaptcha(captchaKey);
        Cookie cookie = new Cookie("captchaKey", captchaKey);
        response.addCookie(cookie);
        OutputStream os = response.getOutputStream();
        // 图片化渲染验证码生成验证码的
        captchaService.sendCaptchaJPG(captcha, os);
    }

}
