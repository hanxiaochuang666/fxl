package com.by.blcu.resource.dto;

import java.util.Date;

public class TestResult {
    /**
	 *INTEGER
	 *id
	 */
    private Integer testResultId;

    /**
	 *INTEGER
	 *学生id
	 */
    private Integer studentId;

    /**
	 *INTEGER
	 *试卷id
	 */
    private Integer testPaperId;

    /**
	 *INTEGER
	 *作业所处状态0:未开始答题；1:继续答题，2已提交；3批改中；4批改完成；5已过期
	 */
    private Integer status;

    /**
	 *DATE
	 *开始答题时间
	 */
    private Date startTime;

    /**
	 *DATE
	 *完成答题时间
	 */
    private Date endTime;

    /**
     * 答题时长
     */
    private Integer  useTime;

    /**
	 *INTEGER
	 *批改人
	 */
    private Integer makingUser;

    /**
	 *DATE
	 *批改时间
	 */
    private Date markingTime;

    /**
	 *INTEGER
	 *客观题得分
	 */
    private Integer objectiveScore;

    /**
	 *INTEGER
	 *主观题得分
	 */
    private Integer subjectiveScore;

    /**
	 *INTEGER
	 *总得分
	 */
    private Integer totalScore;

    public Integer getTestResultId() {
        return testResultId;
    }

    public void setTestResultId(Integer testResultId) {
        this.testResultId = testResultId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getTestPaperId() {
        return testPaperId;
    }

    public void setTestPaperId(Integer testPaperId) {
        this.testPaperId = testPaperId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getMakingUser() {
        return makingUser;
    }

    public void setMakingUser(Integer makingUser) {
        this.makingUser = makingUser;
    }

    public Date getMarkingTime() {
        return markingTime;
    }

    public void setMarkingTime(Date markingTime) {
        this.markingTime = markingTime;
    }

    public Integer getObjectiveScore() {
        return objectiveScore;
    }

    public void setObjectiveScore(Integer objectiveScore) {
        this.objectiveScore = objectiveScore;
    }

    public Integer getSubjectiveScore() {
        return subjectiveScore;
    }

    public void setSubjectiveScore(Integer subjectiveScore) {
        this.subjectiveScore = subjectiveScore;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", testResultId=").append(testResultId);
        sb.append(", studentId=").append(studentId);
        sb.append(", testPaperId=").append(testPaperId);
        sb.append(", status=").append(status);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", useTime=").append(useTime);
        sb.append(", makingUser=").append(makingUser);
        sb.append(", markingTime=").append(markingTime);
        sb.append(", objectiveScore=").append(objectiveScore);
        sb.append(", subjectiveScore=").append(subjectiveScore);
        sb.append(", totalScore=").append(totalScore);
        sb.append("]");
        return sb.toString();
    }

    public Integer getUseTime() {
        return useTime;
    }

    public void setUseTime(Integer useTime) {
        this.useTime = useTime;
    }
}