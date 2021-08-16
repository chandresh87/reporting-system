package com.cm.batch.report.reader;

import com.cm.batch.report.repositories.DepartmentDTO;
import com.cm.batch.report.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Collections;

/** @author chandresh.mishra */
@Configuration
@Slf4j
public class EmployeeDbItemReader {

  @Bean
  @StepScope
  public RepositoryItemReader<DepartmentDTO> employeeItemReader(
      EmployeeRepository repository,
      @Value("#{stepExecutionContext['deptName']}") String deptName,
      @Value("${batch.chunkSize}") int chunkSize) {

    log.info("Reading data for department {}", deptName);
    return new RepositoryItemReaderBuilder<DepartmentDTO>()
        .name("employeeItemReader")
        .arguments(Collections.singletonList(deptName))
        .methodName("getDepartmentAggregateData")
        .repository(repository)
        .pageSize(chunkSize)
        .sorts(Collections.singletonMap("cost", Sort.Direction.DESC))
        .build();
  }
}
