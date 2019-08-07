package com.by.blcu.course.dto;

import java.util.Date;

public class Course {
    /**
	 *INTEGER
	 *课程id
	 */
    private Integer courseId;

    /**
	 *VARCHAR
	 *课程名称
	 */
    private String name;

    /**
	 *INTEGER
	 *类目一级id
	 */
    private Integer categoryOne;

    /**
	 *INTEGER
	 *状态（是否完成）
	 */
    private Integer status;

    /**
	 *INTEGER
	 *类目二级id
	 */
    private Integer categoryTwo;

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
	 *DATE
	 *创建时间
	 */
    private Date createTime;

    /**
	 *INTEGER
	 *更新者id
	 */
    private Integer updateUser;

    /**
	 *DATE
	 *更新时间
	 */
    private Date updateTime;

    /**
	 *VARCHAR
	 *备用1
	 */
    private String bak1;

    /**
	 *VARCHAR
	 *备用2
	 */
    private String bak2;

    /**
	 *VARCHAR
	 *备用3
	 */
    private String bak3;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCategoryTwo() {
        return categoryTwo;
    }

    public void setCategoryTwo(Integer categoryTwo) {
        this.categoryTwo = categoryTwo;
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

    public String getBak1() {
        return bak1;
    }

    public void setBak1(String bak1) {
        this.bak1 = bak1 == null ? null : bak1.trim();
    }

    public String getBak2() {
        return bak2;
    }

    public void setBak2(String bak2) {
        this.bak2 = bak2 == null ? null : bak2.trim();
    }

    public String getBak3() {
        return bak3;
    }

    public void setBak3(String bak3) {
        this.bak3 = bak3 == null ? null : bak3.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", courseId=").append(courseId);
        sb.append(", name=").append(name);
        sb.append(", categoryOne=").append(categoryOne);
        sb.append(", status=").append(status);
        sb.append(", categoryTwo=").append(categoryTwo);
        sb.append(", orgId=").append(orgId);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", bak1=").append(bak1);
        sb.append(", bak2=").append(bak2);
        sb.append(", bak3=").append(bak3);
        sb.append("]");
        return sb.toString();
    }
}