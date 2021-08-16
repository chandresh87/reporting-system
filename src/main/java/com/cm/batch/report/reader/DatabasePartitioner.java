package com.cm.batch.report.reader;

import com.cm.batch.report.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/** @author chandresh.mishra */
@Component
@Slf4j
public class DatabasePartitioner implements Partitioner {
  private static final String PARTITION_KEY = "partition";
  private final EmployeeRepository employeeRepository;

  public DatabasePartitioner(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @Override
  public Map<String, ExecutionContext> partition(int gridSize) {
    List<String> distinctDepartment = employeeRepository.getDistinctDepartment();
    Map<String, ExecutionContext> result = new HashMap<>();
    AtomicInteger count = new AtomicInteger(0);
    log.info("Department found {}", distinctDepartment);
    distinctDepartment.forEach(
        deptName -> {
          ExecutionContext value = new ExecutionContext();
          value.put("deptName", deptName);
          result.put(PARTITION_KEY + count.incrementAndGet(), value);
        });
    return result;
  }
}
