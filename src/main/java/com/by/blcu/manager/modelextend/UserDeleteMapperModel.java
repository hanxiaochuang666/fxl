package com.by.blcu.manager.modelextend;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

/**
 * 用户删除类
 */
public class UserDeleteMapperModel {
    /**
     * 用户Id列表
     */
    private List<String> userIdList;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人
     */
    private String modifyBy;

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }
}
