package com.cm.batch.report.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/** @author chandresh.mishra */
public interface EmployeeRepository extends PagingAndSortingRepository<EmployeeEntity, Integer> {

  @Query(
      "SELECT new com.cm.batch.report.repositories.DepartmentDTO(e.department, SUM(e.cost) , e.currency) FROM EmployeeEntity e group by  e.department, e.currency HAVING e.department = :deptName")
  Page<DepartmentDTO> getDepartmentAggregateData(String deptName, Pageable pageable);

  @Query("SELECT DISTINCT e.department FROM EmployeeEntity e")
  List<String> getDistinctDepartment();
}
