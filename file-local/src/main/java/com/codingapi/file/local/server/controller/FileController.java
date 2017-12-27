package com.codingapi.file.local.server.controller;

import com.codingapi.file.local.server.model.FileServerModel;
import com.codingapi.file.local.server.service.UploadService;
import com.lorne.core.framework.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * create by lorne on 2017/9/26
 */
@RestController
@RequestMapping("/file")
@Api(value = "文件服务接口")
public class FileController {



    @Autowired
    private UploadService uploadService;


    @ApiOperation(value="上传文件", notes="上传文件")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public FileServerModel uploadFile(

            @ApiParam(value = "文件流,name=file")
            @RequestParam("file") MultipartFile file) throws ServiceException {
        return uploadService.uploadFile(file);
    }



    @ApiOperation(value="删除文件", notes="删除文件")
    @RequestMapping(value = "/removeFile", method = RequestMethod.POST)
    public boolean removeFile(
            @ApiParam(value = "文件服务器存放路径")
            @RequestParam("filePath") String filePath,
            @ApiParam(value = "删除标示,0:本文件,1:整个文件目录")
            @RequestParam("flag") int flag ) throws ServiceException {
        return uploadService.removeFile(filePath,flag);
    }




}
