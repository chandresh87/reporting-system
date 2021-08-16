package com.cm.batch.report.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.HashMap;

/** @author chandresh.mishra */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = {"com.cm.batch.report.repositories"},
    transactionManagerRef = "batch-transaction-manager",
    entityManagerFactoryRef = "batchEntityManagerFactory")
@EntityScan(basePackages = "com.cm.batch.report.repositories")
@EnableConfigurationProperties(DatabaseProperty.class)
public class DatabaseConfiguration {

  @Bean(name = "batch-transaction-manager")
  @Primary
  public PlatformTransactionManager transactionManager(
      @Qualifier("batchEntityManagerFactory")
          LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
    JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
    jpaTransactionManager.setEntityManagerFactory(
        localContainerEntityManagerFactoryBean.getObject());

    return jpaTransactionManager;
  }

  @Bean("batch-datasource")
  @Primary
  public DataSource getDataSource(DatabaseProperty databaseProperty) {
    HikariConfig hikariConfig = getHikariConfig(databaseProperty);
    return new HikariDataSource(hikariConfig);
  }

  private HikariConfig getHikariConfig(DatabaseProperty databaseProperty) {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(databaseProperty.getUrl());
    config.setUsername(databaseProperty.getUsername());
    config.setPassword(databaseProperty.getPassword());
    config.setDriverClassName(databaseProperty.getDriverClassName());
    config.setMaximumPoolSize(databaseProperty.getMaxPoolSize());
    config.setMinimumIdle(databaseProperty.getMinIdlePoolSize());
    return config;
  }

  @Bean("batchEntityManagerFactory")
  @Primary
  public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
      @Qualifier("batch-datasource") DataSource dataSource, DatabaseProperty databaseProperty) {
    LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
        new LocalContainerEntityManagerFactoryBean();
    localContainerEntityManagerFactoryBean.setDataSource(dataSource);
    localContainerEntityManagerFactoryBean.setPackagesToScan("com.cm.batch.report.repositories");
    HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
    localContainerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);

    HashMap<String, Object> properties = getJpaPropertyMap(databaseProperty);
    localContainerEntityManagerFactoryBean.setJpaPropertyMap(properties);
    return localContainerEntityManagerFactoryBean;
  }

  private HashMap<String, Object> getJpaPropertyMap(DatabaseProperty databaseProperty) {
    HashMap<String, Object> properties = new HashMap<>();
    properties.put("hibernate.hbm2ddl.auto", databaseProperty.getHbm2ddl());
    properties.put("hibernate.show_sql", databaseProperty.getShowSql());
    properties.put("hibernate.jdbc.time_zone", databaseProperty.getTimeZone());
    return properties;
  }

  @Bean("batchEntityManger")
  @Primary
  public EntityManager entityManager(
      @Qualifier("batchEntityManagerFactory")
          LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
    return localContainerEntityManagerFactoryBean
        .getNativeEntityManagerFactory()
        .createEntityManager();
  }
}
