package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "manager_areas")
@ApiModel(description= "省市区表")
public class ManagerAreas {
    /**
     *省市区表Id(自增)
     */
    @Id
    @Column(name = "areas_id")
    @ApiModelProperty(value = "省市区表Id(自增)")
    private Integer areasId;

    /**
     *全国编码
     */
    @ApiModelProperty(value = "全国编码")
    private String code;

    /**
     *名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     *上级标识
     */
    @ApiModelProperty(value = "上级标识")
    private String pid;

    /**
     *地名简称
     */
    @ApiModelProperty(value = "地名简称")
    private String sname;

    /**
     *等级
     */
    @ApiModelProperty(value = "等级")
    private Integer level;

    /**
     *简编
     */
    @ApiModelProperty(value = "简编")
    private String citycode;

    /**
     *邮政编码
     */
    @ApiModelProperty(value = "邮政编码")
    private String yzcode;

    /**
     *组合名称
     */
    @ApiModelProperty(value = "组合名称")
    private String mername;

    /**
     *经度
     */
    @Column(name = "Lng")
    @ApiModelProperty(value = "经度")
    private BigDecimal lng;

    /**
     *纬度
     */
    @Column(name = "Lat")
    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    /**
     *名称拼音
     */
    @ApiModelProperty(value = "名称拼音")
    private String pingyin;

    /**
     * 获取省市区表Id(自增)
     *
     * @return areas_id - 省市区表Id(自增)
     */
    public Integer getAreasId() {
        return areasId;
    }

    /**
     * 设置省市区表Id(自增)
     *
     * @param areasId 省市区表Id(自增)
     */
    public void setAreasId(Integer areasId) {
        this.areasId = areasId;
    }

    /**
     * 获取全国编码
     *
     * @return code - 全国编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置全国编码
     *
     * @param code 全国编码
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取上级标识
     *
     * @return pid - 上级标识
     */
    public String getPid() {
        return pid;
    }

    /**
     * 设置上级标识
     *
     * @param pid 上级标识
     */
    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

    /**
     * 获取地名简称
     *
     * @return sname - 地名简称
     */
    public String getSname() {
        return sname;
    }

    /**
     * 设置地名简称
     *
     * @param sname 地名简称
     */
    public void setSname(String sname) {
        this.sname = sname == null ? null : sname.trim();
    }

    /**
     * 获取等级
     *
     * @return level - 等级
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 设置等级
     *
     * @param level 等级
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 获取简编
     *
     * @return citycode - 简编
     */
    public String getCitycode() {
        return citycode;
    }

    /**
     * 设置简编
     *
     * @param citycode 简编
     */
    public void setCitycode(String citycode) {
        this.citycode = citycode == null ? null : citycode.trim();
    }

    /**
     * 获取邮政编码
     *
     * @return yzcode - 邮政编码
     */
    public String getYzcode() {
        return yzcode;
    }

    /**
     * 设置邮政编码
     *
     * @param yzcode 邮政编码
     */
    public void setYzcode(String yzcode) {
        this.yzcode = yzcode == null ? null : yzcode.trim();
    }

    /**
     * 获取组合名称
     *
     * @return mername - 组合名称
     */
    public String getMername() {
        return mername;
    }

    /**
     * 设置组合名称
     *
     * @param mername 组合名称
     */
    public void setMername(String mername) {
        this.mername = mername == null ? null : mername.trim();
    }

    /**
     * 获取经度
     *
     * @return Lng - 经度
     */
    public BigDecimal getLng() {
        return lng;
    }

    /**
     * 设置经度
     *
     * @param lng 经度
     */
    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    /**
     * 获取纬度
     *
     * @return Lat - 纬度
     */
    public BigDecimal getLat() {
        return lat;
    }

    /**
     * 设置纬度
     *
     * @param lat 纬度
     */
    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    /**
     * 获取名称拼音
     *
     * @return pingyin - 名称拼音
     */
    public String getPingyin() {
        return pingyin;
    }

    /**
     * 设置名称拼音
     *
     * @param pingyin 名称拼音
     */
    public void setPingyin(String pingyin) {
        this.pingyin = pingyin == null ? null : pingyin.trim();
    }
}