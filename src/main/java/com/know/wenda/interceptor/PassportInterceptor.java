package com.know.wenda.interceptor;

import com.know.wenda.configuration.component.AccountInfo;
import com.know.wenda.constant.RedisConstant;
import com.know.wenda.constant.StringConstant;
import com.know.wenda.constant.TimeConstant;
import com.know.wenda.dao.TokenDAO;
import com.know.wenda.dao.UserDAO;
import com.know.wenda.domain.TokenDO;
import com.know.wenda.domain.UserDO;
import com.know.wenda.util.JedisAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 *用来使已经登陆的请求链路可以共享用户信息
 *
 * PassportInterceptor
 *
 * @author hlb
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenDAO tokenDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AccountInfo accountInfo;

    @Autowired
    private JedisAdapter jedisAdapter;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String token = null;
        // 请求开始之前先拦截请求，判断用户的cookie中是否有token凭证
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if (cookie.getName().equals(StringConstant.Token.KEY_TOKNE)) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        // 如果有token凭证，那么验证这个token是否过期了
        if (token != null) {
            // TODO 不同用户同一个token值,加入一个后缀用于redis中区分不用用户
            TokenDO loginToken = jedisAdapter.getToken(String.format(StringConstant.Token.TOKEN,token));
            if(ObjectUtils.isEmpty(loginToken)){
                loginToken = tokenDAO.findByToken(token);
            }
             // 如果token无效就直接放行，下面链路中没有用户信息
            if (loginToken == null || loginToken.getExpired().before(new Date())) {
                return true;
            }
            // 如果token有效，那么就取出token绑定的用户信息
            UserDO user = userDAO.get(loginToken.getUserId());
            // 把token缓存起来 7天，减轻数据库压力
            jedisAdapter.setToken(String.format(StringConstant.Token.TOKEN,token),loginToken, TimeConstant.CacheExpireTime.EXPIRE_SECOND_HOME_TIME);
            // 把当前用户信息放入到上下文中，便于后面链路（这个请求）使用这个用户信息
            accountInfo.setUser(user);
        }
        // 这个必须返回true，否则就不会往下进行了
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

        // 在视图渲染之前，把信息保存到model中，这样视图就可以使用
        if (modelAndView != null && accountInfo.getUser() != null) {
            modelAndView.addObject(StringConstant.UserInfo.USERDO, accountInfo.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        // 清除当前请求对应的用户信息，防止内存泄露，即本地线程中用户信息过多
        accountInfo.clear();
    }
}
