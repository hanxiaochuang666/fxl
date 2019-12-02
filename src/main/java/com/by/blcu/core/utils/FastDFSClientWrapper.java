package com.by.blcu.core.utils;

/**
 * @author: Martin
 * @Date: 2018/9/28
 * @Description:
 * @Modify By:
 */

import com.by.blcu.core.configurer.FdfsConfig;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 功能描述: 文件处理类
 *
 * @author
 * @version V1.0
 * @date 2018/10/12
 */
@Component
@Data
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
        String fileUrl = fdfsConfig.getHead() + fdfsConfig.getHost() + ":" + fdfsConfig.getPart() + "/" + storePath.getFullPath();
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

    public byte[] getByteData(String fileUrl){
        try {
            StorePath storePath = StorePath.parseFromUrl(fileUrl);
            byte[] fileByte = storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
            return fileByte;
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
    public void webDownFile(HttpServletRequest httpServletRequest, HttpServletResponse response, String fileUrl, String fileName){
        response.setContentType("application/force-download");
        //支持中文名称
        try {
            fileName = URLEncoder.encode(fileName,"UTF-8");
            if (httpServletRequest.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //兼容下载文件名称
        if(fileName.indexOf(".") < 0){
            fileName = fileName + ".docx";
        }

        response.setHeader("Access-Control-Expose-Headers","fileName");
        response.setHeader("fileName", fileName);
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
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


    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[10240];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    public static InputStream getImageFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
            //byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
            return inStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
