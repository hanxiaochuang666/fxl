package com.by.blcu.resource.dao;

import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.resource.dto.LearnActive;

import java.util.Map;

public interface ILearnActiveDao extends IBaseDao {
    /**
     * 幂等插入
     * @param learnActive
     * @return
     */
    long insertNoExists(LearnActive learnActive);

    /**
     * 查询学习进度，需要根据目录状态筛选生效的目录
     * @param map
     * @param <T>
     * @return
     */
    <T> long selectLearnActiveCount(Map<String, Object> map);
}