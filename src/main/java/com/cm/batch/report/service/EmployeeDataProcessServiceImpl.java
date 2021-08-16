package com.cm.batch.report.service;

import com.cm.batch.report.remote.CurrencyRatesDTO;
import com.cm.batch.report.remote.CurrencyRemoteService;
import com.cm.batch.report.service.bos.DepartmentDataBO;
import com.cm.batch.report.service.exceptions.ReportServiceException;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** @author chandresh.mishra */
@Service
@StepScope
public class EmployeeDataProcessServiceImpl implements EmployeeDataProcessService {

  private final CurrencyRemoteService currencyRemoteService;
  private final String baseCurrency;

  public EmployeeDataProcessServiceImpl(
      CurrencyRemoteService currencyRemoteService,
      @Value("#{jobParameters['baseCurrency']}") String baseCurrency) {
    this.currencyRemoteService = currencyRemoteService;
    this.baseCurrency = baseCurrency;
  }

  @Override
  public DepartmentDataBO convertCostInBaseCurrency(DepartmentDataBO departmentDataBO) {
    CurrencyRatesDTO currencyRates = currencyRemoteService.getCurrencyRates(baseCurrency);

    if (null == currencyRates || currencyRates.getRates().isEmpty()) {
      throw new ReportServiceException("No data returned from remote currency service");
    }
    BigDecimal rate = currencyRates.getRates().get(departmentDataBO.getCurrency());
    BigDecimal convertedRate =
        departmentDataBO.getTotalCost().divide(rate, 4, RoundingMode.CEILING);
    departmentDataBO.setTotalCost(convertedRate);

    return departmentDataBO;
  }
}
