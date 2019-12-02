package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "web_news_category")
@ApiModel(description= "用户")
public class WebNewsCategory {
    /**
     *新闻分类Id
     */
    @Id
    @Column(name = "category_id")
    @ApiModelProperty(value = "新闻分类Id")
    private String categoryId;

    /**
     *课程分类
     */
    @Column(name = "cc_id")
    @ApiModelProperty(value = "课程分类")
    private String ccId;

    /**
     *机构编号
     */
    @Column(name = "org_code")
    @ApiModelProperty(value = "机构编号")
    private String orgCode;

    /**
     *分类名称
     */
    @Column(name = "category_name")
    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    /**
     *分类编码
     */
    @ApiModelProperty(value = "分类编码")
    private String code;

    /**
     *所属父ID(顶级为0)
     */
    @Column(name = "parent_id")
    @ApiModelProperty(value = "所属父ID(顶级为0)")
    private String parentId;

    /**
     *分类深度
     */
    @Column(name = "class_layer")
    @ApiModelProperty(value = "分类深度")
    private Integer classLayer;

    /**
     *状态（1 正常；2 停用）
     */
    @ApiModelProperty(value = "状态（1 正常；2 停用）")
    private Integer status;

    /**
     *分类图片地址
     */
    @Column(name = "image_url")
    @ApiModelProperty(value = "分类图片地址")
    private String imageUrl;

    /**
     *链接地址
     */
    @Column(name = "link_url")
    @ApiModelProperty(value = "链接地址")
    private String linkUrl;

    /**
     *排序数字
     */
    @ApiModelProperty(value = "排序数字")
    private Integer sort;

    /**
     *分类介绍
     */
    @ApiModelProperty(value = "分类介绍")
    private String description;

    /**
     *备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     *是否删除
     */
    @Column(name = "is_deleted")
    @ApiModelProperty(value = "是否删除")
    private Boolean isDeleted;

    /**
     *创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     *创建人
     */
    @Column(name = "create_by")
    @ApiModelProperty(value = "创建人")
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