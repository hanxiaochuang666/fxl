package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "web_message")
@ApiModel(description= "消息提醒表")
public class WebMessage {
    /**
     *消息Id
     */
    @Id
    @Column(name = "message_id")
    @ApiModelProperty(value = "消息Id")
    private String messageId;

    /**
     *课程分类
     */
    @Column(name = "cc_id")
    @ApiModelProperty(value = "课程分类")
    private String ccId;

    /**
     *商品Id
     */
    @Column(name = "commodity_id")
    @ApiModelProperty(value = "商品Id")
    private String commodityId;

    /**
     *所属分类Id
     */
    @Column(name = "category_id")
    @ApiModelProperty(value = "所属分类Id")
    private String categoryId;

    /**
     *机构编号
     */
    @Column(name = "org_code")
    @ApiModelProperty(value = "机构编号")
    private String orgCode;

    /**
     *标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     *范围( 1 所有学员，2 按课程分类，3 按课程内容)
     */
    @ApiModelProperty(value = "范围( 1 所有学员，2 按课程分类，3 按课程内容)")
    private Integer scope;

    /**
     *消息内容
     */
    @ApiModelProperty(value = "消息内容")
    private String content;

    /**
     *附件地址（多个用逗号隔开）
     */
    @Column(name = "file_url")
    @ApiModelProperty(value = "附件地址（多个用逗号隔开）")
    private String fileUrl;

    /**
     *描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     *排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     *是否删除
     */
    @Column(name = "is_deleted")
    @ApiModelProperty(value = "是否删除")
    private Boolean isDeleted;

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
}