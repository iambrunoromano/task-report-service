package com.service.taskreport.enums;

public enum TaskStepExecutionReportColumnNameEnum {
  id("id"),
  taskExecutionId("task_execution_id"),
  stepName("step_name"),
  status("status"),
  startDateTime("start_date_time"),
  endDateTime("end_date_time"),
  executionTimeSeconds("execution_time_seconds"),
  errorMessage("error_message"),
  insertDate("insert_date"),
  updateDate("update_date");

  private final String value;

  TaskStepExecutionReportColumnNameEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
