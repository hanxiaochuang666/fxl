package com.by.blcu.core.configurer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class RoleListConfigurer {
    public static List<String> ROLE_LIST_CODE=new ArrayList<>();

    @Value("${blcu.roleList}")
    private String roleList;

    @PostConstruct
    public void getRoleList(){
        String[] split = this.roleList.split(";");
        ROLE_LIST_CODE = Arrays.asList(split);
    }
}
