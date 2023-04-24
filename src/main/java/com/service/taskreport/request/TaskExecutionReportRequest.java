package com.service.taskreport.request;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
public class TaskExecutionReportRequest {
  private Integer taskId;
  private List<TaskStepExecutionReportRequest> taskStepExecutionReports;
  private Timestamp startDateTime;
  private Timestamp endDateTime;
  private Integer executionTimeSeconds;
  private String errorMessage;
}
