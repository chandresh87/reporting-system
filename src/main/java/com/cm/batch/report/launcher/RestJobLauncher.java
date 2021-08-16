package com.cm.batch.report.launcher;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Properties;

/** @author chandresh.mishra */
@RestController
public class RestJobLauncher {

  private final JobLauncher jobLauncher;

  private final Job job;

  public RestJobLauncher(JobLauncher jobLauncher, Job job) {
    this.jobLauncher = jobLauncher;
    this.job = job;
  }

  @GetMapping(path = "/run/{baseCurrency}")
  public Long runJob(@PathVariable String baseCurrency) throws Exception {

    JobParameters jobParameters = getJobParameters(baseCurrency);

    return this.jobLauncher.run(job, jobParameters).getJobId();
  }

  private JobParameters getJobParameters(String baseCurrency) {
    Properties properties = new Properties();
    properties.put("baseCurrency", baseCurrency);
    return new JobParametersBuilder(properties).addDate("currentDate", new Date()).toJobParameters();
  }
}
