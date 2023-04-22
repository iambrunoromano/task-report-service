package com.service.taskreport.entity;

import com.service.taskreport.enums.StatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = TaskStepExecutionReport.TABLE_NAME)
@Table(name = TaskStepExecutionReport.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskStepExecutionReport extends AbstractExecution {
  public static final String TABLE_NAME = "task_step_execution_report";

  @Column(name = "task_execution_id")
  private Integer taskExecutionId;

  @Column(name = "step_name")
  private String stepName;

  @Column(name = "status")
  private StatusEnum status;
}
