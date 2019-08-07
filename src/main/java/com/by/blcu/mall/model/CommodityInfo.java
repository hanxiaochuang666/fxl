package com.by.blcu.mall.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "mall_commodity_info")
public class CommodityInfo {
    /**
     * 商品id
     */
    @Id
    @Column(name = "commodity_id")
    private String commodityId;

    /**
     * 课程分类id
     */
    @Column(name = "cc_id")
    private String ccId;

    /**
     * 课程名称
     */
    @Column(name = "course_name")
    private String courseName;

    /**
     * 商品状态（下架：0，上架：1）
     */
    @Column(name = "commodity_status")
    private Integer commodityStatus;

    /**
     * 价格
     */
    private Double price;

    /**
     * 删除状态（删除：0，未删除：1）
     */
    private Integer status;

    /**
     * 有效期(-1为永久有效)
     */
    private Integer validity;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 特色简介
     */
    private String introduce;

    /**
     * 优惠价
     */
    private Double preferential;

    /**
     * 购买人数
     */
    @Column(name = "persion_num")
    private Integer persionNum;

    /**
     * 课时
     */
    @Column(name = "class_time")
    private Integer classTime;

    /**
     * 开班时间
     */
    @Column(name = "open_class_time")
    private Date openClassTime;

    /**
     * 授课类型
     */
    @Column(name = "lesson_type")
    private Integer lessonType;

    /**
     * 主讲老师
     */
    private String lecturer;

    /**
     * 试听视频
     */
    @Column(name = "audition_video_path")
    private String auditionVideoPath;

    /**
     * 课程封面
     */
    @Column(name = "picture_path")
    private String picturePath;

    /**
     * 咨询方式
     */
    @Column(name = "contact_info")
    private String contactInfo;

    /**
     * 自动发送消息(不自动发送：0，自动发送：1)
     */
    @Column(name = "aoto_mail")
    private Integer aotoMail;

    /**
     * 课程简介
     */
    @Column(name = "course_introduce")
    private String courseIntroduce;

    /**
     * 课程id
     */
    @Column(name = "course_id")
    private String courseId;

    /**
     * 课程类型(单一课程：0，套餐：1)
     */
    @Column(name = "course_type")
    private Integer courseType;

    /**
     * 获取商品id
     *
     * @return commodity_id - 商品id
     */
    public String getCommodityId() {
        return commodityId;
    }

    /**
     * 设置商品id
     *
     * @param commodityId 商品id
     */
    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId == null ? null : commodityId.trim();
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
        this.ccId = ccId == null ? null : ccId.trim();
    }

    /**
     * 获取课程名称
     *
     * @return course_name - 课程名称
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * 设置课程名称
     *
     * @param courseName 课程名称
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName == null ? null : courseName.trim();
    }

    /**
     * 获取商品状态（下架：0，上架：1）
     *
     * @return commodity_status - 商品状态（下架：0，上架：1）
     */
    public Integer getCommodityStatus() {
        return commodityStatus;
    }

    /**
     * 设置商品状态（下架：0，上架：1）
     *
     * @param commodityStatus 商品状态（下架：0，上架：1）
     */
    public void setCommodityStatus(Integer commodityStatus) {
        this.commodityStatus = commodityStatus;
    }

    /**
     * 获取价格
     *
     * @return price - 价格
     */
    public Double getPrice() {
        return price;
    }

    /**
     * 设置价格
     *
     * @param price 价格
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * 获取删除状态（删除：0，未删除：1）
     *
     * @return status - 删除状态（删除：0，未删除：1）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置删除状态（删除：0，未删除：1）
     *
     * @param status 删除状态（删除：0，未删除：1）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取有效期(-1为永久有效)
     *
     * @return validity - 有效期(-1为永久有效)
     */
    public Integer getValidity() {
        return validity;
    }

    /**
     * 设置有效期(-1为永久有效)
     *
     * @param validity 有效期(-1为永久有效)
     */
    public void setValidity(Integer validity) {
        this.validity = validity;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * 获取特色简介
     *
     * @return introduce - 特色简介
     */
    public String getIntroduce() {
        return introduce;
    }

    /**
     * 设置特色简介
     *
     * @param introduce 特色简介
     */
    public void setIntroduce(String introduce) {
        this.introduce = introduce == null ? null : introduce.trim();
    }

    /**
     * 获取优惠价
     *
     * @return preferential - 优惠价
     */
    public Double getPreferential() {
        return preferential;
    }

    /**
     * 设置优惠价
     *
     * @param preferential 优惠价
     */
    public void setPreferential(Double preferential) {
        this.preferential = preferential;
    }

    /**
     * 获取购买人数
     *
     * @return persion_num - 购买人数
     */
    public Integer getPersionNum() {
        return persionNum;
    }

    /**
     * 设置购买人数
     *
     * @param persionNum 购买人数
     */
    public void setPersionNum(Integer persionNum) {
        this.persionNum = persionNum;
    }

    /**
     * 获取课时
     *
     * @return class_time - 课时
     */
    public Integer getClassTime() {
        return classTime;
    }

    /**
     * 设置课时
     *
     * @param classTime 课时
     */
    public void setClassTime(Integer classTime) {
        this.classTime = classTime;
    }

    /**
     * 获取开班时间
     *
     * @return open_class_time - 开班时间
     */
    public Date getOpenClassTime() {
        return openClassTime;
    }

    /**
     * 设置开班时间
     *
     * @param openClassTime 开班时间
     */
    public void setOpenClassTime(Date openClassTime) {
        this.openClassTime = openClassTime;
    }

    /**
     * 获取授课类型
     *
     * @return lesson_type - 授课类型
     */
    public Integer getLessonType() {
        return lessonType;
    }

    /**
     * 设置授课类型
     *
     * @param lessonType 授课类型
     */
    public void setLessonType(Integer lessonType) {
        this.lessonType = lessonType;
    }

    /**
     * 获取主讲老师
     *
     * @return lecturer - 主讲老师
     */
    public String getLecturer() {
        return lecturer;
    }

    /**
     * 设置主讲老师
     *
     * @param lecturer 主讲老师
     */
    public void setLecturer(String lecturer) {
        this.lecturer = lecturer == null ? null : lecturer.trim();
    }

    /**
     * 获取试听视频
     *
     * @return audition_video_path - 试听视频
     */
    public String getAuditionVideoPath() {
        return auditionVideoPath;
    }

    /**
     * 设置试听视频
     *
     * @param auditionVideoPath 试听视频
     */
    public void setAuditionVideoPath(String auditionVideoPath) {
        this.auditionVideoPath = auditionVideoPath == null ? null : auditionVideoPath.trim();
    }

    /**
     * 获取课程封面
     *
     * @return picture_path - 课程封面
     */
    public String getPicturePath() {
        return picturePath;
    }

    /**
     * 设置课程封面
     *
     * @param picturePath 课程封面
     */
    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath == null ? null : picturePath.trim();
    }

    /**
     * 获取咨询方式
     *
     * @return contact_info - 咨询方式
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * 设置咨询方式
     *
     * @param contactInfo 咨询方式
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo == null ? null : contactInfo.trim();
    }

    /**
     * 获取自动发送消息(不自动发送：0，自动发送：1)
     *
     * @return aoto_mail - 自动发送消息(不自动发送：0，自动发送：1)
     */
    public Integer getAotoMail() {
        return aotoMail;
    }

    /**
     * 设置自动发送消息(不自动发送：0，自动发送：1)
     *
     * @param aotoMail 自动发送消息(不自动发送：0，自动发送：1)
     */
    public void setAotoMail(Integer aotoMail) {
        this.aotoMail = aotoMail;
    }

    /**
     * 获取课程简介
     *
     * @return course_introduce - 课程简介
     */
    public String getCourseIntroduce() {
        return courseIntroduce;
    }

    /**
     * 设置课程简介
     *
     * @param courseIntroduce 课程简介
     */
    public void setCourseIntroduce(String courseIntroduce) {
        this.courseIntroduce = courseIntroduce == null ? null : courseIntroduce.trim();
    }

    /**
     * 获取课程id
     *
     * @return course_id - 课程id
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * 设置课程id
     *
     * @param courseId 课程id
     */
    public void setCourseId(String courseId) {
        this.courseId = courseId == null ? null : courseId.trim();
    }

    /**
     * 获取课程类型(单一课程：0，套餐：1)
     *
     * @return course_type - 课程类型(单一课程：0，套餐：1)
     */
    public Integer getCourseType() {
        return courseType;
    }

    /**
     * 设置课程类型(单一课程：0，套餐：1)
     *
     * @param courseType 课程类型(单一课程：0，套餐：1)
     */
    public void setCourseType(Integer courseType) {
        this.courseType = courseType;
    }
}