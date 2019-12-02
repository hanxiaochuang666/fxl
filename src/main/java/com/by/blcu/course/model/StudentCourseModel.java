package com.by.blcu.course.model;

import com.by.blcu.mall.model.CommodityLecturer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(value = "StudentCourseModel",description = "学生课程实体")
@Data
public class StudentCourseModel implements Serializable {
    private static final long serialVersionUID = -4534506709446363299L;

    @ApiModelProperty(value = "课程封面")
    private String picturePath;

    @ApiModelProperty(value = "商品id")
    private String commodityId;

    @ApiModelProperty(value = "课程id")
    private String courseId;

    @ApiModelProperty(value = "商品名称")
    private String ccName;

    @ApiModelProperty(value = "授课类型(1：直播；2：面授；3：录播；4：录播+直播)")
    private Integer lessonType;

    @ApiModelProperty(value = "有效期")
    private String validity;

    @ApiModelProperty(value = "创建日期")
    private Date createTime;

    @ApiModelProperty(value = "已学课时")
    private String havaStudyClass;

    @ApiModelProperty(value = "总课时")
    private String totalClass;

    @ApiModelProperty(value = "学习进度")
    private String studySchedule;

    @ApiModelProperty(value = "主讲老师")
    private List<CommodityLecturer> commodityLecturerList;

    @ApiModelProperty(value = "是否是套餐 0:不是套餐  1：是套餐")
    private Integer isPackage;

    @ApiModelProperty(value = "套餐中的子商品")
    private List<StudentCourseModel> childList;

}
