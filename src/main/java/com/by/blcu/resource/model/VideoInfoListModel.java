package com.by.blcu.resource.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class VideoInfoListModel implements Serializable {
    private static final long serialVersionUID = 5789844891340015488L;

    private Integer videoInfoId;

    private String videoName;

    private String createUserName;

    private String createTimeStr;

    private Integer size;

    private String url;

    private Integer duration;

    private String bucketName;
}
