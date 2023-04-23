package com.service.taskreport.response;

import com.service.taskreport.enums.StatusEnum;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
public class TaskExecutionReportResponse {
  private Integer id;
  private Integer taskId;
  private List<TaskStepExecutionReportResponse> taskStepExecutionReports;
  private Timestamp startDateTime;
  private Timestamp endDateTime;
  private Integer executionTimeSeconds;
  private String errorMessage;
  private StatusEnum status;
}
