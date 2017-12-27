package com.codingapi.file.local.server.controller;

import com.codingapi.file.local.server.model.FileServerModel;
import com.codingapi.file.local.server.service.UploadService;
import com.lorne.core.framework.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * create by lorne on 2017/9/26
 */
@RestController
@RequestMapping("/upload")
@Api(value = "文件服务接口")
public class UploadController {



    @Autowired
    private UploadService uploadService;


    @ApiOperation(value="上传文件", notes="上传文件")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public FileServerModel uploadFile(

            @ApiParam(value = "文件流,name=file")
            @RequestParam("file") MultipartFile file) throws ServiceException {
        return uploadService.uploadFile(file);
    }






}
