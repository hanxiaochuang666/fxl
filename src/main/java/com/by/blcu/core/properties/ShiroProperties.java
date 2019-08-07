package com.by.blcu.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {

    private String anonUrl;

    //token 默认有效期 1 天
    private Long jwtTimeOut = 86400L;


}
