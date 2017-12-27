package com.codingapi.file.local.server.controller;

import com.codingapi.file.local.server.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * create by lorne on 2017/12/27
 */
@Controller
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    private DownloadService downloadService;


    @RequestMapping("/file")
    public void file(HttpServletResponse response, @RequestParam("filePath") String filePath){
        downloadService.download(response,filePath);
    }

}
