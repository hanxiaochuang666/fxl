package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import javax.persistence.*;

/**
 * 教师工作经历表
 */
@Table(name = "manager_teacher_exp")
@ApiModel(description= "教师扩展信息")
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

    /**
     * 获取教师工作经历表d
     *
     * @return teacher_exp_id - 教师工作经历表d
     */
    public String getTeacherExpId() {
        return teacherExpId;
    }

    /**
     * 设置教师工作经历表d
     *
     * @param teacherExpId 教师工作经历表d
     */
    public void setTeacherExpId(String teacherExpId) {
        this.teacherExpId = teacherExpId == null ? null : teacherExpId.trim();
    }

    /**
     * 获取教师入驻申请表Id
     *
     * @return teacher_apply_id - 教师入驻申请表Id
     */
    public String getTeacherApplyId() {
        return teacherApplyId;
    }

    /**
     * 设置教师入驻申请表Id
     *
     * @param teacherApplyId 教师入驻申请表Id
     */
    public void setTeacherApplyId(String teacherApplyId) {
        this.teacherApplyId = teacherApplyId == null ? null : teacherApplyId.trim();
    }

    /**
     * 获取类别（1 教学工作经历；2 教学工作成果）
     *
     * @return category - 类别（1 教学工作经历；2 教学工作成果）
     */
    public Integer getCategory() {
        return category;
    }

    /**
     * 设置类别（1 教学工作经历；2 教学工作成果）
     *
     * @param category 类别（1 教学工作经历；2 教学工作成果）
     */
    public void setCategory(Integer category) {
        this.category = category;
    }

    /**
     * 获取开始时间
     *
     * @return start_time - 开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取结束时间
     *
     * @return end_time - 结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取经历描述
     *
     * @return description - 经历描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置经历描述
     *
     * @param description 经历描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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