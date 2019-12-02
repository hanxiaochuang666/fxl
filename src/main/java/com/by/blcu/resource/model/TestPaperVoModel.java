package com.by.blcu.resource.model;

import com.by.blcu.core.utils.DateUtils;
import com.by.blcu.resource.dto.TestPaper;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

public class TestPaperVoModel extends TestPaper implements Serializable {
    private static final long serialVersionUID = -4547713396856003772L;
    private String  startTimeStr;
    private String  endTimeStr;
    private String  exportTimeStr;
    private String  createTimeStr;
    private String  updateTimeStr;
    private String  courseName;


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public void setStartTime(Date startTime) {
        super.setStartTime(startTime);
        try {
            this.startTimeStr=DateUtils.date2String(startTime,DateUtils.DATE_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setEndTime(Date endTime) {
        super.setEndTime(endTime);
        try {
            this.endTimeStr=DateUtils.date2String(endTime,DateUtils.DATE_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setExportTime(Date exportTime) {
        super.setExportTime(exportTime);
        try {
            this.exportTimeStr=DateUtils.date2String(exportTime,DateUtils.TIME_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setCreateTime(Date createTime) {
        super.setCreateTime(createTime);
        try {
            this.createTimeStr=DateUtils.date2String(createTime,DateUtils.TIME_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void setUpdateTime(Date updateTime) {
        super.setUpdateTime(updateTime);
        try {
            this.updateTimeStr=DateUtils.date2String(updateTime,DateUtils.TIME_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public String getStartTimeStr(){
       return startTimeStr;

    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getExportTimeStr() {
        return exportTimeStr;
    }

    public void setExportTimeStr(String exportTimeStr) {
        this.exportTimeStr = exportTimeStr;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getUpdateTimeStr() {
        return updateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }
}

