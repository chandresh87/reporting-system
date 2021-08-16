package com.cm.batch.report.service;

import com.cm.batch.report.service.bos.DepartmentDataBO;

/** @author chandresh.mishra */
public interface EmployeeDataProcessService {

  DepartmentDataBO convertCostInBaseCurrency(DepartmentDataBO departmentDataBO);
}
