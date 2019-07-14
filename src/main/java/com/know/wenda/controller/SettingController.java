package com.know.wenda.controller;

import com.know.wenda.domain.UserDO;
import com.know.wenda.execption.ConsumerException;
import com.know.wenda.execption.GlobalErrorEnum;
import com.know.wenda.service.IUserService;
import com.know.wenda.system.logger.KnwLoggerFactory;
import com.know.wenda.system.logger.KnwLoggerMarkers;
import com.know.wenda.system.loggerfactory.LoggerUtil;
import com.know.wenda.util.MD5Util;
import com.know.wenda.configuration.component.FileUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * SettingController
 *
 * @author hlb
 */
@Api("设置")
@Controller
@RequestMapping("/setting")
public class SettingController {

    @Resource
    private IUserService userService;

    @Resource
    private FileUrlService fileUrlService;

    /**
     * 跳转到设置页面
     *
     * @return
     */
    @ApiOperation(value = "跳转到设置页面",notes = "跳转到设置页面")
    @RequestMapping(path = {"/user"}, method = {RequestMethod.POST})
    public String setting(@RequestParam("id") Integer id,
                          @RequestParam("password") String oldPass,
                          @RequestParam("pass") String pass,
                          @RequestParam("file") MultipartFile file) throws IOException {

        // 1 判断用户有效性
        UserDO userDO = userService.getUser(id);
        if (ObjectUtils.isEmpty(userDO)) {
            throw new ConsumerException(GlobalErrorEnum.SETTING_ERROR_USER_EXCEPTION.getCode(), GlobalErrorEnum.SETTING_ERROR_USER_EXCEPTION.getMessage());
        }
        // 2 判断密码是否正确
        if (!MD5Util.getMD5String(oldPass + userDO.getSalt()).equals(userDO.getPassword())) {
            throw new ConsumerException(GlobalErrorEnum.SETTING_ERROR_PASSWORD_EXCEPTION.getCode(),GlobalErrorEnum.SETTING_ERROR_PASSWORD_EXCEPTION.getMessage());
        }
        // 3 判断是否上传了文件
        String url = null;
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            // 校验上传文件类型是否符合
            if (!matchPrefix(fileName)) {
                // 不合适就不修改头像
                url = userDO.getHeadUrl();
                LoggerUtil.error(KnwLoggerFactory.EXCEPTION_HANDLER_LOGGER, KnwLoggerMarkers.EXCEPTION_HANDLER, KnwLoggerFactory.formatLog("头像文件类型不合适", fileName));
            } else { // 符合就上传
                url = fileUrlService.getFileUrl(file);
            }

        }
        // 盐值加密
        pass = MD5Util.getMD5String(pass + userDO.getSalt());
        userDO.setHeadUrl(url);
        userDO.setPassword(pass);
        // 修改数据库
        userService.updateUser(userDO);
        return "redirect:/";
    }

    /**
     * 验证上传的文件是否符合图片格式
     *
     * @param fileName
     * @return
     */
    private boolean matchPrefix(String fileName) {
        String reg = ".+(.JPEG|.jpeg|.JPG|.jpg|.png|.PNG|.gif|.GIF)$";
        Matcher matcher = Pattern.compile(reg).matcher(fileName);
        return matcher.find();
    }
}
