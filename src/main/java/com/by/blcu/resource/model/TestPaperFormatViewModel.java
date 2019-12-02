package com.by.blcu.resource.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "TestPaperFormatViewModel",description = "保存试卷组成请求参数")
public class TestPaperFormatViewModel implements Serializable {
    private static final long serialVersionUID = -5407662019301062052L;
    @ApiModelProperty(value = "试卷id")
    private Integer testPaperId;
    @ApiModelProperty(value = "知识点id，多个使用;分割")
    private String  konwledges;
    @ApiModelProperty(value = "组成内容list")
    private List<PaperFormatInfo> PaperFormatInfoLst;
    /**
     * 更新者
     */
    private Integer updateUser;
}

