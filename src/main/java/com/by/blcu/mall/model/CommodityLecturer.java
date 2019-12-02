package com.by.blcu.mall.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
@Data
@Table(name = "mall_commodity_lecturer")
@ApiModel(value = "商品对象")
public class CommodityLecturer implements Serializable {

    private static final long serialVersionUID = -5138461221865469955L;
    /**
     * id
     */
    @Id
    @Column(name = "cl_id")
    @ApiModelProperty(value = "主键id，自动生成")
    private String clId;

    /**
     * 商品id
     */
    @Column(name = "commodity_id")
    @ApiModelProperty(value = "商品id")
    private String commodityId;

    /**
     * 教师编号
     */
    @Column(name = "lecturer")
    @ApiModelProperty(value = "教师编号")
    private String lecturer;

    /**
     * 教师名字
     */
    @ApiModelProperty(value = "教师名字")
    private String lecturerName;


}
