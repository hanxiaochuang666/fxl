package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import javax.persistence.*;

@ApiModel(description= "用户")
public class File {
    /**
     *文件ID
     */
    @Id
    @Column(name = "file_id")
    @ApiModelProperty(value = "文件ID")
    private String fileId;

    /**
     *商品ID
     */
    @Column(name = "commodity_id")
    @ApiModelProperty(value = "商品ID")
    private String commodityId;

    /**
     *文件名
     */
    @Column(name = "file_name")
    @ApiModelProperty(value = "文件名")
    private String fileName;

    /**
     *文件路径
     */
    @Column(name = "file_path")
    @ApiModelProperty(value = "文件路径")
    private String filePath;

    /**
     *创建时间
     */
    @Column(name = "file_time")
    @ApiModelProperty(value = "创建时间")
    private Date fileTime;

    /**
     *是否删除(删除：0，未删除：1)
     */
    @ApiModelProperty(value = "是否删除(删除：0，未删除：1)")
    private Integer isdelete;

    /**
     *是否有效(无效：0，有效：1)
     */
    @ApiModelProperty(value = "是否有效(无效：0，有效：1)")
    private Integer isvalidity;

    /**
     *存储文件名
     */
    @Column(name = "file_size")
    @ApiModelProperty(value = "存储文件名")
    private String fileSize;

    /**
     *审核状态（0：待审核；1::机审不通过；2：机审通过；3：人审不通过；4：人审通过）
     */
    @Column(name = "check_status")
    @ApiModelProperty(value = "审核状态（0：待审核；1::机审不通过；2：机审通过；3：人审不通过；4：人审通过）")
    private Integer checkStatus;

    /**
     *审核时间
     */
    @Column(name = "check_time")
    @ApiModelProperty(value = "审核时间")
    private Date checkTime;

    /**
     *审核人id
     */
    @Column(name = "check_user")
    @ApiModelProperty(value = "审核人id")
    private Integer checkUser;

    /**
     *审核意见
     */
    @Column(name = "check_msg")
    @ApiModelProperty(value = "审核意见")
    private String checkMsg;

    /**
     *审核id
     */
    @Column(name = "check_id")
    @ApiModelProperty(value = "审核id")
    private String checkId;

    /**
     *音频:audio 图片:pic 文档:doc 视频:video
     */
    @Column(name = "file_type")
    @ApiModelProperty(value = "音频:audio 图片:pic 文档:doc 视频:video")
    private String fileType;

    /**
     *备用2
     */
    @ApiModelProperty(value = "备用2")
    private String bak2;

    /**
     *备用3
     */
    @ApiModelProperty(value = "备用3")
    private String bak3;

    /**
     * 获取文件ID
     *
     * @return file_id - 文件ID
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * 设置文件ID
     *
     * @param fileId 文件ID
     */
    public void setFileId(String fileId) {
        this.fileId = fileId == null ? null : fileId.trim();
    }

    /**
     * 获取商品ID
     *
     * @return commodity_id - 商品ID
     */
    public String getCommodityId() {
        return commodityId;
    }

    /**
     * 设置商品ID
     *
     * @param commodityId 商品ID
     */
    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId == null ? null : commodityId.trim();
    }

    /**
     * 获取文件名
     *
     * @return file_name - 文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置文件名
     *
     * @param fileName 文件名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    /**
     * 获取文件路径
     *
     * @return file_path - 文件路径
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * 设置文件路径
     *
     * @param filePath 文件路径
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    /**
     * 获取创建时间
     *
     * @return file_time - 创建时间
     */
    public Date getFileTime() {
        return fileTime;
    }

    /**
     * 设置创建时间
     *
     * @param fileTime 创建时间
     */
    public void setFileTime(Date fileTime) {
        this.fileTime = fileTime;
    }

    /**
     * 获取是否删除(删除：0，未删除：1)
     *
     * @return isdelete - 是否删除(删除：0，未删除：1)
     */
    public Integer getIsdelete() {
        return isdelete;
    }

    /**
     * 设置是否删除(删除：0，未删除：1)
     *
     * @param isdelete 是否删除(删除：0，未删除：1)
     */
    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

    /**
     * 获取是否有效(无效：0，有效：1)
     *
     * @return isvalidity - 是否有效(无效：0，有效：1)
     */
    public Integer getIsvalidity() {
        return isvalidity;
    }

    /**
     * 设置是否有效(无效：0，有效：1)
     *
     * @param isvalidity 是否有效(无效：0，有效：1)
     */
    public void setIsvalidity(Integer isvalidity) {
        this.isvalidity = isvalidity;
    }

    /**
     * 获取存储文件名
     *
     * @return file_size - 存储文件名
     */
    public String getFileSize() {
        return fileSize;
    }

    /**
     * 设置存储文件名
     *
     * @param fileSize 存储文件名
     */
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize == null ? null : fileSize.trim();
    }

    /**
     * 获取审核状态（0：待审核；1::机审不通过；2：机审通过；3：人审不通过；4：人审通过）
     *
     * @return check_status - 审核状态（0：待审核；1::机审不通过；2：机审通过；3：人审不通过；4：人审通过）
     */
    public Integer getCheckStatus() {
        return checkStatus;
    }

    /**
     * 设置审核状态（0：待审核；1::机审不通过；2：机审通过；3：人审不通过；4：人审通过）
     *
     * @param checkStatus 审核状态（0：待审核；1::机审不通过；2：机审通过；3：人审不通过；4：人审通过）
     */
    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    /**
     * 获取审核时间
     *
     * @return check_time - 审核时间
     */
    public Date getCheckTime() {
        return checkTime;
    }

    /**
     * 设置审核时间
     *
     * @param checkTime 审核时间
     */
    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    /**
     * 获取审核人id
     *
     * @return check_user - 审核人id
     */
    public Integer getCheckUser() {
        return checkUser;
    }

    /**
     * 设置审核人id
     *
     * @param checkUser 审核人id
     */
    public void setCheckUser(Integer checkUser) {
        this.checkUser = checkUser;
    }

    /**
     * 获取审核意见
     *
     * @return check_msg - 审核意见
     */
    public String getCheckMsg() {
        return checkMsg;
    }

    /**
     * 设置审核意见
     *
     * @param checkMsg 审核意见
     */
    public void setCheckMsg(String checkMsg) {
        this.checkMsg = checkMsg == null ? null : checkMsg.trim();
    }

    /**
     * 获取审核id
     *
     * @return check_id - 审核id
     */
    public String getCheckId() {
        return checkId;
    }

    /**
     * 设置审核id
     *
     * @param checkId 审核id
     */
    public void setCheckId(String checkId) {
        this.checkId = checkId == null ? null : checkId.trim();
    }

    /**
     * 获取音频:audio 图片:pic 文档:doc 视频:video
     *
     * @return file_type - 音频:audio 图片:pic 文档:doc 视频:video
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * 设置音频:audio 图片:pic 文档:doc 视频:video
     *
     * @param fileType 音频:audio 图片:pic 文档:doc 视频:video
     */
    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    /**
     * 获取备用2
     *
     * @return bak2 - 备用2
     */
    public String getBak2() {
        return bak2;
    }

    /**
     * 设置备用2
     *
     * @param bak2 备用2
     */
    public void setBak2(String bak2) {
        this.bak2 = bak2 == null ? null : bak2.trim();
    }

    /**
     * 获取备用3
     *
     * @return bak3 - 备用3
     */
    public String getBak3() {
        return bak3;
    }

    /**
     * 设置备用3
     *
     * @param bak3 备用3
     */
    public void setBak3(String bak3) {
        this.bak3 = bak3 == null ? null : bak3.trim();
    }
}