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
    private String storeDatetime;

    /**
	 *VARCHAR
	 *
	 */
    private String bucketName;

    /**
	 *VARCHAR
	 *
	 */
    private String extensionName;

    /**
	 *VARCHAR
	 *
	 */
    private String endpoint;

    /**
	 *TINYINT
	 *
	 */
    private Byte success;

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
    private String storageType;

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
    private String fileId;

    /**
	 *BIGINT
	 *播放时长(秒)
	 */
    private Long duration;

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

    public String getStoreDatetime() {
        return storeDatetime;
    }

    public void setStoreDatetime(String storeDatetime) {
        this.storeDatetime = storeDatetime == null ? null : storeDatetime.trim();
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName == null ? null : bucketName.trim();
    }

    public String getExtensionName() {
        return extensionName;
    }

    public void setExtensionName(String extensionName) {
        this.extensionName = extensionName == null ? null : extensionName.trim();
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint == null ? null : endpoint.trim();
    }

    public Byte getSuccess() {
        return success;
    }

    public void setSuccess(Byte success) {
        this.success = success;
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

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType == null ? null : storageType.trim();
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

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId == null ? null : fileId.trim();
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
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
        sb.append(", storeDatetime=").append(storeDatetime);
        sb.append(", bucketName=").append(bucketName);
        sb.append(", extensionName=").append(extensionName);
        sb.append(", endpoint=").append(endpoint);
        sb.append(", success=").append(success);
        sb.append(", securitytoken=").append(securitytoken);
        sb.append(", accesskeysecret=").append(accesskeysecret);
        sb.append(", storageType=").append(storageType);
        sb.append(", expiration=").append(expiration);
        sb.append(", keyWord=").append(keyWord);
        sb.append(", accesskeyid=").append(accesskeyid);
        sb.append(", fileId=").append(fileId);
        sb.append(", duration=").append(duration);
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