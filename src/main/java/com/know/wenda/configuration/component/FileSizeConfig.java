package com.know.wenda.configuration.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 *
 * 处理文件传输过大，不配置过大会报异常
 * FileSizeConfig
 *
 * @author hlb
 */
@Configuration
public class FileSizeConfig {

    @Value("${spring.server.maxFileSize}")
    private String maxFileSize;

    @Value("${spring.server.maxRequestSize}")
    private String maxRequestSize;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(maxFileSize);
        factory.setMaxRequestSize(maxRequestSize);
        return factory.createMultipartConfig();
    }

}