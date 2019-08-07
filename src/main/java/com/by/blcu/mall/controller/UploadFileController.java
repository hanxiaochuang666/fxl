package com.by.blcu.mall.controller;

import com.by.blcu.core.utils.FastDFSClientWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

/**
 * @author: Martin
 * @Date: 2018/9/28
 * @Description:
 * @Modify By:
 */
@Controller
@RequestMapping("/upload")
public class UploadFileController {
    @Autowired
    private FastDFSClientWrapper dfsClient;

    @GetMapping("/")
    public String index() {
        return "upload/upload";
    }

    @PostMapping("/fdfs_upload")
    public String fdfsUpload(@RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/upload/uploadStatus";
        }

        try {
            String fileUrl = dfsClient.uploadFile(file);
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + fileUrl + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/upload/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "upload/uploadStatus";
    }

    @RequestMapping("/deleteFile")
    @ResponseBody
    public String deleteFile(@RequestParam(value = "fileUrl") String fileUrl) {
        dfsClient.deleteFile(fileUrl);
        return "Success";
    }
}
