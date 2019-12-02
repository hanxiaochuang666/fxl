package com.by.blcu.course.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel
public class CheckPassModel implements Serializable {
    private static final long serialVersionUID = 7110258476279308440L;

    @Min(value = 1,message = "课程 courseId 非法")
    @ApiModelProperty(value = "课程ID")
    private int courseId;

    /**
     * 0:目录，1:资源
     */
    @Range(min = 0,max=1,message = "0:目录，1:资源")
    @ApiModelProperty(value = "0:目录，1:资源")
    private Integer resourceType;

    @NotNull(message = "资源ID不能为空")
    @ApiModelProperty(value = "资源ID不能为空")
    private Integer resourceId;

    @Range(min = 3,max=4,message = "审核状态 3:不通过，4：通过")
    @ApiModelProperty(value = "审核状态 3:不通过，4：通过")
    private Integer checkStatus;
}
