package com.codingapi.file.local.server.service.impl;

import com.codingapi.file.local.server.config.LocalFileConfig;
import com.codingapi.file.local.server.model.FileServerModel;
import com.codingapi.file.local.server.model.ImageWH;
import com.codingapi.file.local.server.service.FileValidateService;
import com.codingapi.file.local.server.service.ImgService;
import com.codingapi.file.local.server.service.UploadService;
import com.codingapi.file.local.server.utils.FileServerUtils;
import com.lorne.core.framework.exception.ParamException;
import com.lorne.core.framework.exception.ServiceException;
import com.lorne.core.framework.utils.KidUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * create by lorne on 2017/12/27
 */
@Service
public class ImgServiceImpl implements ImgService {



    @Autowired
    private UploadService uploadService;


    @Autowired
    private LocalFileConfig localFileConfig;

    @Override
    public FileServerModel uploadImage(MultipartFile file, String cutSize) throws ServiceException {

        if (file.getSize() <= 0) {
            throw new ParamException("file is null.");
        }

        if(cutSize==null){
            cutSize = localFileConfig.getDefaultCutSize();
        }

        FileServerModel fileServerModel =  uploadService.uploadFile(file);

        List<ImageWH> whs = FileServerUtils.loadCutSize(cutSize);

        try {

            File uploadFile = new File(localFileConfig.getFileServerPath()+"/"+fileServerModel.getPath());

            uploadCutImages(whs,uploadFile);

            return fileServerModel;
        } catch (Exception e) {
            throw new ServiceException(e);
        }

    }



    private void uploadCutImages(List<ImageWH> whs,File sourceFile) throws ServiceException {
        //上传裁剪的图片
        try {
            for (ImageWH wh : whs) {

                String fileName = sourceFile.getName();

                String baseName = FilenameUtils.getBaseName(fileName);

                String ext = FilenameUtils.getExtension(fileName);

                String newFileName = baseName+"_"+wh.getW()+"x"+wh.getH()+"."+ext;

                File imgFile = new File(sourceFile.getParent()+"/"+newFileName);

                Thumbnails.of(sourceFile).size(wh.getW(), wh.getH()).toFile(imgFile);
            }
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean cutImage(String filePath, String cutSize) throws ServiceException {

        if(StringUtils.isEmpty(filePath)){
            throw new ParamException("filePath is null");
        }

        List<ImageWH> whs = FileServerUtils.loadCutSize(cutSize);

        String serverPath = localFileConfig.getFileServerPath();

        File uploadFile = new File(serverPath+"/"+filePath);

        if(!uploadFile.exists()){
            throw new ParamException("file not exist");
        }

        uploadCutImages(whs,uploadFile);

        return true;
    }

}
