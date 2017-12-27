package com.codingapi.file.local.server;

import com.codingapi.file.local.server.config.LocalFileConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * create by lorne on 2017/12/26
 */
@Configuration
public class LocalFileConfigConfiguration {


    @Value("${file.config.server.path}")
    private String fileServerPath;

    @Value("${file.config.download.url}")
    private String fileDownloadUrl;

    @Value("${file.config.default.cutSize}")
    private String fileDefaultCutSize;

    @Value("${file.config.validate.type}")
    private String fileValidateType;

    @Bean
    public LocalFileConfig localFileConfig(){
        LocalFileConfig localFileConfig = new LocalFileConfig();
        localFileConfig.setFileServerPath(fileServerPath);
        localFileConfig.setFileDownloadUrl(fileDownloadUrl);
        localFileConfig.setFileValidateType(fileValidateType);
        localFileConfig.setDefaultCutSize(fileDefaultCutSize);
        return localFileConfig;
    }
}
