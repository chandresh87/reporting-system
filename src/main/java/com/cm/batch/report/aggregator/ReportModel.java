package com.cm.batch.report.aggregator;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/** @author chandresh.mishra */
@Data
@AllArgsConstructor
public class ReportModel {
  private String departmentName;
  private BigDecimal totalCost;
  private String baseCurrency;
}
