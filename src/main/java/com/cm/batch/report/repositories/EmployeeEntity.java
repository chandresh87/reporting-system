package com.cm.batch.report.repositories;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/** @author chandresh.mishra */
@Entity
@Data
@Table(name = "company.employee")
public class EmployeeEntity implements Serializable {

  private static final long serialVersionUID = -658018491202095257L;

  @Id
  @Column(name = "empid")
  private String employeeID;

  private String title;
  private String department;

  @Column(name = "monthly_cost")
  private BigDecimal cost;

  private String currency;
}
