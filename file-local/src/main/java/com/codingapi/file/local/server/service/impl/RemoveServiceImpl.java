package com.codingapi.file.local.server.service.impl;

import com.codingapi.file.local.server.config.LocalFileConfig;
import com.codingapi.file.local.server.service.RemoveService;
import com.codingapi.file.local.server.utils.FileServerUtils;
import com.lorne.core.framework.exception.ParamException;
import com.lorne.core.framework.exception.ServiceException;
import com.lorne.core.framework.utils.encode.MD5Util;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * create by lorne on 2017/12/27
 */
@Service
public class RemoveServiceImpl implements RemoveService{



    @Autowired
    private LocalFileConfig localFileConfig;

    @Override
    public boolean removeFile(String filePath, int flag) throws ServiceException {

        if(StringUtils.isEmpty(filePath)){
            throw new ParamException("filePath is null");
        }

        String serverPath = localFileConfig.getFileServerPath();

        File file = new File(serverPath+"/"+filePath);

        if(!file.exists()){
            throw new ParamException("file not exists");
        }

        boolean isCutFile = FileServerUtils.isCutImgName(file.getName());

        String baseName =  FilenameUtils.getBaseName(file.getName());

        if(isCutFile){
            baseName = baseName.substring(0,baseName.lastIndexOf("_"));
        }

        if(!file.getParent().endsWith(MD5Util.md5(baseName.getBytes()))){
            throw new ParamException("file md5 val error");
        }

        if(flag==1){
            try {
                FileUtils.deleteDirectory(file.getParentFile());
            } catch (IOException e) {
                throw new ParamException("delete fileName group error.");
            }
        }else {
            file.delete();

            int listLength = file.getParentFile().list().length;
            if(listLength==0){
                try {
                    FileUtils.deleteDirectory(file.getParentFile());
                } catch (IOException e) {
                    throw new ParamException("delete fileName group error.");
                }
            }
        }

        return true;
    }

}
