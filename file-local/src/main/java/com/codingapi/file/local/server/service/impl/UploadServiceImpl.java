package com.codingapi.file.local.server.service.impl;

import com.codingapi.file.local.server.config.LocalFileConfig;
import com.codingapi.file.local.server.model.FileServerModel;
import com.codingapi.file.local.server.model.ImageWH;
import com.codingapi.file.local.server.service.FileValidateService;
import com.codingapi.file.local.server.service.UploadService;
import com.codingapi.file.local.server.utils.FileServerUtils;
import com.lorne.core.framework.exception.ParamException;
import com.lorne.core.framework.exception.ServiceException;
import com.lorne.core.framework.utils.KidUtils;
import com.lorne.core.framework.utils.encode.MD5Util;
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

}
