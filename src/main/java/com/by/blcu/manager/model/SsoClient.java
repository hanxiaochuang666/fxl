package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import javax.persistence.*;

/**
 * 客户端表
 */
@Table(name = "sso_client")
@ApiModel(description= "客户端表")
public class SsoClient {
    /**
     *表Id
     */
    @Id
    @ApiModelProperty(value = "表Id")
    private String id;

    /**
     *客户端Id
     */
    @Column(name = "client_id")
    @ApiModelProperty(value = "客户端Id")
    private String clientId;

    /**
     *客户端名称
     */
    @Column(name = "client_name")
    @ApiModelProperty(value = "客户端名称")
    private String clientName;

    /**
     *客户端密钥
     */
    @Column(name = "client_secret")
    @ApiModelProperty(value = "客户端密钥")
    private String clientSecret;

    /**
     *客户端描述
     */
    @ApiModelProperty(value = "客户端描述")
    private String description;

    /**
     *添加时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value = "添加时间")
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
     * @return id - 表Id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置表Id
     *
     * @param id 表Id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取客户端Id
     *
     * @return client_id - 客户端Id
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * 设置客户端Id
     *
     * @param clientId 客户端Id
     */
    public void setClientId(String clientId) {
        this.clientId = clientId == null ? null : clientId.trim();
    }

    /**
     * 获取客户端名称
     *
     * @return client_name - 客户端名称
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * 设置客户端名称
     *
     * @param clientName 客户端名称
     */
    public void setClientName(String clientName) {
        this.clientName = clientName == null ? null : clientName.trim();
    }

    /**
     * 获取客户端密钥
     *
     * @return client_secret - 客户端密钥
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * 设置客户端密钥
     *
     * @param clientSecret 客户端密钥
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret == null ? null : clientSecret.trim();
    }

    /**
     * 获取客户端描述
     *
     * @return description - 客户端描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置客户端描述
     *
     * @param description 客户端描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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