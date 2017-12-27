package com.codingapi.file.local.server.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * create by lorne on 2017/12/27
 */
@ApiModel(value = "文件返回数据")
public class FileServerModel {

    @ApiModelProperty(value = "完全url路径")
    private String url;

    @ApiModelProperty(value = "文件服务器存放路径")
    private String path;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
