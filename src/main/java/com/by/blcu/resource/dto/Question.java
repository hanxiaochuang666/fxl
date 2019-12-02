package com.by.blcu.resource.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class Question {
    /**
	 *INTEGER
	 *试题id
	 */
    private Integer questionId;

    /**
	 *VARCHAR
	 *类目一级id
	 */
    private String categoryOne;

    /**
	 *VARCHAR
	 *类目二级id
	 */
    private String categoryTwo;

    /**
	 *INTEGER
	 *课程id
	 */
    private Integer courseId;

    /**
	 *VARCHAR
	 *知识点id,多个使用;分割
	 */
    private String knowledgePoints;

    /**
	 *INTEGER
	 *难度等级(0：无;1:易；2：中；3：难)
	 */
    private Integer difficultyLevel;

    /**
	 *INTEGER
	 *试题类型id
	 */
    private Integer questionType;

    /**
	 *VARCHAR
	 *题干
	 */
    private String questionBody;

    /**
	 *VARCHAR
	 *音频fileId
	 */
    private String questionSound;

    /**
	 *VARCHAR
	 *选项(复用与综合体，表示子题id，使用;分割)
	 */
    private String questionOpt;

    /**
	 *VARCHAR
	 *答案
	 */
    private String questionAnswer;

    /**
	 *VARCHAR
	 *解析
	 */
    private String questionResolve;

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

    /**
	 *INTEGER
	 *父试题id
	 */
    private Integer parentQuestionId;

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