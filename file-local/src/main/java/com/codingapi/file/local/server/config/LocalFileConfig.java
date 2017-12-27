package com.codingapi.file.local.server.config;

/**
 * create by lorne on 2017/12/26
 */
public class LocalFileConfig {

    /**
     * 文件存储地址
     */
    private String fileServerPath;


    private String fileDownloadUrl;

    public String getFileServerPath() {
        return fileServerPath;
    }

    public void setFileServerPath(String fileServerPath) {
        this.fileServerPath = fileServerPath;
    }


    public String getFileDownloadUrl() {
        return fileDownloadUrl;
    }

    public void setFileDownloadUrl(String fileDownloadUrl) {
        this.fileDownloadUrl = fileDownloadUrl;
    }
}
