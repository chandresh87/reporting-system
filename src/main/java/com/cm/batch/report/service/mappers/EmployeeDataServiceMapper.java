package com.cm.batch.report.service.mappers;

import com.cm.batch.report.repositories.DepartmentDTO;
import com.cm.batch.report.service.bos.DepartmentDataBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** @author chandresh.mishra */
@Mapper
public interface EmployeeDataServiceMapper {

  @Mapping(source = "currency", target = "currency")
  DepartmentDataBO departmentDataDTODepartmentDataBO(DepartmentDTO departmentDTO);
}
