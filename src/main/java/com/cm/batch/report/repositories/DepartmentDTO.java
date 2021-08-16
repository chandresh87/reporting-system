package com.cm.batch.report.repositories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** @author chandresh.mishra */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {

  private String name;
  private BigDecimal totalCost;
  private String currency;
}
