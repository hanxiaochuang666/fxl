package com.by.blcu.mall.vo;

import com.by.blcu.mall.model.CommodityLecturer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(value = "商品对象")
@Data
public class CommodityInfoVo implements Serializable {
    private static final long serialVersionUID = -29955955351196691L;
    /**
     * 商品id
     */
    @Id
    @ApiModelProperty(value = "商品id")
    private String commodityId;

    /**
     * 课程分类id
     */
    @NotBlank(message="课程分类不能为空")
    @ApiModelProperty(value = "课程分类id")
    private String ccId;

    /**
     * 课程分类名称
     */
    @NotBlank(message="课程分类不能为空")
    @ApiModelProperty(value = "课程分类名称")
    private String ccName;

    /**
     * 课程名称
     */
    @ApiModelProperty(value = "课程名称")
    private String courseName;

    /**
     * 商品状态（下架：0，上架：1）
     */
    @ApiModelProperty(value = "商品状态（下架：0，上架：1）")
    private Integer commodityStatus;

    /**
     * 价格
     */
    @NotNull(message="原价不能为空")
    @ApiModelProperty(value = "价格")
    private Double price;

    /**
     * 删除状态（删除：0，未删除：1）
     */
    @ApiModelProperty(value = "删除状态（删除：0，未删除：1）")
    private Integer status;

    /**
     * 有效期(-1为永久有效)
     */
    @NotNull(message="原价不能为空")
    @ApiModelProperty(value = "有效期(-1为永久有效)")
    private Integer validity;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String creator;

    /**
     * 特色简介
     */
    @NotBlank(message="特色简介不能为空")
    @ApiModelProperty(value = "特色简介")
    private String introduce;

    /**
     * 优惠价
     */
    @NotNull(message="优惠价不能为空")
    @ApiModelProperty(value = "优惠价")
    private Double preferential;

    /**
     * 购买人数
     */
    @NotNull(message="购买人数不能为空")
    @ApiModelProperty(value = "购买人数")
    private Integer persionNum;

    /**
     * 课时
     */
    @NotNull(message="课时不能为空")
    @ApiModelProperty(value = "课时")
    private Integer classTime;

    /**
     * 开班时间
     */
    @NotNull(message="开班时间不能为空")
    @ApiModelProperty(value = "开班时间")
    private Date openClassTime;

    /**
     * 授课类型
     */
    @NotNull(message="授课类型不能为空")
    @ApiModelProperty(value = "授课类型")
    private Integer lessonType;

    /**
     * 主讲老师
     */
    @ApiModelProperty(value = "主讲老师")
    private List<CommodityLecturer> commodityLecturerList;

    /**
     * 套餐子商品
     */
    @ApiModelProperty(value = "套餐子商品")
    private List<CommodityInfoVo> commodityInfoVoList;

    /**
     * 试听视频
     */
    //@NotBlank(message="试听视频不能为空")
    @ApiModelProperty(value = "试听视频")
    private String auditionVideoPath;

    /**
     * 课程封面
     */
    @NotBlank(message="课程封面不能为空")
    @ApiModelProperty(value = "课程封面")
    private String picturePath;

    /**
     * 咨询方式
     */
    @ApiModelProperty(value = "咨询方式")
    private String contactInfo;

    /**
     * 自动发送消息(不自动发送：0，自动发送：1)
     */
    @ApiModelProperty(value = "自动发送消息(不自动发送：0，自动发送：1)")
    private Integer aotoMail;

    /**
     * 课程简介
     */
    @ApiModelProperty(value = "课程简介")
    private String courseIntroduce;

    /**
     * 课程id
     */
    @ApiModelProperty(value = "课程id")
    private String courseId;

    /**
     * 课程类型(单一课程：0，套餐：1)
     */
    @ApiModelProperty(value = "课程类型(单一课程：0，套餐：1)")
    private Integer courseType;


    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否推荐(未推荐：0，推荐：1)")
    private Integer isRecommend;

    /**
     * 机构编码
     */
    @NotBlank(message="机构不能为空")
    @ApiModelProperty(value = "机构编码")
    private String orgCode;

    /**
     * 排序
     */
    @Column(name = "recommend_sort")
    @ApiModelProperty(value = "推荐排序")
    private Integer recommendSort;

    /**
     * 课时标准
     */
    @NotNull(message="课时标准不能为空")
    private Double standard;


    @ApiModelProperty(value = "是否收藏(未收藏：0，收藏：1)")
    private Integer isFavorite;

    public Integer getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Integer isFavorite) {
        this.isFavorite = isFavorite;
    }

    /**
     * 是否展示（展示：1，不展示：0）
     */
    @Column(name = "is_view")
    @ApiModelProperty(value = "是否展示")
    private Integer isView;

    /**
     * 是否打包（打包：1，不打包：0）
     */
    @Column(name = "is_packing")
    @ApiModelProperty(value = "是否打包")
    private Integer isPacking;

    /**
     * 套餐类型（固定套餐：0，可选套餐：1）
     */
    @Column(name = "meal_type")
    @ApiModelProperty(value = "套餐类型")
    private Integer mealType;


}