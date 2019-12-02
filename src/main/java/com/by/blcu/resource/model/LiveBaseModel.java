package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class LiveBaseModel implements Serializable {

    private static final long serialVersionUID = 676628372994398133L;
    @ApiModelProperty(value = "应用id",hidden = true)
    private String appId;

    @ApiModelProperty(value = "应用appKey",hidden = true)
    private String appKey;

    @ApiModelProperty(value = "签名结果串",hidden = true)
    private String signature;

    @ApiModelProperty(value = "请求的时间戳。日期格式按照ISO8601标准表示，并需要使用 UTC 时间。格式为：YYYY-MM-DDThh:mm:ssZ 例如，2014-05-26T12:00:00Z（为北京时间 2014 年 5 月 26 日 12 点 0 分 0 秒）。",hidden = true)
    private String timestamp;

    @ApiModelProperty(value = "唯一随机数，用于防止网络重放攻击。用户在不同请求间要使用不同的随机数值",hidden = true)
    private String signatureNonce;
}
