package com.by.blcu.course.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class Course {
    /**
	 *INTEGER
	 *课程id
	 */
    private Integer courseId;

    /**
	 *VARCHAR
	 *课程名称
	 */
    private String name;

    /**
	 *VARCHAR
	 *类目一级id
	 */
    private String categoryOne;

    /**
	 *INTEGER
	 *状态(0:保存1:提交审核2::审核通过3:审核未通过)
	 */
    private Integer status;

    /**
	 *VARCHAR
	 *类目二级id
	 */
    private String categoryTwo;

    /**
	 *INTEGER
	 *机构id
	 */
    private String orgCode;

	/**
	 *TIMESTAMP
	 *提交时间
	 */
	private Date commitTime;

    /**
	 *INTEGER
	 *审核人
	 */
    private Integer examineUser;

    /**
	 *TIMESTAMP
	 *审核时间
	 */
    private Date examineTime;

    /**
	 *VARCHAR
	 *审核意见
	 */
    private String examineContext;

    /**
	 *INTEGER
	 *创建者id
	 */
    private Integer createUser;

    /**
	 *TIMESTAMP
	 *创建时间
	 */
    private Date createTime;

    /**
	 *INTEGER
	 *更新者id
	 */
    private Integer updateUser;

    /**
	 *TIMESTAMP
	 *更新时间
	 */
    private Date updateTime;

    /**
	 *VARCHAR
	 *备用1
	 */
    private String bak1;

    /**
	 *VARCHAR
	 *备用2
	 */
    private String bak2;

    /**
	 *VARCHAR
	 *备用3
	 */
    private String bak3;

}