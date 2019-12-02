package com.by.blcu.resource.dto;

import java.util.Date;

public class LiveTelecast {
    /**
	 *INTEGER
	 *主键id
	 */
    private Integer liveTelecasId;

    /**
	 *VARCHAR
	 *名称
	 */
    private String name;

    /**
	 *DATE
	 *开始时间
	 */
    private Date startTime;

    /**
	 *DATE
	 *结束时间
	 */
    private Date endTime;

    /**
	 *VARCHAR
	 *描述
	 */
    private String des;

    /**
	 *VARCHAR
	 *回调url
	 */
    private String checkUrl;

    /**
	 *VARCHAR
	 *老师直播间url
	 */
    private String tecUrl;

    /**
	 *VARCHAR
	 *助教直播间url
	 */
    private String assUrl;

    /**
	 *VARCHAR
	 *学生直播间url
	 */
    private String stuUrl;

    /**
	 *VARCHAR
	 *直播间id
	 */
    private String romeId;

    /**
	 *INTEGER
	 *状态
	 */
    private Integer status;

    /**
	 *VARCHAR
	 *直播回放地址
	 */
    private String playbackUrl;

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
	 *修改者id
	 */
    private Integer updateUser;

    /**
	 *TIMESTAMP
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

    public Integer getLiveTelecasId() {
        return liveTelecasId;
    }

    public void setLiveTelecasId(Integer liveTelecasId) {
        this.liveTelecasId = liveTelecasId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des == null ? null : des.trim();
    }

    public String getCheckUrl() {
        return checkUrl;
    }

    public void setCheckUrl(String checkUrl) {
        this.checkUrl = checkUrl == null ? null : checkUrl.trim();
    }

    public String getTecUrl() {
        return tecUrl;
    }

    public void setTecUrl(String tecUrl) {
        this.tecUrl = tecUrl == null ? null : tecUrl.trim();
    }

    public String getAssUrl() {
        return assUrl;
    }

    public void setAssUrl(String assUrl) {
        this.assUrl = assUrl == null ? null : assUrl.trim();
    }

    public String getStuUrl() {
        return stuUrl;
    }

    public void setStuUrl(String stuUrl) {
        this.stuUrl = stuUrl == null ? null : stuUrl.trim();
    }

    public String getRomeId() {
        return romeId;
    }

    public void setRomeId(String romeId) {
        this.romeId = romeId == null ? null : romeId.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPlaybackUrl() {
        return playbackUrl;
    }

    public void setPlaybackUrl(String playbackUrl) {
        this.playbackUrl = playbackUrl == null ? null : playbackUrl.trim();
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
        sb.append(", liveTelecasId=").append(liveTelecasId);
        sb.append(", name=").append(name);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", des=").append(des);
        sb.append(", checkUrl=").append(checkUrl);
        sb.append(", tecUrl=").append(tecUrl);
        sb.append(", assUrl=").append(assUrl);
        sb.append(", stuUrl=").append(stuUrl);
        sb.append(", romeId=").append(romeId);
        sb.append(", status=").append(status);
        sb.append(", playbackUrl=").append(playbackUrl);
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