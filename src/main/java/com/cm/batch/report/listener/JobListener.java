package com.cm.batch.report.listener;

import com.cm.batch.report.aggregator.ReportModel;
import com.cm.batch.report.remote.CurrencyRemoteService;
import com.cm.batch.report.writers.DocumentGenerator;
import com.itextpdf.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.List;

@Component
@Slf4j
public class JobListener extends JobExecutionListenerSupport {

  private final DocumentGenerator documentGenerator;
  private final String pdfLocation;
  private final CurrencyRemoteService currencyRemoteService;

  public JobListener(
      DocumentGenerator documentGenerator,
      @Value("${report.file.location}") String pdfLocation,
      CurrencyRemoteService currencyRemoteService) {
    this.documentGenerator = documentGenerator;
    this.pdfLocation = pdfLocation;
    this.currencyRemoteService = currencyRemoteService;
  }

  @Override
  public void beforeJob(JobExecution jobExecution) {
    log.info(
        "{} Job Started for base Currency {}",
        jobExecution.getJobInstance().getJobName(),
        jobExecution.getExecutionContext().get("baseCurrency"));
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    log.info(
        "{} Job Finished with status {}",
        jobExecution.getJobInstance().getJobName(),
        jobExecution.getStatus());
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      List<ReportModel> reportModelList =
          (List<ReportModel>) jobExecution.getExecutionContext().get("reportModelList");
      log.info("Report model {}", reportModelList);

      try {
        documentGenerator.generateDocument(pdfLocation, reportModelList);
      } catch (FileNotFoundException | DocumentException e) {
        log.error("Error while generating the report ", e);
        jobExecution.setStatus(BatchStatus.FAILED);
        throw new DocumentGenerationException("Error while generating the report");
      } finally {
        currencyRemoteService.evictAllCacheValues();
      }
    }
  }
}
