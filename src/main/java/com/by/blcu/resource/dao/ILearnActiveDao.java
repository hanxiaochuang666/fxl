package com.by.blcu.resource.dao;

import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.resource.dto.LearnActive;

public interface ILearnActiveDao extends IBaseDao {
    /**
     * 幂等插入
     * @param learnActive
     * @return
     */
    long insertNoExists(LearnActive learnActive);
}