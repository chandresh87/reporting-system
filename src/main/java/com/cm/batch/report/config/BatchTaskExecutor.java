package com.cm.batch.report.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/** @author chandresh.mishra */
@Configuration
public class BatchTaskExecutor {

  @Bean
  public TaskExecutor batchExecutor(
      @Value("${batch.threadPool.coreSize}") int coreSize,
      @Value("${batch.threadPool.maxSize}") int maxSize) {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(coreSize);
    executor.setMaxPoolSize(maxSize);
    executor.setThreadNamePrefix("default_task_executor_thread");
    executor.setDaemon(true);
    executor.initialize();
    return executor;
  }
}
