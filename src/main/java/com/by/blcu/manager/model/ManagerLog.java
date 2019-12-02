package com.by.blcu.manager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.*;

/**
 * 系统日志表
 */
@Table(name = "manager_log")
@ApiModel(description= "系统日志表")
@Data
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

}