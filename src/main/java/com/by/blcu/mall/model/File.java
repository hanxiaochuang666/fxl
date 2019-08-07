package com.by.blcu.mall.model;

import java.util.Date;
import javax.persistence.*;

public class File {
    /**
     * 文件ID
     */
    @Id
    @Column(name = "file_id")
    private String fileId;

    /**
     * 商品ID
     */
    @Column(name = "commodity_id")
    private String commodityId;

    /**
     * 文件名
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * 文件路径
     */
    @Column(name = "file_path")
    private String filePath;

    /**
     * 创建时间
     */
    @Column(name = "file_time")
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
    @Column(name = "file_size")
    private String fileSize;

    /**
     * 获取文件ID
     *
     * @return file_id - 文件ID
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * 设置文件ID
     *
     * @param fileId 文件ID
     */
    public void setFileId(String fileId) {
        this.fileId = fileId == null ? null : fileId.trim();
    }

    /**
     * 获取商品ID
     *
     * @return commodity_id - 商品ID
     */
    public String getCommodityId() {
        return commodityId;
    }

    /**
     * 设置商品ID
     *
     * @param commodityId 商品ID
     */
    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId == null ? null : commodityId.trim();
    }

    /**
     * 获取文件名
     *
     * @return file_name - 文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置文件名
     *
     * @param fileName 文件名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    /**
     * 获取文件路径
     *
     * @return file_path - 文件路径
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * 设置文件路径
     *
     * @param filePath 文件路径
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    /**
     * 获取创建时间
     *
     * @return file_time - 创建时间
     */
    public Date getFileTime() {
        return fileTime;
    }

    /**
     * 设置创建时间
     *
     * @param fileTime 创建时间
     */
    public void setFileTime(Date fileTime) {
        this.fileTime = fileTime;
    }

    /**
     * 获取是否删除(删除：0，未删除：1)
     *
     * @return isdelete - 是否删除(删除：0，未删除：1)
     */
    public Integer getIsdelete() {
        return isdelete;
    }

    /**
     * 设置是否删除(删除：0，未删除：1)
     *
     * @param isdelete 是否删除(删除：0，未删除：1)
     */
    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

    /**
     * 获取是否有效(无效：0，有效：1)
     *
     * @return isvalidity - 是否有效(无效：0，有效：1)
     */
    public Integer getIsvalidity() {
        return isvalidity;
    }

    /**
     * 设置是否有效(无效：0，有效：1)
     *
     * @param isvalidity 是否有效(无效：0，有效：1)
     */
    public void setIsvalidity(Integer isvalidity) {
        this.isvalidity = isvalidity;
    }

    /**
     * 获取存储文件名
     *
     * @return _file_size - 文件大小
     */
    public String getFileSize() {
        return fileSize;
    }

    /**
     * 设置文件大小
     *
     * @param fileSize 文件大小
     */
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize == null ? null : fileSize.trim();
    }
}