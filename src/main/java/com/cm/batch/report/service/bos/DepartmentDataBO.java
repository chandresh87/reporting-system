package com.cm.batch.report.service.bos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** @author chandresh.mishra */
@Data
@NoArgsConstructor
public class DepartmentDataBO {

  private String name;
  private BigDecimal totalCost;
  private String currency;
}
