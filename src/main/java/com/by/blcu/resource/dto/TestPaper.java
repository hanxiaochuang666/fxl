package com.by.blcu.resource.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TestPaper {
    /**
	 *INTEGER
	 *试卷id
	 */
    private Integer testPaperId;

    /**
	 *VARCHAR
	 *试卷名称
	 */
    private String name;

    /**
	 *INTEGER
	 *一级类目id
	 */
    private Integer categoryOne;

    /**
	 *INTEGER
	 *二级类目id
	 */
    private Integer categoryTwo;

    /**
	 *INTEGER
	 *课程id
	 */
    private Integer courseId;

    /**
	 *TINYINT
	 *用途:0 测试，1作业
	 */
    private int useType;

    /**
	 *INTEGER
	 *时长(单位  分)
	 */
    private Integer time;

    /**
	 *DATE
	 *有效期开始时间
	 */
    private Date startTime;

    /**
	 *DATE
	 *有效期结束时间
	 */
    private Date endTime;

    /**
	 *TINYINT
	 *是否计分:0计分，1不计分
	 */
    private int isScore;

    /**
	 *INTEGER
	 *总分
	 */
    private Integer totalScore;

    /**
	 *INTEGER
	 *组卷类型（0:人工组卷；1:智能组卷）
	 */
    private Integer formType;

    /**
	 *VARCHAR
	 *导出文件path
	 */
    private String exportPath;

    /**
	 *DATE
	 *导出时间
	 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date exportTime;

    /**
	 *INTEGER
	 *机构id
	 */
    private Integer orgId;

    /**
	 *INTEGER
	 *创建者id
	 */
    private Integer createUser;

    /**
	 *TIMESTAMP
	 *创建时间
	 */
    private Date createTime;

    /**
	 *INTEGER
	 *更新者id
	 */
    private Integer updateUser;

    /**
	 *TIMESTAMP
	 *更新时间
	 */
    private Date updateTime;

    public Integer getTestPaperId() {
        return testPaperId;
    }

    public void setTestPaperId(Integer testPaperId) {
        this.testPaperId = testPaperId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCategoryOne() {
        return categoryOne;
    }

    public void setCategoryOne(Integer categoryOne) {
        this.categoryOne = categoryOne;
    }

    public Integer getCategoryTwo() {
        return categoryTwo;
    }

    public void setCategoryTwo(Integer categoryTwo) {
        this.categoryTwo = categoryTwo;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getIsScore() {
        return isScore;
    }

    public void setIsScore(int isScore) {
        this.isScore = isScore;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getFormType() {
        return formType;
    }

    public void setFormType(Integer formType) {
        this.formType = formType;
    }

    public String getExportPath() {
        return exportPath;
    }

    public void setExportPath(String exportPath) {
        this.exportPath = exportPath == null ? null : exportPath.trim();
    }

    public Date getExportTime() {
        return exportTime;
    }

    public void setExportTime(Date exportTime) {
        this.exportTime = exportTime;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", testPaperId=").append(testPaperId);
        sb.append(", name=").append(name);
        sb.append(", categoryOne=").append(categoryOne);
        sb.append(", categoryTwo=").append(categoryTwo);
        sb.append(", courseId=").append(courseId);
        sb.append(", useType=").append(useType);
        sb.append(", time=").append(time);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", isScore=").append(isScore);
        sb.append(", totalScore=").append(totalScore);
        sb.append(", formType=").append(formType);
        sb.append(", exportPath=").append(exportPath);
        sb.append(", exportTime=").append(exportTime);
        sb.append(", orgId=").append(orgId);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}