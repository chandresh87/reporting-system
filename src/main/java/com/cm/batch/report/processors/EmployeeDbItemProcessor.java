package com.cm.batch.report.processors;

import com.cm.batch.report.repositories.DepartmentDTO;
import com.cm.batch.report.service.EmployeeDataProcessService;
import com.cm.batch.report.service.bos.DepartmentDataBO;
import com.cm.batch.report.service.mappers.EmployeeDataServiceMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.adapter.ItemProcessorAdapter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/** @author chandresh.mishra */
@Configuration
public class EmployeeDbItemProcessor {

  @Bean
  public ItemProcessor<DepartmentDTO, DepartmentDataBO> mapDepartmentDTOToBO(
      EmployeeDataServiceMapper employeeDataServiceMapper) {

    ItemProcessorAdapter<DepartmentDTO, DepartmentDataBO> dataBOItemProcessorAdapter =
        new ItemProcessorAdapter<>();
    dataBOItemProcessorAdapter.setTargetObject(employeeDataServiceMapper);
    dataBOItemProcessorAdapter.setTargetMethod("departmentDataDTODepartmentDataBO");
    return dataBOItemProcessorAdapter;
  }

  @Bean
  public ItemProcessor<DepartmentDataBO, DepartmentDataBO> updateCost(
      EmployeeDataProcessService employeeDataProcessService) {

    ItemProcessorAdapter<DepartmentDataBO, DepartmentDataBO> employeeDataServiceAdapter =
        new ItemProcessorAdapter<>();
    employeeDataServiceAdapter.setTargetObject(employeeDataProcessService);
    employeeDataServiceAdapter.setTargetMethod("convertCostInBaseCurrency");
    return employeeDataServiceAdapter;
  }

  @Bean
  public CompositeItemProcessor<DepartmentDTO, DepartmentDataBO>
      departmentDataBOCompositeItemProcessor(
          ItemProcessor<DepartmentDTO, DepartmentDataBO> mappingProcessor,
          ItemProcessor<DepartmentDataBO, DepartmentDataBO> updateCostItemProcessor) {

    CompositeItemProcessor<DepartmentDTO, DepartmentDataBO> departmentDataBOCompositeItemProcessor =
        new CompositeItemProcessor<>();
    departmentDataBOCompositeItemProcessor.setDelegates(
        Arrays.asList(mappingProcessor, updateCostItemProcessor));
    return departmentDataBOCompositeItemProcessor;
  }
}
