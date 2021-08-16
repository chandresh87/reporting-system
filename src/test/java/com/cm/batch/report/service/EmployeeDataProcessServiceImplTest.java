package com.cm.batch.report.service;

import com.cm.batch.report.remote.CurrencyRatesDTO;
import com.cm.batch.report.remote.CurrencyRemoteService;
import com.cm.batch.report.service.bos.DepartmentDataBO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/** @author chandresh.mishra */
@ExtendWith(MockitoExtension.class)
class EmployeeDataProcessServiceImplTest {

  private final String baseCurrency = "EUR";
  @Mock private CurrencyRemoteService currencyRemoteService;
  private EmployeeDataProcessService employeeDataProcessService;

  @BeforeEach
  void setUp() {
    employeeDataProcessService =
        new EmployeeDataProcessServiceImpl(currencyRemoteService, baseCurrency);
  }

  @Test
  void convertCostToCurrency() {
    CurrencyRatesDTO currencyRatesDTO = new CurrencyRatesDTO();

    Map<String, BigDecimal> rates = new HashMap<>();
    rates.put("GBP", new BigDecimal(0.846924));

    currencyRatesDTO.setRates(rates);
    when(currencyRemoteService.getCurrencyRates(anyString())).thenReturn(currencyRatesDTO);

    DepartmentDataBO departmentDataBO = new DepartmentDataBO();

    departmentDataBO.setCurrency("GBP");
    departmentDataBO.setTotalCost(new BigDecimal(100));
    departmentDataBO.setName("Sales");

    DepartmentDataBO dataBO =
        employeeDataProcessService.convertCostInBaseCurrency(departmentDataBO);
    assertEquals(118.0744, dataBO.getTotalCost().doubleValue());
  }
}
