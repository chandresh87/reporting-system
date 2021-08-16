package com.cm.batch.report.config;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

  private final DataSource dataSource;
  private final PlatformTransactionManager transactionManager;
  private final TaskExecutor taskExecutor;

  public BatchConfiguration(
      DataSource dataSource,
      PlatformTransactionManager transactionManager,
      TaskExecutor taskExecutor) {
    this.dataSource = dataSource;
    this.transactionManager = transactionManager;
    this.taskExecutor = taskExecutor;
  }

  @Override
  protected JobRepository createJobRepository() throws Exception {

    JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
    factoryBean.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
    factoryBean.setTransactionManager(transactionManager);
    factoryBean.setDataSource(dataSource);
    factoryBean.afterPropertiesSet();
    return factoryBean.getObject();
  }

  @Override
  public PlatformTransactionManager getTransactionManager() {
    return this.transactionManager;
  }

  @Override
  public JobExplorer createJobExplorer() throws Exception {

    JobExplorerFactoryBean explorerFactoryBean = new JobExplorerFactoryBean();
    explorerFactoryBean.setDataSource(dataSource);
    explorerFactoryBean.afterPropertiesSet();
    return explorerFactoryBean.getObject();
  }

  @Override
  protected JobLauncher createJobLauncher() throws Exception {
    SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
    jobLauncher.setJobRepository(createJobRepository());
    jobLauncher.setTaskExecutor(taskExecutor);
    jobLauncher.afterPropertiesSet();
    return jobLauncher;
  }
}
