package com.cm.batch.report.config;

import com.cm.batch.report.repositories.DepartmentDTO;
import com.cm.batch.report.service.bos.DepartmentDataBO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.StepExecutionAggregator;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

/** @author chandresh.mishra */
@Configuration
public class JobConfiguration {

  private final StepBuilderFactory stepBuilderFactory;
  private final JobBuilderFactory jobBuilderFactory;
  private final RepositoryItemReader<DepartmentDTO> employeeItemReader;
  private final CompositeItemProcessor<DepartmentDTO, DepartmentDataBO> compositeItemProcessor;
  private final DatabaseProperty databaseProperty;
  private final ItemWriter<DepartmentDataBO> departmentItemWriter;
  private final TaskExecutor taskExecutor;
  private final Partitioner partitioner;
  private final StepExecutionAggregator stepExecutionAggregator;
  private final JobExecutionListener jobListener;

  public JobConfiguration(
      StepBuilderFactory stepBuilderFactory,
      JobBuilderFactory jobBuilderFactory,
      RepositoryItemReader<DepartmentDTO> employeeItemReader,
      CompositeItemProcessor<DepartmentDTO, DepartmentDataBO>
          departmentDataBOCompositeItemProcessor,
      DatabaseProperty databaseProperty,
      ItemWriter<DepartmentDataBO> departmentItemWriter,
      Partitioner partitioner,
      TaskExecutor taskExecutor,
      StepExecutionAggregator stepExecutionAggregator,
      JobExecutionListener jobListener) {
    this.stepBuilderFactory = stepBuilderFactory;
    this.jobBuilderFactory = jobBuilderFactory;
    this.employeeItemReader = employeeItemReader;
    this.compositeItemProcessor = departmentDataBOCompositeItemProcessor;
    this.databaseProperty = databaseProperty;
    this.departmentItemWriter = departmentItemWriter;
    this.partitioner = partitioner;
    this.taskExecutor = taskExecutor;
    this.stepExecutionAggregator = stepExecutionAggregator;
    this.jobListener = jobListener;
  }

  @Bean
  public Step readDatabaseStep(@Value("${batch.chunkSize}") int chunkSize) {
    return this.stepBuilderFactory
        .get("read-employee-data-step")
        .<DepartmentDTO, DepartmentDataBO>chunk(chunkSize)
        .reader(employeeItemReader)
        .processor(compositeItemProcessor)
        .writer(departmentItemWriter)
        .build();
  }

  @Bean
  public Step masterStep(@Qualifier("readDatabaseStep") Step readDatabaseStep) {
    return stepBuilderFactory
        .get("partition-step")
        .partitioner("read-employee-data-step", partitioner)
        .step(readDatabaseStep)
        .gridSize(databaseProperty.getMaxPoolSize())
        .taskExecutor(taskExecutor)
        .aggregator(stepExecutionAggregator)
        .build();
  }

  @Bean
  public Job job(@Qualifier("masterStep") Step masterStep) {
    return jobBuilderFactory.get("reporting-job").start(masterStep).listener(jobListener).build();
  }
}
