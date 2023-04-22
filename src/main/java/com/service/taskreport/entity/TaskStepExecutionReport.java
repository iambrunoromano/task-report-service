package com.service.taskreport.entity;

import com.service.taskreport.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity(name = TaskStepExecutionReport.TABLE_NAME)
@Table(name = TaskStepExecutionReport.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskStepExecutionReport {
  public static final String TABLE_NAME = "task_step_execution_report";

  @Id
  @GeneratedValue
  @Column(name = "task_step_execution_id")
  private Integer id;

  @Column(name = "task_execution_id")
  private Integer taskExecutionId;

  @Column(name = "step_name")
  private String stepName;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private StatusEnum status;

  @Column(name = "start_date_time")
  private Timestamp startDateTime;

  @Column(name = "end_date_time")
  private Timestamp endDateTime;

  @Column(name = "execution_time_seconds")
  private Integer executionTimeSeconds;

  @Column(name = "error_message")
  private String errorMessage;

  @CreationTimestamp
  @Column(
      name = "insert_date",
      updatable = false,
      insertable = false,
      columnDefinition = " DATETIME DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime insertDate;

  @UpdateTimestamp
  @Column(
      name = "update_date",
      insertable = false,
      columnDefinition = " DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  private LocalDateTime updateDate;
}
