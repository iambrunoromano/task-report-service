package com.service.taskreport.request;

import com.service.taskreport.enums.StatusEnum;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class TaskStepExecutionReportRequest {
  private Integer taskExecutionId;
  private String stepName;
  private StatusEnum status;
  private Timestamp startDateTime;
  private Timestamp endDateTime;
  private Integer executionTimeSeconds;
  private String errorMessage;
}
