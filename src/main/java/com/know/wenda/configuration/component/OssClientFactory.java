package com.know.wenda.configuration.component;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * OssClient
 *
 * @author hlb
 */
@Component
public class OssClientFactory {

    /**
     * OSS 连接客户端
     */
    private volatile  OSSClient client;
    /**
     * OSS端点地址
     */
    @Value("${oss.endpoint}")
    private  String endpoint;

    /**
     * 登录key
     */
    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    /**
     * 登录凭证
     */
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    /**
     * 双重检索机制实现单例模式
     * @return
     */
    public OSSClient getClient(){
        if(ObjectUtils.isEmpty(client)){
            synchronized (OSSClient.class){
                if (ObjectUtils.isEmpty(client)){
                   client = new OSSClient(endpoint,accessKeyId,accessKeySecret);
                }
            }
        }
        return client;
    }

}