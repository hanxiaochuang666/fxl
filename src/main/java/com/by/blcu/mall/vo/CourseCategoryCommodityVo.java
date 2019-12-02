package com.by.blcu.mall.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.by.blcu.mall.model.CommodityInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@ApiModel(value = "课程分类对象")
public class CourseCategoryCommodityVo implements Serializable {
    private static final long serialVersionUID = 5557930967584456787L;
    /**
     * 课程分类id
     */
    @ApiModelProperty(value = "主键id")
    private String ccId;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名")
    private String ccName;

    /**
     * 父分类id
     */
    @ApiModelProperty(value = "父分类id")
    private String parentId;

    /**
     * 状态(删除：0，正常：1)
     */
    @ApiModelProperty(value = "是否删除")
    private Integer ccStatus;

    /**
     * 顺序
     */
    @ApiModelProperty(value = "顺序")
    private Integer ccSort;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date ccCreateTime;

    public List<CourseCategoryCommodityVo> getChildren() {
        return children;
    }

    public void setChildren(List<CourseCategoryCommodityVo> children) {
        this.children = children;
    }

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String ccCreator;

    /**
     * 目录级别
     */
    @ApiModelProperty(value = "目录级别")
    private Integer level;

    @ApiModelProperty(value = "子节点")
    @JSONField(serialzeFeatures= {SerializerFeature.WriteMapNullValue})
    private List<CourseCategoryCommodityVo> children;
    @ApiModelProperty(value = "商品集合")
    @JSONField(serialzeFeatures= {SerializerFeature.WriteMapNullValue})
    private List<CommodityInfoVo> commodityInfoList;

    public List<CommodityInfoVo> getCommodityInfoList() {
        return commodityInfoList;
    }

    public void setCommodityInfoList(List<CommodityInfoVo> commodityInfoList) {
        this.commodityInfoList = commodityInfoList;
    }

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