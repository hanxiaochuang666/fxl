package com.by.blcu;

import com.by.blcu.core.dataSource.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.web.context.request.RequestContextListener;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import javax.sql.DataSource;

@SpringBootApplication
@EnableSwagger2
@EnableTransactionManagement
@EnableCaching
//导入自实现的多数据源注册class
@Import(DynamicDataSourceRegister.class)
@EnableAsync
public class DemoApplication implements TransactionManagementConfigurer{

	@Resource(name="txManager1")
	private PlatformTransactionManager txManager1;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public RequestContextListener requestContextListener(){
		return new RequestContextListener();
	}

	// 创建事务管理器1
	@Bean(name = "txManager1")
	public PlatformTransactionManager txManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}


	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return txManager1;
	}
}
