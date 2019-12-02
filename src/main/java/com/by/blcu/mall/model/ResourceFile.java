package com.by.blcu.mall.model;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

@Data
public class ResourceFile implements Serializable {

    private static final long serialVersionUID = 7552369763287351342L;

    private String fileId;

    /**
     * 商品ID
     */
    private String commodityId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 创建时间
     */
    private Date fileTime;

    /**
     * 是否删除(删除：0，未删除：1)
     */
    private Integer isdelete;

    /**
     * 是否有效(无效：0，有效：1)
     */
    private Integer isvalidity;

    /**
     * 存储文件名
     */
    private String fileSize;

    /**
     * 审核状态（0：待审核；1::机审不通过；2：机审通过；3：人审不通过；4：人审通过）
     */
    private Integer checkStatus;

    /**
     * 审核时间
     */
    private Date checkTime;

    /**
     * 审核人id
     */
    private Integer checkUser;

    /**
     * 审核意见
     */
    private String checkMsg;

    /**
     * 审核人ID
     */
    private String checkId;

    /**
     * 资源名称
     */
    private String title;

    /**
     * 资源ID
     */
    private Integer resourcesId;

}
