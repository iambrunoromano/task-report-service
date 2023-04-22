package com.service.taskreport.response;

import com.service.taskreport.enums.StatusEnum;

import java.sql.Timestamp;

public class TaskStepExecutionReportResponse {
  private Integer id;
  private Integer taskExecutionId;
  private String stepName;
  private StatusEnum status;
  private Timestamp startDateTime;
  private Timestamp endDateTime;
  private Integer executionTimeSeconds;
  private String errorMessage;
}
