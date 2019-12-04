package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

/**
 *名师团队查询类
 */
@ApiModel(description= "名师团队查询类")
public class TeacherSearchModel  extends ManagerPagerModel{
    /**
     *名师团队表Id
     */
    @ApiModelProperty(value = "名师团队表Id")
    private String teacherId;

    /**
     *教师姓名
     */
    @ApiModelProperty(value = "教师姓名")
    private String teacherName;

    /**
     *所属组织编码
     */
    @ApiModelProperty(value = "所属组织编码")
    private String orgCode;

    /**
     *用户状态（1. 正常 ，2 停用）
     */
    @ApiModelProperty(value = "用户状态（1. 正常 ，2 停用）")
    private Integer status;

    /**
     * 类型（1 普通；2 推荐；）
     */
    @ApiModelProperty(value = "类型（1 普通；2 推荐；）")
    private Integer type;

    @ApiModelProperty(value = "课程分类")
    private String ccId;


    /**
     *名师团队表Id
     */
    @ApiModelProperty(value = "名师团队表Id列表")
    private List<String> teacherIdList;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId == null ? null : teacherId.trim();
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName == null ? null : teacherName.trim();
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCcId() {
        return ccId;
    }

    public void setCcId(String ccId) {
        this.ccId = ccId == null ? null : ccId.trim();
    }

    public List<String> getTeacherIdList() {
        return teacherIdList;
    }

    public void setTeacherIdList(List<String> teacherIdList) {
        this.teacherIdList = teacherIdList;
    }
}
