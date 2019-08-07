package com.by.blcu.resource.dto;

import java.util.Date;

public class VideoInfo {
    /**
	 *INTEGER
	 *主键id
	 */
    private Integer videoInfoId;

    /**
	 *VARCHAR
	 *
	 */
    private String storedatetime;

    /**
	 *VARCHAR
	 *
	 */
    private String bucketname;

    /**
	 *VARCHAR
	 *
	 */
    private String extensionname;

    /**
	 *VARCHAR
	 *
	 */
    private String endpoint;

    /**
	 *VARCHAR
	 *
	 */
    private String securitytoken;

    /**
	 *VARCHAR
	 *
	 */
    private String accesskeysecret;

    /**
	 *VARCHAR
	 *
	 */
    private String storagetype;

    /**
	 *VARCHAR
	 *
	 */
    private String expiration;

    /**
	 *VARCHAR
	 *
	 */
    private String keyWord;

    /**
	 *VARCHAR
	 *
	 */
    private String accesskeyid;

    /**
	 *VARCHAR
	 *
	 */
    private String fileid;

    /**
	 *VARCHAR
	 *播放地址
	 */
    private String url;

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
	 *视频名称
	 */
    private String videoName;

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

    public Integer getVideoInfoId() {
        return videoInfoId;
    }

    public void setVideoInfoId(Integer videoInfoId) {
        this.videoInfoId = videoInfoId;
    }

    public String getStoredatetime() {
        return storedatetime;
    }

    public void setStoredatetime(String storedatetime) {
        this.storedatetime = storedatetime == null ? null : storedatetime.trim();
    }

    public String getBucketname() {
        return bucketname;
    }

    public void setBucketname(String bucketname) {
        this.bucketname = bucketname == null ? null : bucketname.trim();
    }

    public String getExtensionname() {
        return extensionname;
    }

    public void setExtensionname(String extensionname) {
        this.extensionname = extensionname == null ? null : extensionname.trim();
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint == null ? null : endpoint.trim();
    }

    public String getSecuritytoken() {
        return securitytoken;
    }

    public void setSecuritytoken(String securitytoken) {
        this.securitytoken = securitytoken == null ? null : securitytoken.trim();
    }

    public String getAccesskeysecret() {
        return accesskeysecret;
    }

    public void setAccesskeysecret(String accesskeysecret) {
        this.accesskeysecret = accesskeysecret == null ? null : accesskeysecret.trim();
    }

    public String getStoragetype() {
        return storagetype;
    }

    public void setStoragetype(String storagetype) {
        this.storagetype = storagetype == null ? null : storagetype.trim();
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration == null ? null : expiration.trim();
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord == null ? null : keyWord.trim();
    }

    public String getAccesskeyid() {
        return accesskeyid;
    }

    public void setAccesskeyid(String accesskeyid) {
        this.accesskeyid = accesskeyid == null ? null : accesskeyid.trim();
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid == null ? null : fileid.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
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

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName == null ? null : videoName.trim();
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
        sb.append(", videoInfoId=").append(videoInfoId);
        sb.append(", storedatetime=").append(storedatetime);
        sb.append(", bucketname=").append(bucketname);
        sb.append(", extensionname=").append(extensionname);
        sb.append(", endpoint=").append(endpoint);
        sb.append(", securitytoken=").append(securitytoken);
        sb.append(", accesskeysecret=").append(accesskeysecret);
        sb.append(", storagetype=").append(storagetype);
        sb.append(", expiration=").append(expiration);
        sb.append(", keyWord=").append(keyWord);
        sb.append(", accesskeyid=").append(accesskeyid);
        sb.append(", fileid=").append(fileid);
        sb.append(", url=").append(url);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", videoName=").append(videoName);
        sb.append(", bak1=").append(bak1);
        sb.append(", bak2=").append(bak2);
        sb.append(", bak3=").append(bak3);
        sb.append("]");
        return sb.toString();
    }
}