package com.service.taskreport;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.enums.StatusEnum;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestUtility {

  protected static final Integer ID = 1;
  protected static final Integer TASK_ID = 1;
  protected static final Timestamp START_DATE_TIME = Timestamp.from(Instant.now());
  protected static final Timestamp END_DATE_TIME = Timestamp.from(Instant.now());
  protected static final Integer EXECUTION_TIME_SECONDS = 60;
  protected static final String ERROR_MESSAGE = "error-message";
  protected static final StatusEnum STATUS = StatusEnum.SUCCESS;
  protected static final LocalDateTime INSERT_DATE = LocalDateTime.now();
  protected static final LocalDateTime UPDATE_DATE = LocalDateTime.now();

  protected static final Integer TASK_EXECUTION_ID = 1;
  protected static final String STEP_NAME = "step-name";

  protected static List<TaskExecutionReport> buildTaskExecutionReportList() {
    List<TaskExecutionReport> taskExecutionReportList = new ArrayList<>();
    taskExecutionReportList.add(buildTaskExecutionReport());
    taskExecutionReportList.add(buildTaskExecutionReport());
    return taskExecutionReportList;
  }

  protected static List<TaskStepExecutionReport> buildTaskStepExecutionReportList() {
    List<TaskStepExecutionReport> taskStepExecutionReportList = new ArrayList<>();
    taskStepExecutionReportList.add(buildTaskStepExecutionReport());
    taskStepExecutionReportList.add(buildTaskStepExecutionReport());
    return taskStepExecutionReportList;
  }

  protected static TaskExecutionReport buildTaskExecutionReport() {
    return TaskExecutionReport.builder()
        .id(ID)
        .taskId(TASK_ID)
        .startDateTime(START_DATE_TIME)
        .endDateTime(END_DATE_TIME)
        .executionTimeSeconds(EXECUTION_TIME_SECONDS)
        .errorMessage(ERROR_MESSAGE)
        .status(STATUS)
        .insertDate(INSERT_DATE)
        .updateDate(UPDATE_DATE)
        .build();
  }

  protected static TaskStepExecutionReport buildTaskStepExecutionReport() {
    return TaskStepExecutionReport.builder()
        .id(ID)
        .taskExecutionId(TASK_EXECUTION_ID)
        .stepName(STEP_NAME)
        .status(STATUS)
        .startDateTime(START_DATE_TIME)
        .endDateTime(END_DATE_TIME)
        .executionTimeSeconds(EXECUTION_TIME_SECONDS)
        .errorMessage(ERROR_MESSAGE)
        .insertDate(INSERT_DATE)
        .updateDate(UPDATE_DATE)
        .build();
  }
}
