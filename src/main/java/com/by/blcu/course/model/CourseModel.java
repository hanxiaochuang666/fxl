package com.by.blcu.course.model;

import java.io.Serializable;
import java.util.Date;

public class CourseModel implements Serializable {

    private static final long serialVersionUID = -1594608954256984202L;

    /**
     *课程id
     */
    private Integer courseId;

    /**
     *课程名称
     */
    private String name;

    /**
     *类目一级id
     */
    private String categoryOne;

    /**
     *状态(0:保存1:提交审核2::审核通过3:审核未通过)
     */
    private Integer status;

    /**
     *类目二级id
     */
    private String categoryTwo;

    /**
     *机构id
     */
    private String orgCode;

    /**
     *审核人
     */
    private Integer examineUser;

    /**
     *审核时间
     */
    private Date examineTime;

    /**
     *审核意见
     */
    private String examineContext;

    /**
     *创建者id
     */
    private Integer createUser;

    /**
     *创建时间
     */
    private Date createTime;

    /**
     *更新者id
     */
    private Integer updateUser;

    /**
     *更新时间
     */
    private Date updateTime;

    /**
     *备用1
     */
    private String bak1;

    /**
     *备用2
     */
    private String bak2;

    /**
     *备用3
     */
    private String bak3;
}
