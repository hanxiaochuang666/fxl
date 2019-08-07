package com.by.blcu.resource.dto;

import java.util.Date;

public class Question {
    /**
	 *INTEGER
	 *试题id
	 */
    private Integer questionId;

    /**
	 *INTEGER
	 *类目一级id
	 */
    private Integer categoryOne;

    /**
	 *INTEGER
	 *类目二级id
	 */
    private Integer categoryTwo;

    /**
	 *INTEGER
	 *课程id
	 */
    private Integer courseId;

    /**
	 *VARCHAR
	 *知识点id,多个使用;分割
	 */
    private String knowledgePoints;

    /**
	 *INTEGER
	 *难度等级(0：无;1:易；2：中；3：难)
	 */
    private Integer difficultyLevel;

    /**
	 *INTEGER
	 *试题类型id
	 */
    private Integer questionType;

    /**
	 *VARCHAR
	 *题干
	 */
    private String questionBody;

    /**
	 *VARCHAR
	 *音频fileId
	 */
    private String questionSound;

    /**
	 *VARCHAR
	 *选项(复用与综合体，表示子题id，使用;分割)
	 */
    private String questionOpt;

    /**
	 *VARCHAR
	 *答案
	 */
    private String questionAnswer;

    /**
	 *VARCHAR
	 *解析
	 */
    private String questionResolve;

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
	 *INTEGER
	 *父试题id
	 */
    private Integer parentQuestionId;

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

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
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

    public String getKnowledgePoints() {
        return knowledgePoints;
    }

    public void setKnowledgePoints(String knowledgePoints) {
        this.knowledgePoints = knowledgePoints == null ? null : knowledgePoints.trim();
    }

    public Integer getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(Integer difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public String getQuestionBody() {
        return questionBody;
    }

    public void setQuestionBody(String questionBody) {
        this.questionBody = questionBody == null ? null : questionBody.trim();
    }

    public String getQuestionSound() {
        return questionSound;
    }

    public void setQuestionSound(String questionSound) {
        this.questionSound = questionSound == null ? null : questionSound.trim();
    }

    public String getQuestionOpt() {
        return questionOpt;
    }

    public void setQuestionOpt(String questionOpt) {
        this.questionOpt = questionOpt == null ? null : questionOpt.trim();
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer == null ? null : questionAnswer.trim();
    }

    public String getQuestionResolve() {
        return questionResolve;
    }

    public void setQuestionResolve(String questionResolve) {
        this.questionResolve = questionResolve == null ? null : questionResolve.trim();
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

    public Integer getParentQuestionId() {
        return parentQuestionId;
    }

    public void setParentQuestionId(Integer parentQuestionId) {
        this.parentQuestionId = parentQuestionId;
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
        sb.append(", questionId=").append(questionId);
        sb.append(", categoryOne=").append(categoryOne);
        sb.append(", categoryTwo=").append(categoryTwo);
        sb.append(", courseId=").append(courseId);
        sb.append(", knowledgePoints=").append(knowledgePoints);
        sb.append(", difficultyLevel=").append(difficultyLevel);
        sb.append(", questionType=").append(questionType);
        sb.append(", questionBody=").append(questionBody);
        sb.append(", questionSound=").append(questionSound);
        sb.append(", questionOpt=").append(questionOpt);
        sb.append(", questionAnswer=").append(questionAnswer);
        sb.append(", questionResolve=").append(questionResolve);
        sb.append(", orgId=").append(orgId);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", parentQuestionId=").append(parentQuestionId);
        sb.append(", bak1=").append(bak1);
        sb.append(", bak2=").append(bak2);
        sb.append(", bak3=").append(bak3);
        sb.append("]");
        return sb.toString();
    }
}