package com.by.blcu.core.configurer;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author:
 * @Date:
 * @Description:
 * @Modify By:
 */
@Component
@Data
public class FdfsConfig {
    @Value("${fdfs.tracker-list}")
    private String trackerList;

    @Value("${fdfs.host}")
    private String host;

    @Value("${fdfs.part}")
    private String part;

}
