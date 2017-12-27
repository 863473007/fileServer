package com.codingapi.file.local.server.service;

import com.codingapi.file.local.server.model.FileServerModel;
import com.lorne.core.framework.exception.ServiceException;
import org.springframework.web.multipart.MultipartFile;

/**
 * create by lorne on 2017/9/26
 */
public interface UploadService {

    FileServerModel uploadFile(MultipartFile file) throws ServiceException;

    boolean removeFile(String fileName, int flag) throws ServiceException;

    String uploadImage(String groupName, String cutSize, MultipartFile file) throws ServiceException;

    boolean cutImage(String fileName, String cutSize)throws ServiceException;

}
