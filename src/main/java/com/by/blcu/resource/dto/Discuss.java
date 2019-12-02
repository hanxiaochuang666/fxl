package com.by.blcu.resource.dto;

import java.util.Date;

public class Discuss {
    /**
	 *INTEGER
	 *主键id
	 */
    private Integer discussId;

    /**
	 *INTEGER
	 *学生id
	 */
    private Integer studentId;

    /**
     *INTEGER
     *父级回复创建者的id
     */
    private Integer parentUserId;

    /**
	 *INTEGER
	 *课程id
	 */
    private Integer courseId;

    /**
	 *INTEGER
	 *讨论资源id
	 */
    private Integer resourceId;

    /**
	 *VARCHAR
	 *讨论内容
	 */
    private String content;

    /**
	 *INTEGER
	 *多级讨论回复的父id，如果回复的是一级讨论，则为0
	 */
    private Integer parentId;

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
	 *修改者id
	 */
    private Integer updateUser;

    /**
	 *DATE
	 *修改时间
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

    public Integer getDiscussId() {
        return discussId;
    }

    public void setDiscussId(Integer discussId) {
        this.discussId = discussId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    public Integer getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Integer parentUserId) {
        this.parentUserId = parentUserId;
    }

    @Override
    public String toString() {
        return "Discuss{" +
                "discussId=" + discussId +
                ", studentId=" + studentId +
                ", parentUserId=" + parentUserId +
                ", courseId=" + courseId +
                ", resourceId=" + resourceId +
                ", content='" + content + '\'' +
                ", parentId=" + parentId +
                ", createUser=" + createUser +
                ", createTime=" + createTime +
                ", updateUser=" + updateUser +
                ", updateTime=" + updateTime +
                ", bak1='" + bak1 + '\'' +
                ", bak2='" + bak2 + '\'' +
                ", bak3='" + bak3 + '\'' +
                '}';
    }
}