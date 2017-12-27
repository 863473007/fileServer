package com.codingapi.file.local.server.controller;

import com.codingapi.file.local.server.service.RemoveService;
import com.lorne.core.framework.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by lorne on 2017/9/26
 */
@RestController
@RequestMapping("/remove")
@Api(value = "文件服务接口")
public class RemoveController {


    @Autowired
    private RemoveService removeService;


    @ApiOperation(value="删除文件", notes="删除文件")
    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public boolean removeFile(
            @ApiParam(value = "文件服务器存放路径")
            @RequestParam("filePath") String filePath,
            @ApiParam(value = "删除标示,0:本文件,1:整个文件目录")
            @RequestParam("flag") int flag ) throws ServiceException {
        return removeService.removeFile(filePath,flag);
    }




}
