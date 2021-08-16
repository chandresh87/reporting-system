package com.cm.batch.report.reader;

import com.cm.batch.report.repositories.DepartmentDTO;
import com.cm.batch.report.repositories.EmployeeEntity;
import com.cm.batch.report.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** @author chandresh.mishra */
@SpringBatchTest
@SpringBootTest
@ActiveProfiles({"test"})
@AutoConfigureWireMock(port = 0)
class EmployeeDbItemReaderTest {

  @Autowired private RepositoryItemReader<DepartmentDTO> departmentDTORepositoryItemReader;

  @Autowired private EmployeeRepository repository;

  public StepExecution getStepExecution() {
    StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
    stepExecution.getExecutionContext().put("deptName", "Sales");
    return stepExecution;
  }

  @BeforeEach
  void setUp() {
    EmployeeEntity employeeEntity = new EmployeeEntity();
    employeeEntity.setEmployeeID("91847");
    employeeEntity.setTitle("Associate");
    employeeEntity.setDepartment("Sales");
    employeeEntity.setCost(new BigDecimal(500));
    employeeEntity.setCurrency("USD");

    EmployeeEntity employeeEntity1 = new EmployeeEntity();
    employeeEntity1.setEmployeeID("10900");
    employeeEntity1.setTitle("Manager");
    employeeEntity1.setDepartment("Sales");
    employeeEntity1.setCost(new BigDecimal(1200));
    employeeEntity1.setCurrency("EUR");

    EmployeeEntity employeeEntity2 = new EmployeeEntity();
    employeeEntity2.setEmployeeID("36197");
    employeeEntity2.setTitle("Senior Manager");
    employeeEntity2.setDepartment("Sales");
    employeeEntity2.setCost(new BigDecimal(300000));
    employeeEntity2.setCurrency("JPY");

    EmployeeEntity employeeEntity3 = new EmployeeEntity();
    employeeEntity3.setEmployeeID("91468");
    employeeEntity3.setTitle("Associate");
    employeeEntity3.setDepartment("Administration");
    employeeEntity3.setCost(new BigDecimal(500));
    employeeEntity3.setCurrency("GBP");

    EmployeeEntity employeeEntity4 = new EmployeeEntity();
    employeeEntity4.setEmployeeID("07397");
    employeeEntity4.setTitle("Associate");
    employeeEntity4.setDepartment("Sales");
    employeeEntity4.setCost(new BigDecimal(500));
    employeeEntity4.setCurrency("USD");

    List<EmployeeEntity> employeeEntityList =
        Arrays.asList(
            employeeEntity, employeeEntity1, employeeEntity2, employeeEntity3, employeeEntity4);
    repository.saveAll(employeeEntityList);
  }

  @Test
  void employeeItemReader() throws Exception {
    DepartmentDTO departmentDTO = departmentDTORepositoryItemReader.read();
    assertEquals(
        new DepartmentDTO("Sales", new BigDecimal(300000.00).setScale(2), "JPY"), departmentDTO);
    DepartmentDTO departmentDTO1 = departmentDTORepositoryItemReader.read();
    assertEquals(
        new DepartmentDTO("Sales", new BigDecimal(1200.00).setScale(2), "EUR"), departmentDTO1);
    DepartmentDTO departmentDTO2 = departmentDTORepositoryItemReader.read();
    assertEquals(
        new DepartmentDTO("Sales", new BigDecimal(1000.00).setScale(2), "USD"), departmentDTO2);
  }
}
