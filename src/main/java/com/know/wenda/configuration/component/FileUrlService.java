package com.know.wenda.configuration.component;

import com.aliyun.oss.OSSClient;
import com.know.wenda.configuration.component.OssClientFactory;
import com.know.wenda.constant.AliOssConstant;
import com.know.wenda.constant.StringConstant;
import com.know.wenda.constant.TimeConstant;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * OssUtil
 *
 * @author hlb
 */
@Component
public class FileUrlService {

    @Resource
    private   OssClientFactory ossClientFactory;

    /**
     * 获取上传到阿里云的图片url
     * @param file
     * @return
     * @throws IOException
     */
    public  String getFileUrl(MultipartFile file) throws IOException {
        // 获取文件后缀名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        // 创建OSSClient实例
        OSSClient ossClient = ossClientFactory.getClient();
        // 文件名格式
        SimpleDateFormat sdf = new SimpleDateFormat(StringConstant.TimeFormat.TIME_FORMAT);
        // 该桶中的文件key 20180322010634.jpg
        String dateString = sdf.format(new Date()) + "."+suffix;
        // 上传文件
        ossClient.putObject(AliOssConstant.BUCKET_NAMW, dateString, new ByteArrayInputStream(file.getBytes()));
        // 设置URL过期时间为100年，默认这里是int型，转换为long型即可
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + TimeConstant.AlyImageExpire.ALIYUN_IMAGE);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(AliOssConstant.BUCKET_NAMW, dateString, expiration);
        return url.toString();
    }

}