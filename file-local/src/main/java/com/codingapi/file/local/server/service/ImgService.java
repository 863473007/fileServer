package com.codingapi.file.local.server.service;

import com.codingapi.file.local.server.model.FileServerModel;
import com.lorne.core.framework.exception.ServiceException;
import org.springframework.web.multipart.MultipartFile;
/**
 * create by lorne on 2017/12/27
 */
public interface ImgService {

    FileServerModel uploadImage(MultipartFile file, String cutSize)throws ServiceException;

    boolean cutImage(String filePath, String cutSize) throws ServiceException;
}
