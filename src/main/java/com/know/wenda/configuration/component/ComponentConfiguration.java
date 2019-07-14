package com.know.wenda.configuration.component;

import com.know.wenda.filter.CommonFilter;
import com.know.wenda.interceptor.LoginRequiredInterceptor;
import com.know.wenda.interceptor.PassportInterceptor;
import com.thetransactioncompany.cors.CORSFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * WendaWebConfiguration
 *
 * @author hlb
 */
@Component
public class ComponentConfiguration extends WebMvcConfigurerAdapter {


    @Autowired
    private PassportInterceptor passportInterceptor;
    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 共享用户的拦截器
        registry.addInterceptor(passportInterceptor)
                .excludePathPatterns("/getCaptcha");
        // 访问特定页面需要进行身份认证
        registry.addInterceptor(loginRequiredInterceptor)
                .addPathPatterns("/")
                .addPathPatterns("/user/*")
                .addPathPatterns("/question/*")
                .addPathPatterns("/comment/*")
                .addPathPatterns("/pullfeeds")
                .addPathPatterns("/followQuestion")
                .excludePathPatterns("/getCaptcha");
        super.addInterceptors(registry);
    }

    /**
     * 静态资源处理
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    /**
     * 字符编码过滤器
     */
    @Bean
    public FilterRegistrationBean addCharacterEncodingFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CharacterEncodingFilter());
        registration.addInitParameter("encoding", "UTF-8");
        registration.addInitParameter("forceEncoding", "UTF-8");
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;

    }


    /**
     * 支持跨域处理
     */
    @Bean
    public FilterRegistrationBean addCORSFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CORSFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(2);
        return registration;
    }


    /**
     * 通用过滤器
     */
    @Bean
    public FilterRegistrationBean addCommonFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CommonFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(2);
        return registration;
    }

}
