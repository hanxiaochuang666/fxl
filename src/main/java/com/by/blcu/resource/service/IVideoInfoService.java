package com.by.blcu.resource.service;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.resource.dto.VideoInfo;
import com.by.blcu.resource.model.VideoInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface IVideoInfoService extends IBaseService {
    /**
     * 删除视频
     * @param videoInfoId
     */
    void deleteVideoById(int videoInfoId);

    /**
     * 上传视频回调
     * @param videoInfoId
     * @param videoName
     */
    Map<String,String> saveVideoPlayUrl(Integer videoInfoId, String videoName, int userId, Integer bak2, Integer duration)throws Exception;

    /**
     * 获取授权
     * @return
     */
    RetResult<VideoInfo> getAuthorization(String extendName,int userId,String videoName)throws Exception;

    /**
     *
     */
    RetResult editVideoInfo(VideoInfo videoInfo,CourseCheckModel courseCheckModel)throws Exception;

    /**
     * 课程目录下插入视频资源
     * @param videoInfoVO
     * @param request
     * @return
     */
    Integer addVideoInfo(VideoInfoVO videoInfoVO, HttpServletRequest request, CourseCheckModel courseCheckModel) throws Exception;

    /**
     * 保存Acca视频
     * @param obj
     * @return
     */
    RetResult saveAccaVideo(JSONObject obj,CourseCheckModel courseCheckModel)throws Exception;
}