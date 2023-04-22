package com.service.taskreport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = TaskExecutionReport.TABLE_NAME)
@Table(name = TaskExecutionReport.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskExecutionReport extends AbstractExecution {
  public static final String TABLE_NAME = "task_execution_report";

  @Column(name = "task_id")
  private Integer taskId;

  @Column(name = "task_step_execution_reports")
  private List<TaskStepExecutionReport> taskStepExecutionReports;
}
