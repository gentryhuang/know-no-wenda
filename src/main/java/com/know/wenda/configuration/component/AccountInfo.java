package com.know.wenda.configuration.component;

import com.know.wenda.domain.UserDO;
import org.springframework.stereotype.Component;

/**
 * AccountInfo
 *
 * @author hlb
 */
@Component
public class AccountInfo {
    /** 使用本地线程绑定各自的用户信息 */
    private static ThreadLocal<UserDO> users = new ThreadLocal<UserDO>();

    public UserDO getUser() {
        return users.get();
    }

    public void setUser(UserDO userDO) {
        users.set(userDO);
    }

    public void clear() {
        users.remove();
    }
}