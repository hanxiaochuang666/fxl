package com.by.blcu.course.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class KnowledgePointsModel implements Serializable {

    private static final long serialVersionUID = -6111185596217779710L;

    @Excel(name = "序号")
    private String order;

    @Excel(name = "课程名称")
    private String className;

    @Excel(name = "章")
    private String chapter;

    @Excel(name = "章助记码")
    private Integer chapterMnemonicCode;

    @Excel(name = "节")
    private String section;

    @Excel(name = "节助记码")
    private Integer sectionMnemonicCode;

    @Excel(name = "课时")
    private String knowledgePoints;

    @Excel(name = "课时助记码")
    private Integer pointsMnemonicCode;

    @Excel(name = "资源名称")
    private String resourceName;

    @Excel(name = "资源类型")
    private String resourceType;

}
