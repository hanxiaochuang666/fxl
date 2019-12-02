package com.by.blcu.mall.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "mall_course_category_info")
@ApiModel(value = "课程分类对象")
public class CourseCategoryInfo implements Serializable {
    private static final long serialVersionUID = 1568604570565196077L;
    /**
     * 课程分类id
     */
    @Id
    @Column(name = "cc_id")
    @ApiModelProperty(value = "主键id，自动生成")
    private String ccId;

    /**
     * 分类名称
     */
    @Column(name = "cc_name")
    @ApiModelProperty(value = "分类名")
    private String ccName;

    /**
     * 父分类id
     */
    @Column(name = "parent_id")
    @ApiModelProperty(value = "父类ID，0为一级分类")
    private String parentId;

    /**
     * 状态(删除：0，正常：1)
     */
    @Column(name = "cc_status")
    @ApiModelProperty(value = "分类状态，删除：0，正常：1，添加分类时不传")
    private Integer ccStatus;

    /**
     * 顺序
     */
    @Column(name = "cc_sort")
    @ApiModelProperty(value = "顺序，添加分类时不传")
    private Integer ccSort;

    /**
     * 创建时间
     */
    @Column(name = "cc_create_time")
    @ApiModelProperty(value = "创建时间,添加时不传")
    @JsonIgnore
    private Date ccCreateTime;

    /**
     * 创建人
     */
    @Column(name = "cc_creator")
    @ApiModelProperty(value = "创建人")
    private String ccCreator;

    /**
     * 目录级别
     */
    @Column(name = "level")
    @ApiModelProperty(value = "目录级别")
    private Integer level;

    /**
     * 获取课程分类id
     *
     * @return cc_id - 课程分类id
     */
    public String getCcId() {
        return ccId;
    }

    /**
     * 设置课程分类id
     *
     * @param ccId 课程分类id
     */
    public void setCcId(String ccId) {
        this.ccId = ccId;
    }

    /**
     * 获取分类名称
     *
     * @return cc_name - 分类名称
     */
    public String getCcName() {
        return ccName;
    }

    /**
     * 设置分类名称
     *
     * @param ccName 分类名称
     */
    public void setCcName(String ccName) {
        this.ccName = ccName == null ? null : ccName.trim();
    }

    /**
     * 获取父分类id
     *
     * @return parent_id - 父分类id
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * 设置父分类id
     *
     * @param parentId 父分类id
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取状态(删除：0，正常：1)
     *
     * @return cc_status - 状态(删除：0，正常：1)
     */
    public Integer getCcStatus() {
        return ccStatus;
    }

    /**
     * 设置状态(删除：0，正常：1)
     *
     * @param ccStatus 状态(删除：0，正常：1)
     */
    public void setCcStatus(Integer ccStatus) {
        this.ccStatus = ccStatus;
    }

    /**
     * 获取顺序
     *
     * @return cc_sort - 顺序
     */
    public Integer getCcSort() {
        return ccSort;
    }

    /**
     * 设置顺序
     *
     * @param ccSort 顺序
     */
    public void setCcSort(Integer ccSort) {
        this.ccSort = ccSort;
    }

    /**
     * 获取创建时间
     *
     * @return cc_create_time - 创建时间
     */
    public Date getCcCreateTime() {
        return ccCreateTime;
    }

    /**
     * 设置创建时间
     *
     * @param ccCreateTime 创建时间
     */
    public void setCcCreateTime(Date ccCreateTime) {
        this.ccCreateTime = ccCreateTime;
    }

    /**
     * 获取创建人
     *
     * @return cc_creator - 创建人
     */
    public String getCcCreator() {
        return ccCreator;
    }

    /**
     * 设置创建人
     *
     * @param ccCreator 创建人
     */
    public void setCcCreator(String ccCreator) {
        this.ccCreator = ccCreator == null ? null : ccCreator.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}