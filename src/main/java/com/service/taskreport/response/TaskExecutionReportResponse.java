package com.service.taskreport.response;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class TaskExecutionReportResponse {
  private Integer id;
  private Integer taskId;
  private List<TaskStepExecutionReportResponse> taskStepExecutionReports;
  private Timestamp startDateTime;
  private Integer executionTimeSeconds;
  private String errorMessage;
}
