package org.springframework.boot.autoconfigure.jdbc;

import com.zaxxer.hikari.HikariDataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@SpringBootConfiguration
@EnableConfigurationProperties(DataSourceProperties.class)
@Import({ DataSourceConfiguration.Hikari.class, DataSourceJmxConfiguration.class,
		DataSourceInitializationConfiguration.class})
public class BrokenApplication {

	@Component
	protected static class RefreshScopeConfiguration
			implements BeanDefinitionRegistryPostProcessor {

		@Override
		public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
				throws BeansException {
		}

		@Override
		public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
				throws BeansException {
			registry.registerBeanDefinition("fooScope",
					BeanDefinitionBuilder.genericBeanDefinition(FooScope.class)
							.setRole(BeanDefinition.ROLE_INFRASTRUCTURE)
							.getBeanDefinition());
		}
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
		SpringApplication.run(BrokenApplication.class, args);
	}

}

class FooScope implements org.springframework.beans.factory.config.Scope, BeanFactoryPostProcessor {

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		return objectFactory.getObject();
	}

	@Override
	public Object remove(String name) {
		return null;
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
	}

	@Override
	public Object resolveContextualObject(String key) {
		return null;
	}

	@Override
	public String getConversationId() {
		return null;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
			throws BeansException {
		beanFactory.registerScope("foo", this);
	}
	
}