package com.by.blcu.resource.dto;

import java.util.Date;

public class LearnActive {
    /**
	 *INTEGER
	 *主键id
	 */
    private Integer learnActiveId;

    /**
	 *INTEGER
	 *学生id
	 */
    private Integer studentId;

    /**
	 *INTEGER
	 *课程id
	 */
    private Integer courseId;

    /**
	 *INTEGER
	 *课程详情id
	 */
    private Integer courseDetailId;

    /**
	 *TINYINT
	 *学习标志（0：未学习；1：学习）
	 */
    private Byte learnFlag;

    /**
	 *DATE
	 *学习时间(只记录第一次，试卷应该是提交后才算提交完毕)
	 */
    private Date learnTime;

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

    public Integer getLearnActiveId() {
        return learnActiveId;
    }

    public void setLearnActiveId(Integer learnActiveId) {
        this.learnActiveId = learnActiveId;
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

    public Integer getCourseDetailId() {
        return courseDetailId;
    }

    public void setCourseDetailId(Integer courseDetailId) {
        this.courseDetailId = courseDetailId;
    }

    public Byte getLearnFlag() {
        return learnFlag;
    }

    public void setLearnFlag(Byte learnFlag) {
        this.learnFlag = learnFlag;
    }

    public Date getLearnTime() {
        return learnTime;
    }

    public void setLearnTime(Date learnTime) {
        this.learnTime = learnTime;
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
        sb.append(", learnActiveId=").append(learnActiveId);
        sb.append(", studentId=").append(studentId);
        sb.append(", courseId=").append(courseId);
        sb.append(", courseDetailId=").append(courseDetailId);
        sb.append(", learnFlag=").append(learnFlag);
        sb.append(", learnTime=").append(learnTime);
        sb.append(", bak1=").append(bak1);
        sb.append(", bak2=").append(bak2);
        sb.append(", bak3=").append(bak3);
        sb.append("]");
        return sb.toString();
    }
}