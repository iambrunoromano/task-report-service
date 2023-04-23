package com.service.taskreport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.enums.StatusEnum;
import com.service.taskreport.response.TaskExecutionReportResponse;
import com.service.taskreport.response.TaskStepExecutionReportResponse;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestUtility {

  protected static final SimpleDateFormat simpleDateFormat =
      new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

  protected static final Integer ID = 1;
  protected static final Integer FIRST_TASK_ID = 1;
  protected static final Timestamp START_DATE_TIME = getTimestamp("2023-01-01 00:00:00");
  protected static final Timestamp FIRST_END_DATE_TIME = getTimestamp("2023-01-01 00:01:00");
  protected static final Integer FIRST_EXECUTION_TIME_SECONDS = 60;
  protected static final String ERROR_MESSAGE = "error-message";
  protected static final StatusEnum STATUS = StatusEnum.SUCCESS;
  protected static final LocalDateTime INSERT_DATE = LocalDateTime.now();
  protected static final LocalDateTime UPDATE_DATE = LocalDateTime.now();

  protected static final Integer TASK_EXECUTION_ID = 1;
  protected static final String FIRST_STEP_NAME = "step_1";
  protected static final String SECOND_STEP_NAME = "step_2";

  protected static final Timestamp SECOND_END_DATE_TIME = getTimestamp("2023-01-01 00:00:59");
  protected static final Integer SECOND_EXECUTION_TIME_SECONDS = 59;

  private static Timestamp getTimestamp(String input) {
    Timestamp timestamp = Timestamp.from(Instant.now());
    try {
      timestamp = new Timestamp(simpleDateFormat.parse(input).getTime());
    } catch (ParseException parseException) {
      System.out.println(parseException.getMessage());
    }
    return timestamp;
  }

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
        .taskId(FIRST_TASK_ID)
        .startDateTime(START_DATE_TIME)
        .endDateTime(FIRST_END_DATE_TIME)
        .executionTimeSeconds(FIRST_EXECUTION_TIME_SECONDS)
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
        .stepName(FIRST_STEP_NAME)
        .status(STATUS)
        .startDateTime(START_DATE_TIME)
        .endDateTime(FIRST_END_DATE_TIME)
        .executionTimeSeconds(FIRST_EXECUTION_TIME_SECONDS)
        .errorMessage(ERROR_MESSAGE)
        .insertDate(INSERT_DATE)
        .updateDate(UPDATE_DATE)
        .build();
  }

  protected String convertToJson(ObjectMapper objectMapper, Object object) {
    String converted = "";
    try {
      converted = objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException jsonProcessingException) {
      System.out.println(jsonProcessingException.getMessage());
    }
    return converted;
  }

  protected List<TaskExecutionReportResponse> buildGetAllTaskResponses() {
    TaskExecutionReportResponse firstTaskExecutionReportResponse =
        TaskExecutionReportResponse.builder()
            .id(ID)
            .taskId(FIRST_TASK_ID)
            .startDateTime(START_DATE_TIME)
            .endDateTime(FIRST_END_DATE_TIME)
            .executionTimeSeconds(FIRST_EXECUTION_TIME_SECONDS)
            .errorMessage("")
            .status(StatusEnum.FAILURE)
            .taskStepExecutionReports(buildFirstTaskStepResponses())
            .build();
    TaskExecutionReportResponse secondTaskExecutionReportResponse =
        TaskExecutionReportResponse.builder()
            .id(ID + 1)
            .taskId(FIRST_TASK_ID + 1)
            .startDateTime(START_DATE_TIME)
            .endDateTime(SECOND_END_DATE_TIME)
            .executionTimeSeconds(SECOND_EXECUTION_TIME_SECONDS)
            .errorMessage("")
            .status(StatusEnum.SUCCESS)
            .taskStepExecutionReports(buildSecondTaskStepResponses())
            .build();
    return Arrays.asList(firstTaskExecutionReportResponse, secondTaskExecutionReportResponse);
  }

  protected List<TaskStepExecutionReportResponse> buildFirstTaskStepResponses() {
    TaskStepExecutionReportResponse firstTaskStepExecutionReportResponse =
        TaskStepExecutionReportResponse.builder()
            .id(ID)
            .taskExecutionId(ID)
            .stepName(FIRST_STEP_NAME)
            .status(StatusEnum.SUCCESS)
            .startDateTime(START_DATE_TIME)
            .endDateTime(FIRST_END_DATE_TIME)
            .executionTimeSeconds(FIRST_EXECUTION_TIME_SECONDS)
            .errorMessage("")
            .build();
    TaskStepExecutionReportResponse secondTaskStepExecutionReportResponse =
        TaskStepExecutionReportResponse.builder()
            .id(ID + 1)
            .taskExecutionId(ID)
            .stepName(SECOND_STEP_NAME)
            .status(StatusEnum.FAILURE)
            .startDateTime(START_DATE_TIME)
            .endDateTime(FIRST_END_DATE_TIME)
            .executionTimeSeconds(FIRST_EXECUTION_TIME_SECONDS)
            .errorMessage("")
            .build();
    return Arrays.asList(
        firstTaskStepExecutionReportResponse, secondTaskStepExecutionReportResponse);
  }

  protected List<TaskStepExecutionReportResponse> buildSecondTaskStepResponses() {
    TaskStepExecutionReportResponse firstTaskStepExecutionReportResponse =
        TaskStepExecutionReportResponse.builder()
            .id(ID + 2)
            .taskExecutionId(ID + 1)
            .stepName(FIRST_STEP_NAME)
            .status(StatusEnum.SUCCESS)
            .startDateTime(START_DATE_TIME)
            .endDateTime(SECOND_END_DATE_TIME)
            .executionTimeSeconds(SECOND_EXECUTION_TIME_SECONDS)
            .errorMessage("")
            .build();
    TaskStepExecutionReportResponse secondTaskStepExecutionReportResponse =
        TaskStepExecutionReportResponse.builder()
            .id(ID + 3)
            .taskExecutionId(ID + 1)
            .stepName(SECOND_STEP_NAME)
            .status(StatusEnum.SUCCESS)
            .startDateTime(START_DATE_TIME)
            .endDateTime(SECOND_END_DATE_TIME)
            .executionTimeSeconds(SECOND_EXECUTION_TIME_SECONDS)
            .errorMessage("")
            .build();
    return Arrays.asList(
        firstTaskStepExecutionReportResponse, secondTaskStepExecutionReportResponse);
  }
}
