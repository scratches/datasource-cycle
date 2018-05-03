package org.springframework.boot.autoconfigure.jdbc;

import com.zaxxer.hikari.HikariDataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

@SpringBootConfiguration
@EnableConfigurationProperties(DataSourceProperties.class)
@Import({ DataSourceConfiguration.Hikari.class,
		DataSourceInitializationConfiguration.class, DataSourceJmxConfiguration.class })
public class AnotherNotBrokenApplication {

	@Bean
	public static FooScope fooScope() {
		return new FooScope();
	}

	@Bean
	@Scope("foo")
	@ConfigurationProperties(prefix = "spring.datasource.another")
	public HikariDataSource dataSource(DataSourceProperties properties) {
		HikariDataSource dataSource = properties.initializeDataSourceBuilder()
				.type(HikariDataSource.class).build();
		return dataSource;
	}

	public static void main(String[] args) {
		SpringApplication.run(AnotherNotBrokenApplication.class, args);
	}

}
