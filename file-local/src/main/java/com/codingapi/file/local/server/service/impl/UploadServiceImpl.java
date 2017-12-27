package com.codingapi.file.local.server.service.impl;

import com.codingapi.file.local.server.config.LocalFileConfig;
import com.codingapi.file.local.server.model.FileServerModel;
import com.codingapi.file.local.server.service.FileValidateService;
import com.codingapi.file.local.server.service.UploadService;
import com.lorne.core.framework.exception.ParamException;
import com.lorne.core.framework.exception.ServiceException;
import com.lorne.core.framework.utils.encode.MD5Util;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * create by lorne on 2017/9/26
 */
@Service
public class UploadServiceImpl implements UploadService {



    @Autowired
    private FileValidateService fileValidateService;


    @Autowired
    private LocalFileConfig localFileConfig;


    @Override
    public FileServerModel uploadFile(MultipartFile file) throws ServiceException {

        if (file.getSize() <= 0) {
            throw new ParamException("file is null.");
        }

        fileValidateService.validateFile(file);

        try {
            String fileName = file.getOriginalFilename();

            String ext = FilenameUtils.getExtension(fileName);

            String baseName =  FilenameUtils.getBaseName(fileName);

            String serverPath = localFileConfig.getFileServerPath();

            String fileNamePath = MD5Util.md5(baseName.getBytes());

            String filePath = "/"+fileNamePath+"/"+baseName+"."+ext;

            File uploadFile = new File(serverPath+filePath);

            FileUtils.copyInputStreamToFile(file.getInputStream(),uploadFile);

            FileServerModel fileServerModel = new FileServerModel();
            fileServerModel.setPath(filePath);
            fileServerModel.setUrl(localFileConfig.getFileDownloadUrl()+filePath);

            return fileServerModel;

        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }



}
