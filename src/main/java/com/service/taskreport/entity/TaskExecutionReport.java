package com.service.taskreport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.sql.Timestamp;
import java.util.List;

@Entity
public class TaskExecutionReport extends AbstractEntity {
  public static final String TABLE_NAME = "task_execution_report";

  @Column(name = "task_id")
  private Integer taskId;

  @Column(name = "star_date_time")
  private Timestamp startDateTime;

  @Column(name = "end_date_time")
  private Timestamp endDateTime;

  @Column(name = "execution_time_seconds")
  private Integer executionTimeSeconds;

  @Column(name = "error_message")
  private String errorMessage;

  @Column(name = "task_step_execution_reports")
  private List<TaskStepExecutionReport> taskStepExecutionReports;
}
