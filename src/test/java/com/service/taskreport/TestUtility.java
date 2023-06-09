package com.service.taskreport;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.enums.StatusEnum;
import com.service.taskreport.request.TaskExecutionReportRequest;
import com.service.taskreport.request.TaskStepExecutionReportRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

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

  protected static final Timestamp SECOND_END_DATE_TIME = getTimestamp("2023-01-01 00:00:59");
  protected static final Integer SECOND_EXECUTION_TIME_SECONDS = 59;

  protected static final Integer SECOND_ID = 3;
  protected static final Integer SECOND_TASK_ID = 3;

  protected static final Integer THIRD_TASK_ID = 5;

  protected static final Integer THIRD_ID = 6;
  protected static final Integer FOURTH_TASK_ID = 6;

  protected static final Integer FOURTH_ID = 8;

  protected static final Integer FIFTH_ID = 9;
  protected static final Integer FIFTH_TASK_ID = 9;

  protected static final Integer SIXTH_ID = 11;

  protected static final Integer SEVENTH_ID = 12;
  protected static final Integer SEVENTH_TASK_ID = 12;

  protected static final Integer EIGHTH_ID = 13;

  protected static final String SECOND_STEP_NAME = "step_2";

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

  protected List<TaskExecutionReportResponse> buildGetAllTaskResponses() {
    TaskExecutionReportResponse firstTaskExecutionReportResponse =
        buildFirstTaskExecutionReportResponse(ID, FIRST_TASK_ID);
    TaskExecutionReportResponse secondTaskExecutionReportResponse =
        buildSecondTaskExecutionReportResponse(ID, FIRST_TASK_ID);
    return Arrays.asList(firstTaskExecutionReportResponse, secondTaskExecutionReportResponse);
  }

  protected List<TaskStepExecutionReportResponse> buildGetAllTaskStepResponses() {
    List<TaskStepExecutionReportResponse> firstTaskStepExecutionReportResponse =
        buildFirstTaskStepResponses(ID);
    List<TaskStepExecutionReportResponse> secondTaskStepExecutionReportResponse =
        buildSecondTaskStepResponses(ID + 1);
    List<TaskStepExecutionReportResponse> allTaskStepExecutionReportResponse = new ArrayList<>();
    allTaskStepExecutionReportResponse.addAll(firstTaskStepExecutionReportResponse);
    allTaskStepExecutionReportResponse.addAll(secondTaskStepExecutionReportResponse);
    return allTaskStepExecutionReportResponse;
  }

  protected List<TaskExecutionReportResponse> buildGetAllOrderByExecutionTime() {
    TaskExecutionReportResponse firstTaskExecutionReportResponse =
        buildFirstTaskExecutionReportResponse(THIRD_ID, FOURTH_TASK_ID);
    TaskExecutionReportResponse secondTaskExecutionReportResponse =
        buildSecondTaskExecutionReportResponse(THIRD_ID, FOURTH_TASK_ID);
    return Arrays.asList(firstTaskExecutionReportResponse, secondTaskExecutionReportResponse);
  }

  private TaskExecutionReportResponse buildSecondTaskExecutionReportResponse(
      Integer id, Integer taskId) {
    Integer secondId = id + 1;
    Integer secondTaskId = taskId + 1;
    return TaskExecutionReportResponse.builder()
        .id(secondId)
        .taskId(secondTaskId)
        .startDateTime(START_DATE_TIME)
        .endDateTime(SECOND_END_DATE_TIME)
        .executionTimeSeconds(SECOND_EXECUTION_TIME_SECONDS)
        .errorMessage("")
        .status(StatusEnum.SUCCESS)
        .taskStepExecutionReports(buildSecondTaskStepResponses(secondId))
        .build();
  }

  protected TaskExecutionReportResponse buildFirstTaskExecutionReportResponse(
      Integer id, Integer taskId) {
    return TaskExecutionReportResponse.builder()
        .id(id)
        .taskId(taskId)
        .startDateTime(START_DATE_TIME)
        .endDateTime(FIRST_END_DATE_TIME)
        .executionTimeSeconds(FIRST_EXECUTION_TIME_SECONDS)
        .errorMessage("")
        .status(StatusEnum.FAILURE)
        .taskStepExecutionReports(buildFirstTaskStepResponses(id))
        .build();
  }

  protected List<TaskStepExecutionReportResponse> buildFirstTaskStepResponses(Integer id) {
    TaskStepExecutionReportResponse firstTaskStepExecutionReportResponse =
        TaskStepExecutionReportResponse.builder()
            .id(id)
            .taskExecutionId(id)
            .stepName(FIRST_STEP_NAME)
            .status(StatusEnum.SUCCESS)
            .startDateTime(START_DATE_TIME)
            .endDateTime(FIRST_END_DATE_TIME)
            .executionTimeSeconds(FIRST_EXECUTION_TIME_SECONDS)
            .errorMessage("")
            .build();
    return Arrays.asList(firstTaskStepExecutionReportResponse);
  }

  protected List<TaskStepExecutionReportResponse> buildSecondTaskStepResponses(Integer id) {
    TaskStepExecutionReportResponse firstTaskStepExecutionReportResponse =
        TaskStepExecutionReportResponse.builder()
            .id(id)
            .taskExecutionId(id)
            .stepName(FIRST_STEP_NAME)
            .status(StatusEnum.SUCCESS)
            .startDateTime(START_DATE_TIME)
            .endDateTime(SECOND_END_DATE_TIME)
            .executionTimeSeconds(SECOND_EXECUTION_TIME_SECONDS)
            .errorMessage("")
            .build();
    return Arrays.asList(firstTaskStepExecutionReportResponse);
  }

  protected TaskExecutionReportRequest buildTaskExecutionReportRequest(Integer id, Integer taskId) {
    TaskStepExecutionReportRequest taskStepExecutionReportRequest =
        buildTaskStepExecutionReportRequest(id);
    return TaskExecutionReportRequest.builder()
        .taskId(taskId)
        .taskStepExecutionReports(Arrays.asList(taskStepExecutionReportRequest))
        .startDateTime(START_DATE_TIME)
        .endDateTime(FIRST_END_DATE_TIME)
        .errorMessage("")
        .build();
  }

  protected TaskStepExecutionReportRequest buildTaskStepExecutionReportRequest(Integer id) {
    return TaskStepExecutionReportRequest.builder()
        .taskExecutionId(id)
        .stepName(FIRST_STEP_NAME)
        .status(StatusEnum.SUCCESS)
        .startDateTime(START_DATE_TIME)
        .endDateTime(FIRST_END_DATE_TIME)
        .errorMessage("")
        .build();
  }

  protected static void assertTaskCreateTestEquals(
      TaskExecutionReportResponse expectedTaskResponse,
      TaskExecutionReportResponse actualTaskResponse) {
    assertEquals(expectedTaskResponse.getTaskId(), actualTaskResponse.getTaskId());
    assertEquals(expectedTaskResponse.getStartDateTime(), actualTaskResponse.getStartDateTime());
    assertEquals(expectedTaskResponse.getEndDateTime(), actualTaskResponse.getEndDateTime());
    assertEquals(
        expectedTaskResponse.getExecutionTimeSeconds(),
        actualTaskResponse.getExecutionTimeSeconds());
    assertEquals(expectedTaskResponse.getErrorMessage(), actualTaskResponse.getErrorMessage());
    assertEquals(expectedTaskResponse.getStatus(), actualTaskResponse.getStatus());
    for (Integer i = 0; i < actualTaskResponse.getTaskStepExecutionReports().size(); ++i) {
      assertTaskStepCreateTestEquals(
          expectedTaskResponse.getTaskStepExecutionReports().get(i),
          actualTaskResponse.getTaskStepExecutionReports().get(i));
    }
  }

  protected static void assertTaskStepCreateTestEquals(
      TaskStepExecutionReportResponse expectedTaskStepResponse,
      TaskStepExecutionReportResponse actualTaskStepResponse) {
    assertEquals(
        expectedTaskStepResponse.getTaskExecutionId(), actualTaskStepResponse.getTaskExecutionId());
    assertEquals(expectedTaskStepResponse.getStepName(), actualTaskStepResponse.getStepName());
    assertEquals(expectedTaskStepResponse.getStatus(), actualTaskStepResponse.getStatus());
    assertEquals(
        expectedTaskStepResponse.getStartDateTime(), actualTaskStepResponse.getStartDateTime());
    assertEquals(
        expectedTaskStepResponse.getEndDateTime(), actualTaskStepResponse.getEndDateTime());
    assertEquals(
        expectedTaskStepResponse.getExecutionTimeSeconds(),
        actualTaskStepResponse.getExecutionTimeSeconds());
    assertEquals(
        expectedTaskStepResponse.getErrorMessage(), actualTaskStepResponse.getErrorMessage());
  }

  protected List<TaskStepExecutionReportResponse> buildGetByTaskExecutionIdResponses(Integer id) {
    TaskStepExecutionReportResponse firstTaskStepExecutionReportResponse =
        TaskStepExecutionReportResponse.builder()
            .id(id + 1)
            .taskExecutionId(id)
            .stepName(SECOND_STEP_NAME)
            .status(StatusEnum.SUCCESS)
            .startDateTime(START_DATE_TIME)
            .endDateTime(SECOND_END_DATE_TIME)
            .executionTimeSeconds(SECOND_EXECUTION_TIME_SECONDS)
            .errorMessage("")
            .build();
    TaskStepExecutionReportResponse secondTaskStepExecutionReportResponse =
        TaskStepExecutionReportResponse.builder()
            .id(id)
            .taskExecutionId(id)
            .stepName(FIRST_STEP_NAME)
            .status(StatusEnum.SUCCESS)
            .startDateTime(START_DATE_TIME)
            .endDateTime(FIRST_END_DATE_TIME)
            .executionTimeSeconds(FIRST_EXECUTION_TIME_SECONDS)
            .errorMessage("")
            .build();
    return Arrays.asList(
        firstTaskStepExecutionReportResponse, secondTaskStepExecutionReportResponse);
  }
}
