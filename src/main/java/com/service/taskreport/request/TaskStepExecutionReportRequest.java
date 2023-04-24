package com.service.taskreport.request;

import com.service.taskreport.enums.StatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class TaskStepExecutionReportRequest {
  private Integer id;

  @NotNull(message = "taskExecutionId should not be null")
  private Integer taskExecutionId;

  @NotNull(message = "stepName should not be null")
  private String stepName;

  private StatusEnum status;

  @NotNull(message = "startDateTime should not be null")
  private Timestamp startDateTime;

  private Timestamp endDateTime;
  private String errorMessage;
}
