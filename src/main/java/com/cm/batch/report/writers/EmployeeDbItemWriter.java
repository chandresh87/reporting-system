package com.cm.batch.report.writers;

import com.cm.batch.report.service.bos.DepartmentDataBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** @author chandresh.mishra */
@Component
@Slf4j
@StepScope
public class EmployeeDbItemWriter implements ItemWriter<DepartmentDataBO>, StepExecutionListener {

  private StepExecution stepExecution;

  @Override
  public void beforeStep(StepExecution stepExecution) {
    this.stepExecution = stepExecution;
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    return stepExecution.getExitStatus();
  }

  @Override
  public void write(List<? extends DepartmentDataBO> items) {
    Map<String, BigDecimal> totalCostSumMap =
        items.stream()
            .collect(
                Collectors.groupingBy(
                    DepartmentDataBO::getName,
                    Collectors.reducing(
                        BigDecimal.ZERO, DepartmentDataBO::getTotalCost, BigDecimal::add)));

    totalCostSumMap.forEach(
        (deptName, cost) -> {
          log.info(
              "Writing to stepExecution {} {} {} {}",
              deptName,
              cost,
              stepExecution,
              stepExecution.getExecutionContext().entrySet());
          getAndSetStepExecution(deptName, cost);
        });
  }

  private void getAndSetStepExecution(String deptName, BigDecimal cost) {
    Object costFromPreviousIteration = stepExecution.getExecutionContext().get(deptName);
    if (null != costFromPreviousIteration) {
      BigDecimal costTemp = (BigDecimal) costFromPreviousIteration;

      stepExecution.getExecutionContext().put(deptName, costTemp.add(cost));
    } else {
      stepExecution.getExecutionContext().put(deptName, cost);
    }
  }
}
