package com.by.blcu.mall.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.Service;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.mall.model.File;
import com.by.blcu.mall.model.ResourceFile;
import com.by.blcu.mall.vo.VideoInfoVo;
import com.by.blcu.resource.dto.VideoInfo;
import com.by.blcu.resource.model.FileViewModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
* @Description: FileService接口
* @author 李程
* @date 2019/08/21 19:19
*/
public interface FileService extends Service<File> {

    <T> long selectCount(Map<String,Object> map) throws Exception;

    <T> List<ResourceFile> selectList(Map<String,Object> map) throws Exception;

    Integer insertResourceFile(File file, FileViewModel fileViewModel, HttpServletRequest request, CourseCheckModel courseCheckModel) throws Exception;

    File selectByFileId(String fileId);

    /**
     * 获取授权
     * @return
     */
    VideoInfoVo getAuthorization(String extendName, int userId, String videoName,String appkey,String appsecret)throws Exception;

    /**
     * 上传视频回调
     * @param videoInfoId
     * @param videoName
     */
    VideoInfoVo saveVideoPlayUrl(Integer videoInfoId, String videoName, int userId, String size, Integer duration)throws Exception;

}