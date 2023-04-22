package com.service.taskreport.request;

import java.sql.Timestamp;
import java.util.List;

public class TaskExecutionReportRequest {
  private Integer id;
  private Integer taskId;
  private List<TaskStepExecutionReportRequest> taskStepExecutionReports;
  private Timestamp startDateTime;
  private Integer executionTimeSeconds;
  private String errorMessage;
}
