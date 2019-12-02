package com.by.blcu.manager.controller;

import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.FastDFSClientWrapper;
import com.by.blcu.core.utils.UploadActionUtil;
import com.by.blcu.mall.model.File;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@CheckToken
@RestController
@RequestMapping("/managerFile")
@Api(tags = "Manager文件上传接口API", description = "包含接口：\n" +
        "1、文件上传-普通上传【upload】\n"+
        "2、文件上传-fdfs上传【fdfs_upload】\n")
public class ManagerFileController extends ManagerBase {
    @Autowired
    private FastDFSClientWrapper    dfsClient;

    /**
     * 普通方式上传文件
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/upload")
    public RetResult<File> upload(HttpServletRequest httpServletRequest) throws Exception {
        Map<String,String> map = UploadActionUtil.uploadFile(httpServletRequest);
        File file = new File();
        file.setFileName(map.get("fileName"));
        file.setFilePath(map.get("filePath"));
        file.setFileSize(map.get("fileSize"));
        file.setFileId(ApplicationUtils.getUUID());

        return RetResponse.makeOKRsp(file);
    }

    /**
     * fdfs方式上传文件
     * @param file
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/fdfs_upload")
    public RetResult<String> fdfsUpload(@RequestParam("file") MultipartFile file,
                                        RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            return RetResponse.makeErrRsp("Please select a file to upload");
        }
        String fileUrl="";
        File fileSave = new File();
        try {
            fileUrl = dfsClient.uploadFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileSave.setFilePath(fileUrl);
        fileSave.setFileName(file.getOriginalFilename());
        fileSave.setFileSize(String.valueOf(file.getSize()));
        fileSave.setFileId(ApplicationUtils.getUUID());

        return RetResponse.makeOKRsp(fileUrl);
    }
}
