package com.by.blcu.resource.service;

import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.resource.dto.LearnActive;

public interface ILearnActiveService extends IBaseService {
    /**
     * 幂等插入
     * @param learnActive
     * @return
     */
    long insertNoExists(LearnActive learnActive);
}