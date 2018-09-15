package com.ppu.fmc;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "fmcEntityManagerFactory", transactionManagerRef = "fmcTransactionManager", basePackages = {
		"com.ppu.fmc.firepower.repo" })
public class FMCDBConfig {

	@Primary
	@Bean(name = "fmcDataSource")
	@ConfigurationProperties(prefix = "myfmc.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "fmcEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("fmcDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.ppu.fmc.firepower.domain").persistenceUnit("myfmc").build();
	}

	@Primary
	@Bean(name = "fmcTransactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("fmcEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}