package com.service.taskreport.enums;

public enum TaskStepExecutionReportColumnNameEnum {
  ID("id"),
  TASK_EXECUTION_ID("taskExecutionId"),
  STEP_NAME("stepName"),
  STATUS("status"),
  START_DATE_TIME("startDateTime"),
  END_DATE_TIME("endDateTime"),
  EXECUTION_TIME_SECONDS("executionTimeSeconds"),
  ERROR_MESSAGE("errorMessage"),
  INSERT_DATE("insertDate"),
  UPDATE_DATE("updateDate");

  private final String value;

  TaskStepExecutionReportColumnNameEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
