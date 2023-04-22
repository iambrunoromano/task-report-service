package com.service.taskreport.request;

import com.service.taskreport.enums.StatusEnum;

import java.sql.Timestamp;

public class TaskStepExecutionReportRequest {
  private Integer id;
  private Integer taskExecutionId;
  private String stepName;
  private StatusEnum status;
  private Timestamp startDateTime;
  private Timestamp endDateTime;
  private Integer executionTimeSeconds;
  private String errorMessage;
}
