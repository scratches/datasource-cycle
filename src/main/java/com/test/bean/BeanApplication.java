package com.test.bean;

import javax.sql.DataSource;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.SimpleThreadScope;

@SpringBootApplication
public class BeanApplication {

	@Bean
	public static BeanFactoryPostProcessor fooScope() {
		return beanFactory -> {
			beanFactory.registerScope("foo", new SimpleThreadScope());
		};
	}

	@Bean
	@Scope("foo")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource(DataSourceProperties properties) {
		return DataSourceBuilder.create().url(properties.determineUrl())
				.type(org.apache.tomcat.jdbc.pool.DataSource.class).build();
	}

	public static void main(String[] args) {
		SpringApplication.run(BeanApplication.class, args);
	}

}