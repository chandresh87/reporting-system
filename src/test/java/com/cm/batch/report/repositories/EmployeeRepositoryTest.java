package com.cm.batch.report.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** @author chandresh.mishra */
@DataJpaTest
class EmployeeRepositoryTest {

  @Autowired private EmployeeRepository employeeRepository;

  @Test
  void getDepartmentAggregateData() {

    Pageable pageable = PageRequest.of(0, 10);

    Page<DepartmentDTO> sales = employeeRepository.getDepartmentAggregateData("Sales", pageable);

    assertEquals(3, sales.getTotalElements());
    DepartmentDTO departmentDTO = new DepartmentDTO("Sales", new BigDecimal("1000.00"), "USD");
    DepartmentDTO departmentDTO1 = new DepartmentDTO("Sales", new BigDecimal("1200.00"), "EUR");
    DepartmentDTO departmentDTO2 = new DepartmentDTO("Sales", new BigDecimal("300000.00"), "JPY");
    List<DepartmentDTO> departmentDTOS = sales.getContent();
    assertTrue(
        departmentDTOS.containsAll(Arrays.asList(departmentDTO, departmentDTO1, departmentDTO2)));
  }

  @Test
  void getDistinctDepartmentTest() {
    List<String> distinctDepartment = employeeRepository.getDistinctDepartment();
    assertTrue(distinctDepartment.containsAll(Arrays.asList("Sales", "Administration")));
  }

  @BeforeEach
  void setup() {
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
    employeeRepository.saveAll(employeeEntityList);
  }
}
