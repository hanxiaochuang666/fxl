package com.by.blcu.resource.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class TestPaper {
    /**
	 *INTEGER
	 *试卷id
	 */
    private Integer testPaperId;

    /**
	 *VARCHAR
	 *试卷名称
	 */
    private String name;

	/**
	 * 结构目录
	 */
	private String catalogs;

    /**
	 *VARCHAR
	 *一级类目id
	 */
    private String categoryOne;

    /**
	 *VARCHAR
	 *二级类目id
	 */
    private String categoryTwo;

    /**
	 *INTEGER
	 *课程id
	 */
    private Integer courseId;

    /**
	 *TINYINT
	 *用途:0 测试，1作业
	 */
    private Integer useType;

    /**
	 *INTEGER
	 *0:可用；1:老师正在编辑；2：学生正在作答
	 */
    private Integer status;

    /**
	 *INTEGER
	 *时长(单位  分)
	 */
    private Integer time;

    /**
	 *DATE
	 *有效期开始时间
	 */
    private Date startTime;

    /**
	 *DATE
	 *有效期结束时间
	 */
    private Date endTime;

    /**
	 *TINYINT
	 *是否计分:0计分，1不计分
	 */
    private int isScore;

    /**
	 *INTEGER
	 *总分
	 */
    private Integer totalScore;

    /**
	 *INTEGER
	 *组卷类型（0:人工组卷；1:智能组卷）
	 */
    private Integer formType;

    /**
	 *VARCHAR
	 *导出文件path
	 */
    private String exportPath;

	/**
	 * 学生端导出path
	 */
	private String exportStuPath;

    /**
	 *DATE
	 *导出时间
	 */
    private Date exportTime;

    /**
	 *INTEGER
	 *机构id
	 */
    private String orgCode;

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


}