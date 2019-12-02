package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "web_news")
@ApiModel(description= "用户")
public class WebNews {
    /**
     *新闻Id
     */
    @Id
    @Column(name = "news_id")
    @ApiModelProperty(value = "新闻Id")
    private String newsId;

    /**
     *课程一级分类Id
     */
    @Column(name = "cc_id1")
    @ApiModelProperty(value = "课程一级分类Id")
    private String ccId1;

    /**
     *课程二级分类Id
     */
    @Column(name = "cc_id2")
    @ApiModelProperty(value = "课程二级分类Id")
    private String ccId2;

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
     *作者
     */
    @ApiModelProperty(value = "作者")
    private String author;

    /**
     *状态( 1 置顶+最新，2 置顶，3 最新，4 普通)
     */
    @ApiModelProperty(value = "状态( 1 置顶+最新，2 置顶，3 最新，4 普通)")
    private Integer status;

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

    @ApiModelProperty(value = "点击次数")
    private Integer clicks;

    @ApiModelProperty(value = "类型（1 普通；2 推荐；）")
    private Integer type;


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
     *新闻内容
     */
    @ApiModelProperty(value = "新闻内容")
    private String content;
}