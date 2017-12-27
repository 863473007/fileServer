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

    private String defaultCutSize;

    private String fileValidateType;

    public String getFileValidateType() {
        return fileValidateType;
    }

    public void setFileValidateType(String fileValidateType) {
        this.fileValidateType = fileValidateType;
    }

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

    public String getDefaultCutSize() {
        return defaultCutSize;
    }

    public void setDefaultCutSize(String defaultCutSize) {
        this.defaultCutSize = defaultCutSize;
    }
}
