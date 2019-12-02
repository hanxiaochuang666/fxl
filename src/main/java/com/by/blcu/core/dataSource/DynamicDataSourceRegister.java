package com.by.blcu.core.dataSource;

import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源注册
 */
@Slf4j
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar,EnvironmentAware {

    //数据源配置前缀
    private static final String DB_DEFAULT_VALUE="spring.datasource";
    private static final String DATASOURCE_TYPE_DEFAULT="com.alibaba.druid.pool.DruidDataSource";
    //默认数据源
    private DataSource defaultDataSource;

    //用户自定义数据源
    private Map<String,DataSource> slaveDataSource=new HashMap<>();
    /**
     * 注册数据源
     * @param annotationMetadata
     * @param beanDefinitionRegistry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Map<String, Object> initMap = MapUtils.initMap();
        //1.添加默认数据源
        initMap.put("dataSource",defaultDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("dataSource");
        //2.添加所有的数据源
        initMap.putAll(slaveDataSource);
        for(String key:slaveDataSource.keySet()){
            DynamicDataSourceContextHolder.dataSourceIds.add(key);
        }
        //3.创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", initMap);
        //4.注册 - BeanDefinitionRegistry
        beanDefinitionRegistry.registerBeanDefinition("dataSource", beanDefinition);
    }

    /**
     * 获取配置项,并加载数据源配置
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        initDefaultDataSource(environment);
        initSlaveDataSource(environment);
    }

    /**
     * 初始化默认的数据源
     * @param environment
     */
    private void initDefaultDataSource(Environment environment){
        Map<String, Object> stringObjectMap = MapUtils.initMap();
        stringObjectMap.put("url",environment.getProperty(DB_DEFAULT_VALUE+".url"));
        stringObjectMap.put("username",environment.getProperty(DB_DEFAULT_VALUE+".username"));
        stringObjectMap.put("password",environment.getProperty(DB_DEFAULT_VALUE+".password"));
        stringObjectMap.put("driverClassName",environment.getProperty(DB_DEFAULT_VALUE+".driverClassName"));
        defaultDataSource=bulidDataSource(stringObjectMap);
    }

    /**
     * 初始化更多的数据源
     * @param environment
     */
    private void initSlaveDataSource(Environment environment){
        String property = environment.getProperty(DB_DEFAULT_VALUE + ".names");
        if(StringUtils.isEmpty(property))
            return;
        for(String s:property.split(",")){
            Map<String, Object> initMap = MapUtils.initMap("url", environment.getProperty(DB_DEFAULT_VALUE + "." + s + ".url"));
            initMap.put("username",environment.getProperty(DB_DEFAULT_VALUE + "." + s + ".username"));
            initMap.put("password",environment.getProperty(DB_DEFAULT_VALUE + "." + s +".password"));
            initMap.put("driverClassName",environment.getProperty(DB_DEFAULT_VALUE + "." + s +".driverClassName"));
            DataSource ds=bulidDataSource(initMap);
            slaveDataSource.put(s,ds);
        }
    }


    /**
     * 绑定数据源
     */
    private DataSource bulidDataSource(Map<String,Object> dataSourceMap){
        try {
            Class<? extends DataSource> dataSourceType=(Class<? extends DataSource>) Class.forName(DATASOURCE_TYPE_DEFAULT);
            String driverClassName = dataSourceMap.get("driverClassName").toString();
            String url = dataSourceMap.get("url").toString();
            String username = dataSourceMap.get("username").toString();
            String password = dataSourceMap.get("password").toString();
            // 自定义DataSource配置
            DataSourceBuilder factory = DataSourceBuilder.create().driverClassName(driverClassName).url(url)
                    .username(username).password(password).type(dataSourceType);
            return factory.build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
