package com.by.blcu.manager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.*;

/**
 * 系统日志表
 */
@Table(name = "manager_log")
@ApiModel(description= "系统日志表")
public class ManagerLog {
    /**
     *表Id
     */
    @Id
    @Column(name = "log_id")
    @ApiModelProperty(value = "表Id")
    private String logId;

    /**
     *操作人
     */
    @Column(name = "opt_name")
    @ApiModelProperty(value = "操作人")
    private String optName;

    /**
     *操作类型
     */
    @Column(name = "opt_type")
    @ApiModelProperty(value = "操作类型")
    private String optType;

    /**
     *操作描述
     */
    @Column(name = "opt_description")
    @ApiModelProperty(value = "操作描述")
    private String optDescription;

    /**
     *操作时间
     */
    @Column(name = "opt_time")
    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date optTime;

    /**
     *操作的IP地址
     */
    @Column(name = "opt_i_p")
    @ApiModelProperty(value = "操作的IP地址")
    private String optIP;

    /**
     *添加时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value = "添加时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     *添加人
     */
    @Column(name = "create_by")
    @ApiModelProperty(value = "添加人")
    private String createBy;

    /**
     *修改时间
     */
    @Column(name = "modify_time")
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     *修改人
     */
    @Column(name = "modify_by")
    @ApiModelProperty(value = "修改人")
    private String modifyBy;

    /**
     * 获取表Id
     *
     * @return log_id - 表Id
     */
    public String getLogId() {
        return logId;
    }

    /**
     * 设置表Id
     *
     * @param logId 表Id
     */
    public void setLogId(String logId) {
        this.logId = logId == null ? null : logId.trim();
    }

    /**
     * 获取操作人
     *
     * @return opt_name - 操作人
     */
    public String getOptName() {
        return optName;
    }

    /**
     * 设置操作人
     *
     * @param optName 操作人
     */
    public void setOptName(String optName) {
        this.optName = optName == null ? null : optName.trim();
    }

    /**
     * 获取操作类型
     *
     * @return opt_type - 操作类型
     */
    public String getOptType() {
        return optType;
    }

    /**
     * 设置操作类型
     *
     * @param optType 操作类型
     */
    public void setOptType(String optType) {
        this.optType = optType == null ? null : optType.trim();
    }

    /**
     * 获取操作描述
     *
     * @return opt_description - 操作描述
     */
    public String getOptDescription() {
        return optDescription;
    }

    /**
     * 设置操作描述
     *
     * @param optDescription 操作描述
     */
    public void setOptDescription(String optDescription) {
        this.optDescription = optDescription == null ? null : optDescription.trim();
    }

    /**
     * 获取操作时间
     *
     * @return opt_time - 操作时间
     */
    public Date getOptTime() {
        return optTime;
    }

    /**
     * 设置操作时间
     *
     * @param optTime 操作时间
     */
    public void setOptTime(Date optTime) {
        this.optTime = optTime;
    }

    /**
     * 获取操作的IP地址
     *
     * @return opt_i_p - 操作的IP地址
     */
    public String getOptIP() {
        return optIP;
    }

    /**
     * 设置操作的IP地址
     *
     * @param optIP 操作的IP地址
     */
    public void setOptIP(String optIP) {
        this.optIP = optIP == null ? null : optIP.trim();
    }

    /**
     * 获取添加时间
     *
     * @return create_time - 添加时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置添加时间
     *
     * @param createTime 添加时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取添加人
     *
     * @return create_by - 添加人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置添加人
     *
     * @param createBy 添加人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取修改人
     *
     * @return modify_by - 修改人
     */
    public String getModifyBy() {
        return modifyBy;
    }

    /**
     * 设置修改人
     *
     * @param modifyBy 修改人
     */
    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy == null ? null : modifyBy.trim();
    }
}