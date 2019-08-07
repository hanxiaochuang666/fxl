package com.by.blcu.resource.dto;

import java.util.Date;

public class Resources {
    /**
	 *INTEGER
	 *资源id
	 */
    private Integer resourcesId;

    /**
	 *VARCHAR
	 *标题（讨论问答）
	 */
    private String title;

    /**
	 *INTEGER
	 *资源类型（0：测试；1：作业；2：讨论；3：问答；4：视频；5：直播；6文档；7文本；8资料）
	 */
    private Integer type;

    /**
	 *INTEGER
	 *机构id
	 */
    private Integer orgId;

    /**
	 *VARCHAR
	 *资源内容
	 */
    private String content;

    /**
	 *INTEGER
	 *创建者id
	 */
    private Integer createUser;

    /**
	 *DATE
	 *创建时间
	 */
    private Date creayeTime;

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

    public Integer getResourcesId() {
        return resourcesId;
    }

    public void setResourcesId(Integer resourcesId) {
        this.resourcesId = resourcesId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Date getCreayeTime() {
        return creayeTime;
    }

    public void setCreayeTime(Date creayeTime) {
        this.creayeTime = creayeTime;
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
        sb.append(", resourcesId=").append(resourcesId);
        sb.append(", title=").append(title);
        sb.append(", type=").append(type);
        sb.append(", orgId=").append(orgId);
        sb.append(", content=").append(content);
        sb.append(", createUser=").append(createUser);
        sb.append(", creayeTime=").append(creayeTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", bak1=").append(bak1);
        sb.append(", bak2=").append(bak2);
        sb.append(", bak3=").append(bak3);
        sb.append("]");
        return sb.toString();
    }
}