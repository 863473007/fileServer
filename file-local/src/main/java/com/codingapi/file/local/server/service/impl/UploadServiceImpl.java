package com.codingapi.file.local.server.service.impl;

import com.codingapi.file.local.server.config.LocalFileConfig;
import com.codingapi.file.local.server.model.ImageWH;
import com.codingapi.file.local.server.service.FileValidateService;
import com.codingapi.file.local.server.service.UploadService;
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

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public String uploadFile(String groupName, MultipartFile file) throws ServiceException {

        if (file.getSize() <= 0) {
            throw new ParamException("file is null.");
        }

        fileValidateService.validateFile(file);

        try {
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());

            String serverPath = localFileConfig.getFileServerPath();

            String fileName = file.getOriginalFilename();

            File uploadFile = new File(serverPath+"/"+fileName+"."+ext);

            FileUtils.copyInputStreamToFile(file.getInputStream(),uploadFile);

            return uploadFile.getPath();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }


    @Override
    public boolean removeFile(String fileName) throws ServiceException {

        if(StringUtils.isEmpty(fileName)){
            throw new ParamException("fileName is null");
        }

        String serverPath = localFileConfig.getFileServerPath();

        File file = new File(serverPath+"/"+fileName);

        if(file.exists()){
            file.delete();
            return true;
        }

        return false;
    }



    @Override
    public String uploadImage(String groupName, String cutSize, MultipartFile file) throws ServiceException {

        if (file.getSize() <= 0) {
            throw new ParamException("file is null.");
        }

        if(StringUtils.isEmpty(groupName)){
            throw new ParamException("groupName is null");
        }

        List<ImageWH> whs =loadCutSize(cutSize);

        fileValidateService.validateFile(file);

        try {

            String ext = FilenameUtils.getExtension(file.getOriginalFilename());

            String serverPath = localFileConfig.getFileServerPath();

            String fileName = file.getOriginalFilename();

            File uploadFile = new File(serverPath+"/"+fileName+"."+ext);

            FileUtils.copyInputStreamToFile(file.getInputStream(),uploadFile);


            File sourceFile = new File(KidUtils.getUUID());
            FileUtils.copyInputStreamToFile(file.getInputStream(),sourceFile);

            uploadCutImages(whs,sourceFile,uploadFile,ext);

            sourceFile.delete();

            return uploadFile.getPath();
        } catch (Exception e) {
            throw new ServiceException(e);
        }

    }


    private List<ImageWH> loadCutSize(String cutSize) throws ParamException{
        List<ImageWH> whs =null;
        if(StringUtils.isNotEmpty(cutSize)) {
            try {
                List<String> sizes = Arrays.asList(cutSize.split(","));
                whs = new ArrayList<>();
                for (String size : sizes) {
                    String vals[] = size.split("x");
                    int w = Integer.parseInt(vals[0]);
                    int h = Integer.parseInt(vals[1]);

                    whs.add(new ImageWH(w, h));
                }

            }catch (Exception e){
                throw new ParamException("cutSize is error");
            }
        }

        return whs;
    }


    private void uploadCutImages(List<ImageWH> whs,File sourceFile,File uploadFile,String ext) throws ServiceException {
        //上传裁剪的图片
        try {
            for (ImageWH wh : whs) {

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Thumbnails.of(sourceFile).size(wh.getW(), wh.getH()).toOutputStream(out);
                InputStream newInput = new ByteArrayInputStream(out.toByteArray());


                String resFilePath = String.format("%s/%s_%dx%d.%s",uploadFile.getName(),sourceFile.getName(), wh.getW(), wh.getH(),ext);

                FileUtils.copyInputStreamToFile(newInput,new File(resFilePath));

            }
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean cutImage(String fileName, String cutSize) throws ServiceException {

        if(StringUtils.isEmpty(fileName)){
            throw new ParamException("fileName is null");
        }

        List<ImageWH> whs =loadCutSize(cutSize);

        String serverPath = localFileConfig.getFileServerPath();

        File cutFile = new File(serverPath+"/"+fileName);

        String ext = FilenameUtils.getExtension(cutFile.getName());

        File file = new File(KidUtils.getUUID());
        try {
            FileUtils.copyFile(cutFile,file);
            uploadCutImages(whs,file,cutFile,ext);
        } catch (IOException e) {
            throw new ServiceException(e);
        }finally {
            file.delete();
        }

        return true;
    }


    @Override
    public boolean removeFiles(String fileName) throws ServiceException {

        if(StringUtils.isEmpty(fileName)){
            throw new ParamException("fileName is null");
        }

        String serverPath = localFileConfig.getFileServerPath();

        File file = new File(serverPath+"/"+fileName);




        return true;
    }
}
