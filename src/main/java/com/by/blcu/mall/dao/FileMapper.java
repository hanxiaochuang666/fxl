package com.by.blcu.mall.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.mall.model.File;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FileMapper extends Dao<File> {

    <T> long selectResourcesCount(Map<String, Object> map);

    <T> List<T> selectResourcesList(Map<String, Object> map);

    File selectByFileId(@Param("fileId")String fileId);

}