package com.service.taskreport.enums;

public enum TaskStepExecutionReportColumnNameEnum {
  id("id"),
  taskExecutionId("taskExecutionId"),
  stepName("stepName"),
  status("status"),
  startDateTime("startDateTime"),
  endDateTime("endDateTime"),
  executionTimeSeconds("executionTimeSeconds"),
  errorMessage("errorMessage"),
  insertDate("insertDate"),
  updateDate("updateDate");

  private final String value;

  TaskStepExecutionReportColumnNameEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
