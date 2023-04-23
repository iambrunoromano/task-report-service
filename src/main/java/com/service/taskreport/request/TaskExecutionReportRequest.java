package com.service.taskreport.request;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class TaskExecutionReportRequest {
  private Integer taskId;
  private List<TaskStepExecutionReportRequest> taskStepExecutionReports;
  private Timestamp startDateTime;
  private Integer executionTimeSeconds;
  private String errorMessage;
}
