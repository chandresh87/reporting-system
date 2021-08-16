package com.cm.batch.report.writers;

import com.cm.batch.report.service.bos.DepartmentDataBO;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.MetaDataInstanceFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** @author chandresh.mishra */
class EmployeeDbItemWriterTest {

  private final EmployeeDbItemWriter employeeDbItemWriter = new EmployeeDbItemWriter();

  @Test
  void write() throws Exception {
    StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
    employeeDbItemWriter.beforeStep(stepExecution);
    List<DepartmentDataBO> departmentDataBOS = new ArrayList<>();
    DepartmentDataBO departmentDataBO = new DepartmentDataBO();

    departmentDataBO.setCurrency("EUR");
    departmentDataBO.setTotalCost(new BigDecimal(100));
    departmentDataBO.setName("Sales");

    DepartmentDataBO departmentDataBO1 = new DepartmentDataBO();

    departmentDataBO1.setCurrency("EUR");
    departmentDataBO1.setTotalCost(new BigDecimal(200));
    departmentDataBO1.setName("Sales");
    departmentDataBOS.add(departmentDataBO);
    departmentDataBOS.add(departmentDataBO1);

    employeeDbItemWriter.write(departmentDataBOS);
    BigDecimal sales = (BigDecimal) stepExecution.getExecutionContext().get("Sales");
    assertEquals(300, sales.doubleValue());
  }
}
