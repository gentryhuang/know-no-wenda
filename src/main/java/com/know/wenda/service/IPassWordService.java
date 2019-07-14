package com.know.wenda.service;

import java.util.Map;

/**
 * IPassWordService
 *
 * @author hlb
 */
public interface IPassWordService {

    /**
     * 重置密码
     * @param email
     * @param captcha
     * @param captchaKey
     * @return
     */
    Map<String, String> resetPassword(String email, String captcha, String captchaKey);

}