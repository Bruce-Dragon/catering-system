package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OssConfiguration {
    @Bean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
        log.info("创建阿里云工具类对象");
       return new AliOssUtil(aliOssProperties.getEndpoint(),aliOssProperties.getAccessKeyId()
                ,aliOssProperties.getAccessKeySecret(),aliOssProperties.getBucketName());
    }
}
