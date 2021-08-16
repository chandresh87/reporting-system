package com.cm.batch.report.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** @author chandresh.mishra */
@ConfigurationProperties(prefix = "batch.datasource")
@Data
public class DatabaseProperty {

  private String url;
  private String username;
  private String password;
  private String driverClassName;
  private String dialect;
  private String poolName;
  private String showSql;
  private String timeZone;
  private String hbm2ddl;
  private int maxPoolSize;
  private int minIdlePoolSize;
}
