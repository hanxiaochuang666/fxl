package com.by.blcu.resource.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class Resources {
    /**
	 *INTEGER
	 *资源id
	 */
    private Integer resourcesId;

    /**
	 *VARCHAR
	 *标题（讨论问答）
	 */
    private String title;

    /**
	 *INTEGER
	 *资源类型（0：测试；1：作业；2：视频；3：直播；4：讨论；5：问答；6文档；7文本；8资料）
	 */
    private Integer type;

    /**
	 *INTEGER
	 *机构id
	 */
    private String orgCode ;

    /**
	 *VARCHAR
	 *资源内容
	 */
    private String content;

    /**
	 *INTEGER
	 *创建者id
	 */
    private Integer createUser;

    /**
	 *TIMESTAMP
	 *创建时间
	 */
    private Date creayeTime;

    /**
	 *INTEGER
	 *修改者id
	 */
    private Integer updateUser;

    /**
	 *DATE
	 *修改时间
	 */
    private Date updateTime;

    /**
	 *INTEGER
	 *审核状态（0：待审核；1::机审不通过；2：机审通过；3：人审不通过；4：人审通过）
	 */
    private Integer checkStatus;

    /**
	 *TIMESTAMP
	 *审核时间
	 */
    private Date checkTime;

    /**
	 *INTEGER
	 *审核人id
	 */
    private Integer checkUser;

    /**
	 *VARCHAR
	 *审核意见
	 */
    private String checkMsg;

    /**
	 *VARCHAR
	 *审核id
	 */
    private String checkId;

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