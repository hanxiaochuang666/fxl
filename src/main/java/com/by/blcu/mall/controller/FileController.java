package com.by.blcu.mall.controller;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.FastDFSClientWrapper;
import com.by.blcu.core.utils.UploadActionUtil;
import com.by.blcu.mall.model.File;
import com.by.blcu.mall.service.FileService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @Description: FileController类
* @author 李程
* @date 2019/07/30 13:20
*/
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FastDFSClientWrapper dfsClient;

    @Resource
    private FileService fileService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(File file) throws Exception{
      file.setFileId(ApplicationUtils.getUUID());
       Integer state = fileService.insert(file);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = fileService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(File file) throws Exception {
        Integer state = fileService.update(file);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectById")
    public RetResult<File> selectById(@RequestParam String id) throws Exception {
        File file = fileService.selectById(id);
        return RetResponse.makeOKRsp(file);
    }

    @PostMapping("/upload")
    public RetResult<File> upload(HttpServletRequest httpServletRequest) throws Exception {
        Map<String,String> map = UploadActionUtil.uploadFile(httpServletRequest);
        File file = new File();
        file.setFileName(map.get("fileName"));
        file.setFilePath(map.get("filePath"));
        file.setFileSize(map.get("fileSize"));
        file.setFileId(ApplicationUtils.getUUID());
        fileService.insert(file);
        return RetResponse.makeOKRsp(file);
    }

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
        Integer state = fileService.insert(fileSave);
        if (state != 1){
            return RetResponse.makeErrRsp("请重新上传！");
        }
        return RetResponse.makeOKRsp(fileSave.getFileId());
    }

    /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<File>>
     */
    @PostMapping("/list")
    public RetResult<PageInfo<File>> list(@RequestParam(defaultValue = "0") Integer page,
               @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<File> list = fileService.selectAll();
        PageInfo<File> pageInfo = new PageInfo<File>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }
}