package com.by.blcu.core.utils;

import com.by.blcu.core.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 系统工具类
 *
 * @author MrBird
 */
public class SystemUtils {

    private static Logger log = LoggerFactory.getLogger(SystemUtils.class);

    private static String audio = "cd;ogg;mp3;asf;wma;wav;mp3pro;real;ape;module;midi;vqf;flac;tak;tta;wv;mid;aac";
    private static String pic = "bmp;jpeg;gif;psd;png;tiff;tag;jpg";
    private static String doc = "exe;chm;pdf;psd;ai;txt;pdm;zip;rar;7z;doc;docx;ppt;vsdx;xls;xlsx;pptx";
    private static String video = "mpeg1;mpeg2;mpeg4;mpeg-1;mpg;avi;rm;rmvb;asf;wmv;mov;mp4;mov;mtv;3gp;amv;flv;mkv;f4v;iso";

    /**
     * 获取多媒体文件类型
     *
     * @return File_Type
     */
    public static String getFileType(String fileName){
        String fileType = "unknown";
        if(fileName.indexOf(".") > 0){
            fileName = fileName.toLowerCase();
            String[] typeArr = fileName.split("\\.");

            if(audio.contains(typeArr[1])){
                return "audio";
            }
            if(pic.contains(typeArr[1])){
                return "pic";
            }
            if(doc.contains(typeArr[1])){
                return "doc";
            }
            if(video.contains(typeArr[1])){
                return "video";
            }
        }
        return fileType;
    }

    public static void main(String[] args) {
        System.out.println(getFileType("file.mp4"));
        System.out.println(getFileType("file.doc"));
        System.out.println(getFileType("file.avi"));
        System.out.println(getFileType("file.mp3"));
        System.out.println(getFileType("file.jpg"));
    }

}
