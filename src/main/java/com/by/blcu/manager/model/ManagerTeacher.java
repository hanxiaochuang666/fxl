package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

/**
 * 名师团队表
 */
@Data
@Table(name = "manager_teacher")
@ApiModel(description= "名师团队")
public class ManagerTeacher {
    /**
     *名师团队表Id
     */
    @Id
    @Column(name = "teacher_id")
    @ApiModelProperty(value = "名师团队表Id")
    private String teacherId;

    @Column(name = "cc_id")
    @ApiModelProperty(value = "课程分类")
    private String ccId;

    /**
     *教师姓名
     */
    @Column(name = "teacher_name")
    @ApiModelProperty(value = "教师姓名")
    private String teacherName;

    /**
     *教师资历
     */
    @Column(name = "teacher_zili")
    @ApiModelProperty(value = "教师资历")
    private String teacherZili;

    /**
     *讲授课程
     */
    @Column(name = "teach_course")
    @ApiModelProperty(value = "讲授课程")
    private String teachCourse;

    /**
     *所属组织编码
     */
    @Column(name = "org_code")
    @ApiModelProperty(value = "所属组织编码")
    private String orgCode;

    /**
     *教师简介
     */
    @Column(name = "teacher_jianjie")
    @ApiModelProperty(value = "教师简介")
    private String teacherJianjie;

    /**
     *教师头像
     */
    @Column(name = "teacher_header")
    @ApiModelProperty(value = "教师头像")
    private String teacherHeader;

    /**
     *关联课程
     */
    @Column(name = "teach_course_relation")
    @ApiModelProperty(value = "关联课程")
    private String teachCourseRelation;

    /**
     *自定义属性(JSON)
     */
    @ApiModelProperty(value = "自定义属性(JSON)")
    private String attribute;

    /**
     *用户状态（1. 正常 ，2 停用）
     */
    @ApiModelProperty(value = "用户状态（1. 正常 ，2 停用）")
    private Integer status;

    /**
     *排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     *是否删除
     */
    @Column(name = "is_deleted")
    @ApiModelProperty(value = "是否删除")
    private Boolean isDeleted;

    /**
     *类型（1 普通；2 推荐；）
     */
    @ApiModelProperty(value = "类型（1 普通；2 推荐；）")
    private Integer type;

    /**
     *类型中的排序
     */
    @Column(name = "type_sort")
    @ApiModelProperty(value = "类型中的排序")
    private Integer typeSort;

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