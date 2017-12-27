package com.codingapi.file.local.server.service;

import com.lorne.core.framework.exception.ServiceException;

/**
 * create by lorne on 2017/9/26
 */
public interface RemoveService {


    boolean removeFile(String fileName, int flag) throws ServiceException;


}
