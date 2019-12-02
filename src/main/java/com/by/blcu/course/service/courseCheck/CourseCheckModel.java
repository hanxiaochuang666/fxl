package com.by.blcu.course.service.courseCheck;

import lombok.Data;

@Data
public class CourseCheckModel {
    /**
     * 修改类型 1.修改目录 2.新增目录 3.新增资源4.切换资源，5.修改试卷，6.修改试题
     */
    private Integer changeType;

    private Integer courseId;

    private Integer catalogId;

    private Integer courseDetailId;

    private Integer contentId;
    //资源类型（1：作业/测试；2：视频；3：直播；4：讨论；5：问答；6文档；7文本；8资料）
    private Integer contentType;

    private String  content;

    //是否上架标志
    private Integer isUpper;    //0未上架，1上架

    private Integer syncFlag; //0需要同步，1不需要同步，3.等待异步结果
}
