package com.cm.batch.report.remote;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/** @author chandresh.mishra */
@Data
public class CurrencyRatesDTO implements Serializable {

  private static final long serialVersionUID = -569586102393861391L;

  public String base;
  public LocalDate date;
  public Map<String, BigDecimal> rates;
}
