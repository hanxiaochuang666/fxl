package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

/**
 * 教师工作经历表
 */
@Data
@Table(name = "manager_teacher_exp")
@ApiModel(description= "用户")
public class ManagerTeacherExp {
    /**
     *教师工作经历表d
     */
    @Id
    @Column(name = "teacher_exp_id")
    @ApiModelProperty(value = "教师工作经历表d")
    private String teacherExpId;

    /**
     *教师入驻申请表Id
     */
    @Column(name = "teacher_apply_id")
    @ApiModelProperty(value = "教师入驻申请表Id")
    private String teacherApplyId;

    /**
     *类别（1 教学工作经历；2 教学工作成果）
     */
    @ApiModelProperty(value = "类别（1 教学工作经历；2 教学工作成果）")
    private Integer category;

    /**
     *开始时间
     */
    @Column(name = "start_time")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     *结束时间
     */
    @Column(name = "end_time")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     *经历描述
     */
    @ApiModelProperty(value = "经历描述")
    private String description;

    /**
     *排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     *添加时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value = "添加时间")
    private Date createTime;

    /**
     *添加人
     */
    @Column(name = "create_by")
    @ApiModelProperty(value = "添加人")
    private String createBy;

    /**
     *修改时间
     */
    @Column(name = "modify_time")
    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    /**
     *修改人
     */
    @Column(name = "modify_by")
    @ApiModelProperty(value = "修改人")
    private String modifyBy;

}