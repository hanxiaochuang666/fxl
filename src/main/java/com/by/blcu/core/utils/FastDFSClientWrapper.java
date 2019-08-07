package com.by.blcu.core.utils;

/**
 * @author: Martin
 * @Date: 2018/9/28
 * @Description:
 * @Modify By:
 */

import com.by.blcu.core.configurer.FdfsConfig;
import com.by.blcu.core.constant.GlobalConstants;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadFileWriter;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 功能描述: 文件处理类
 *
 * @author
 * @version V1.0
 * @date 2018/10/12
 */
@Component
public class FastDFSClientWrapper {

    public final static Logger log = LoggerFactory.getLogger(FastDFSClientWrapper.class);
    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private FdfsConfig fdfsConfig;

    public String uploadFile(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadFile((InputStream) file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), null);
        log.info("storePath:" + storePath);
        return getResAccessUrl(storePath);
    }

    /**
     * 本地上传文件到fastdfs
     * @param file
     * @return
     * @throws IOException
     */
    public String localUploadFile(File file,String fileName)throws IOException{
        InputStream inputStream = new FileInputStream(file);
        StorePath storePath = storageClient.uploadFile( inputStream, file.length(), fileName, null);
        log.info("storePath:" + storePath);
        return getResAccessUrl(storePath);
    }

    /**
     * 封装文件完整URL地址
     *
     * @param storePath
     * @return
     */
    private String getResAccessUrl(StorePath storePath) {
        //GlobalConstants.HTTP_PRODOCOL +
        //String part =fdfsConfig.getTrackerList().substring(fdfsConfig.getTrackerList().lastIndexOf("/")+1);
        //String fileUrl = GlobalConstants.HTTP_FILEURL + ":" + part + "/" + storePath.getFullPath();
        String fileUrl = fdfsConfig.getHost() + ":" + fdfsConfig.getPart() + "/" + storePath.getFullPath();
        log.info("fileUrl:" + fileUrl);
        return fileUrl;
    }

    /**
     * 功能描述: 删除文件
     *
     * @param fileUrl
     * @return void
     * @author Martin
     * @date 2018/10/12
     * @version V1.0
     */
    public void deleteFile(String fileUrl) {

        log.info("删除的文件的url:" + fileUrl);
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        try {
            StorePath storePath = StorePath.parseFromUrl(fileUrl);
            log.info("groupName:"+storePath.getGroup()+"------"+"文件路径path："+storePath.getPath());
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (FdfsUnsupportStorePathException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * 功能描述: 下载文件
     *
     * @param fileUrl
     * @return java.io.InputStream
     * @author Martin
     * @date 2018/10/12
     * @version V1.0
     */
    public InputStream downFile(String fileUrl) {
        try {
            StorePath storePath = StorePath.parseFromUrl(fileUrl);
            byte[] fileByte = storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
            InputStream ins = new ByteArrayInputStream(fileByte);
            return ins;
        } catch (Exception e) {
            log.error("Non IO Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    /**
     * 功能描述: 浏览器下载文件
     *
     * @param fileUrl
     * @return java.io.InputStream
     * @author Martin
     * @date 2018/10/12
     * @version V1.0
     */
    public void webDownFile(HttpServletResponse response,String fileUrl,String fileName){
        response.setContentType("application/force-download");
        //支持中文名称
        try {
            fileName = URLEncoder.encode(fileName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int i = fileUrl.lastIndexOf(".");
        String substring = fileUrl.substring(i);
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName+substring);
        try {
            OutputStream outputStream = response.getOutputStream();
            StorePath storePath = StorePath.parseFromUrl(fileUrl);
            byte[] fileByte = storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
            IOUtils.write(fileByte,outputStream);
            outputStream.close();
        } catch (Exception e) {
            log.error("Non IO Exception: Get File from Fast DFS failed", e);
        }

    }
}
