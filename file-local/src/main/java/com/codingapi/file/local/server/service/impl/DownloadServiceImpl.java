package com.codingapi.file.local.server.service.impl;

import com.codingapi.file.local.server.config.LocalFileConfig;
import com.codingapi.file.local.server.service.DownloadService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * create by lorne on 2017/12/27
 */
@Service
public class DownloadServiceImpl implements DownloadService{

    @Autowired
    private LocalFileConfig localFileConfig;

    @Override
    public void download(HttpServletResponse response, String filePath) {
        try {
            File file = new File(localFileConfig.getFileServerPath()+"/"+filePath);
            if(file.exists()) {
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
                OutputStream outputStream = response.getOutputStream();
                response.setContentLength((int)file.length());
                IOUtils.copy(new FileInputStream(file), outputStream);
            }else{
                throw new RuntimeException("file  not exits.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
