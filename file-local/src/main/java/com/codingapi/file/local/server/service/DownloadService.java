package com.codingapi.file.local.server.service;

import javax.servlet.http.HttpServletResponse; /**
 * create by lorne on 2017/12/27
 */
public interface DownloadService {

    void download(HttpServletResponse response, String filePath);
}
