package com.cm.batch.report.aggregator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.partition.support.DefaultStepExecutionAggregator;
import org.springframework.batch.core.partition.support.StepExecutionAggregator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** @author chandresh.mishra */
@Component
@Slf4j
public class DepartmentCostDataAggregator implements StepExecutionAggregator {

  private final StepExecutionAggregator delegate;

  public DepartmentCostDataAggregator() {
    this.delegate = new DefaultStepExecutionAggregator();
  }

  @Override
  public void aggregate(StepExecution result, Collection<StepExecution> executions) {
    Collection<StepExecution> updates = new ArrayList<>();
    List<ReportModel> reportModelList = new ArrayList<>();

    log.info("Aggregated step executions {}", executions);

    for (StepExecution stepExecution : executions) {

      String deptName = stepExecution.getExecutionContext().getString("deptName");
      BigDecimal totalCost = (BigDecimal) stepExecution.getExecutionContext().get(deptName);

      ReportModel reportModel =
          new ReportModel(
              deptName,
              totalCost,
              stepExecution.getJobExecution().getJobParameters().getString("baseCurrency"));
      reportModelList.add(reportModel);
      updates.add(stepExecution);
    }
    log.info("List of reportModel from aggregate result {}", reportModelList);
    result.getJobExecution().getExecutionContext().put("reportModelList", reportModelList);
    delegate.aggregate(result, updates);
  }
}
