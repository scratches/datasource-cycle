package com.test.bean;

import com.zaxxer.hikari.HikariDataSource;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.SimpleThreadScope;

@SpringBootApplication// (exclude= {HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
public class BeanApplication {

	@Bean
	public static BeanFactoryPostProcessor fooScope() {
		return beanFactory -> {
			beanFactory.registerScope("foo", new SimpleThreadScope());
		};
	}

	@Bean
	@Scope("foo")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariDataSource dataSource(DataSourceProperties properties) {
		HikariDataSource dataSource = properties.initializeDataSourceBuilder()
				.type(HikariDataSource.class).build();
		return dataSource;
	}

	public static void main(String[] args) {
		SpringApplication.run(BeanApplication.class, args);
	}

}