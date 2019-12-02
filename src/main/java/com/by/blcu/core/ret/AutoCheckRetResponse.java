package com.by.blcu.core.ret;

import lombok.Data;

import java.io.Serializable;

@Data
public class AutoCheckRetResponse<T> implements Serializable {
    private static final long serialVersionUID = -8579270263381400871L;

    private int code;
    private String taskId;
    private String dataId;
    private String msg;
    private T results;
}
