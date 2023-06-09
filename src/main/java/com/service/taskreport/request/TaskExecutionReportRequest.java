package com.service.taskreport.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
public class TaskExecutionReportRequest {
  private Integer id;

  @NotNull(message = "taskId should not be null")
  private Integer taskId;

  private List<TaskStepExecutionReportRequest> taskStepExecutionReports;

  @NotNull(message = "startDateTime should not be null")
  private Timestamp startDateTime;

  private Timestamp endDateTime;
  private String errorMessage;
}
