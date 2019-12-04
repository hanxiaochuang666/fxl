package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import javax.persistence.*;

/**
 * 名师团队表
 */
@Table(name = "manager_teacher")
@ApiModel(description= "用户")
public class ManagerTeacher {
    /**
     *名师团队表Id
     */
    @Id
    @Column(name = "teacher_id")
    @ApiModelProperty(value = "名师团队表Id")
    private String teacherId;

    /**
     *课程分类
     */
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

    /**
     * 获取名师团队表Id
     *
     * @return teacher_id - 名师团队表Id
     */
    public String getTeacherId() {
        return teacherId;
    }

    /**
     * 设置名师团队表Id
     *
     * @param teacherId 名师团队表Id
     */
    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId == null ? null : teacherId.trim();
    }

    /**
     * 获取课程分类
     *
     * @return cc_id - 课程分类
     */
    public String getCcId() {
        return ccId;
    }

    /**
     * 设置课程分类
     *
     * @param ccId 课程分类
     */
    public void setCcId(String ccId) {
        this.ccId = ccId == null ? null : ccId.trim();
    }

    /**
     * 获取教师姓名
     *
     * @return teacher_name - 教师姓名
     */
    public String getTeacherName() {
        return teacherName;
    }

    /**
     * 设置教师姓名
     *
     * @param teacherName 教师姓名
     */
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName == null ? null : teacherName.trim();
    }

    /**
     * 获取教师资历
     *
     * @return teacher_zili - 教师资历
     */
    public String getTeacherZili() {
        return teacherZili;
    }

    /**
     * 设置教师资历
     *
     * @param teacherZili 教师资历
     */
    public void setTeacherZili(String teacherZili) {
        this.teacherZili = teacherZili == null ? null : teacherZili.trim();
    }

    /**
     * 获取讲授课程
     *
     * @return teach_course - 讲授课程
     */
    public String getTeachCourse() {
        return teachCourse;
    }

    /**
     * 设置讲授课程
     *
     * @param teachCourse 讲授课程
     */
    public void setTeachCourse(String teachCourse) {
        this.teachCourse = teachCourse == null ? null : teachCourse.trim();
    }

    /**
     * 获取所属组织编码
     *
     * @return org_code - 所属组织编码
     */
    public String getOrgCode() {
        return orgCode;
    }

    /**
     * 设置所属组织编码
     *
     * @param orgCode 所属组织编码
     */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    /**
     * 获取教师简介
     *
     * @return teacher_jianjie - 教师简介
     */
    public String getTeacherJianjie() {
        return teacherJianjie;
    }

    /**
     * 设置教师简介
     *
     * @param teacherJianjie 教师简介
     */
    public void setTeacherJianjie(String teacherJianjie) {
        this.teacherJianjie = teacherJianjie == null ? null : teacherJianjie.trim();
    }

    /**
     * 获取教师头像
     *
     * @return teacher_header - 教师头像
     */
    public String getTeacherHeader() {
        return teacherHeader;
    }

    /**
     * 设置教师头像
     *
     * @param teacherHeader 教师头像
     */
    public void setTeacherHeader(String teacherHeader) {
        this.teacherHeader = teacherHeader == null ? null : teacherHeader.trim();
    }

    /**
     * 获取关联课程
     *
     * @return teach_course_relation - 关联课程
     */
    public String getTeachCourseRelation() {
        return teachCourseRelation;
    }

    /**
     * 设置关联课程
     *
     * @param teachCourseRelation 关联课程
     */
    public void setTeachCourseRelation(String teachCourseRelation) {
        this.teachCourseRelation = teachCourseRelation == null ? null : teachCourseRelation.trim();
    }

    /**
     * 获取自定义属性(JSON)
     *
     * @return attribute - 自定义属性(JSON)
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * 设置自定义属性(JSON)
     *
     * @param attribute 自定义属性(JSON)
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute == null ? null : attribute.trim();
    }

    /**
     * 获取用户状态（1. 正常 ，2 停用）
     *
     * @return status - 用户状态（1. 正常 ，2 停用）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置用户状态（1. 正常 ，2 停用）
     *
     * @param status 用户状态（1. 正常 ，2 停用）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取是否删除
     *
     * @return is_deleted - 是否删除
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否删除
     *
     * @param isDeleted 是否删除
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取类型（1 普通；2 推荐；）
     *
     * @return type - 类型（1 普通；2 推荐；）
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型（1 普通；2 推荐；）
     *
     * @param type 类型（1 普通；2 推荐；）
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取类型中的排序
     *
     * @return type_sort - 类型中的排序
     */
    public Integer getTypeSort() {
        return typeSort;
    }

    /**
     * 设置类型中的排序
     *
     * @param typeSort 类型中的排序
     */
    public void setTypeSort(Integer typeSort) {
        this.typeSort = typeSort;
    }

    /**
     * 获取添加时间
     *
     * @return create_time - 添加时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置添加时间
     *
     * @param createTime 添加时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取添加人
     *
     * @return create_by - 添加人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置添加人
     *
     * @param createBy 添加人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取修改人
     *
     * @return modify_by - 修改人
     */
    public String getModifyBy() {
        return modifyBy;
    }

    /**
     * 设置修改人
     *
     * @param modifyBy 修改人
     */
    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy == null ? null : modifyBy.trim();
    }
}