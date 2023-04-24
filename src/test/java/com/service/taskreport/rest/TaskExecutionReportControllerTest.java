package com.service.taskreport.rest;

import com.service.taskreport.TestUtility;
import com.service.taskreport.enums.StatusEnum;
import com.service.taskreport.exception.TaskExecutionReportNotFoundException;
import com.service.taskreport.exception.TaskStepExecutionReportBadRequestException;
import com.service.taskreport.exception.TaskStepExecutionReportNotFoundException;
import com.service.taskreport.exception.UndefinedStatusException;
import com.service.taskreport.request.TaskExecutionReportRequest;
import com.service.taskreport.response.TaskExecutionReportResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class TaskExecutionReportControllerTest extends TestUtility {

  private final TaskExecutionReportController taskExecutionReportController;

  @Autowired
  TaskExecutionReportControllerTest(TaskExecutionReportController taskExecutionReportController) {
    this.taskExecutionReportController = taskExecutionReportController;
  }

  @Test
  @Sql("classpath:task/get_all.sql")
  void getAllTest() {
    ResponseEntity<List<TaskExecutionReportResponse>> actualResponse =
        taskExecutionReportController.getAll();
    assertEquals(buildGetAllTaskResponses(), actualResponse.getBody());
    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
  }

  @Test
  @Sql("classpath:task/get_by_status.sql")
  void getAllByStatusTest()
      throws TaskExecutionReportNotFoundException, TaskStepExecutionReportBadRequestException,
          TaskStepExecutionReportNotFoundException {
    ResponseEntity<List<TaskExecutionReportResponse>> actualResponse =
        taskExecutionReportController.getAllByStatus(StatusEnum.FAILURE);
    assertEquals(
        Arrays.asList(buildFirstTaskExecutionReportResponse(SECOND_ID, SECOND_TASK_ID)),
        actualResponse.getBody());
    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
  }

  @Test
  @Sql("classpath:task/get_by_status_task_not_found.sql")
  void getAllByStatusTaskNotFoundTest() {
    TaskExecutionReportNotFoundException actualException =
        assertThrows(
            TaskExecutionReportNotFoundException.class,
            () -> {
              taskExecutionReportController.getAllByStatus(StatusEnum.FAILURE);
            });
    assertEquals(
        String.format("TaskExecutionReport for status [%s] not found", StatusEnum.FAILURE),
        actualException.getMessage());
  }

  @Test
  @Sql("classpath:task/get_by_status_task_step_not_found.sql")
  void getAllByStatusTaskStepNotFoundTest() {
    TaskStepExecutionReportNotFoundException actualException =
        assertThrows(
            TaskStepExecutionReportNotFoundException.class,
            () -> {
              taskExecutionReportController.getAllByStatus(StatusEnum.FAILURE);
            });
    assertEquals(
        String.format(
            "TaskStepExecutionReport for taskExecutionReportList with ids [%s] not found",
            Collections.singletonList(THIRD_TASK_ID)),
        actualException.getMessage());
  }

  @Test
  @Sql("classpath:task/get_all_order_by_execution_time.sql")
  void getAllOrderByExecutionTimeTest()
      throws TaskStepExecutionReportBadRequestException, TaskStepExecutionReportNotFoundException {
    ResponseEntity<List<TaskExecutionReportResponse>> actualResponse =
        taskExecutionReportController.getAllOrderByExecutionTime();
    assertEquals(buildGetAllOrderByExecutionTime(), actualResponse.getBody());
    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
  }

  @Test
  @Sql("classpath:task/get_all_order_by_execution_time_task_step_not_found.sql")
  void getAllOrderByExecutionTimeTaskStepNotFoundTest() {
    TaskStepExecutionReportNotFoundException actualException =
        assertThrows(
            TaskStepExecutionReportNotFoundException.class,
            taskExecutionReportController::getAllOrderByExecutionTime);
    assertEquals(
        String.format(
            "TaskStepExecutionReport for taskExecutionReportList with ids [%s] not found",
            Collections.singletonList(FOURTH_ID)),
        actualException.getMessage());
  }

  @Test
  @Sql("classpath:task/get_by_id.sql")
  void getByIdTest()
      throws TaskStepExecutionReportNotFoundException, TaskExecutionReportNotFoundException {
    ResponseEntity<TaskExecutionReportResponse> actualResponse =
        taskExecutionReportController.getById(FIFTH_ID);
    assertEquals(
        buildFirstTaskExecutionReportResponse(FIFTH_ID, FIFTH_TASK_ID), actualResponse.getBody());
    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
  }

  @Test
  @Sql("classpath:task/get_by_id_task_not_found.sql")
  void getByIdTaskNotFoundTest() {
    TaskExecutionReportNotFoundException actualException =
        assertThrows(
            TaskExecutionReportNotFoundException.class,
            () -> {
              taskExecutionReportController.getById(FIFTH_ID);
            });
    assertEquals(
        String.format("TaskExecutionReport for id [%s] not found", FIFTH_ID),
        actualException.getMessage());
  }

  @Test
  @Sql("classpath:task/get_by_id_task_step_not_found.sql")
  void getByIdTaskStepNotFoundTest() {
    TaskStepExecutionReportNotFoundException actualException =
        assertThrows(
            TaskStepExecutionReportNotFoundException.class,
            () -> {
              taskExecutionReportController.getById(SIXTH_ID);
            });
    assertEquals(
        String.format(
            "TaskStepExecutionReport for taskExecutionReportList with ids [%s] not found",
            SIXTH_ID),
        actualException.getMessage());
  }

  @Test
  @Sql("classpath:task/create.sql")
  void createTest() throws TaskStepExecutionReportNotFoundException, UndefinedStatusException {
    ResponseEntity<TaskExecutionReportResponse> actualResponse =
        taskExecutionReportController.create(
            buildTaskExecutionReportRequest(SEVENTH_ID, SEVENTH_TASK_ID));
    assertEquals(
        buildFirstTaskExecutionReportResponse(SEVENTH_ID, SEVENTH_TASK_ID),
        actualResponse.getBody());
    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
  }

  @Test
  @Sql("classpath:task/create.sql")
  void createUndefinedStatusTest() {
    TaskExecutionReportRequest taskExecutionReportRequest =
        buildTaskExecutionReportRequest(SEVENTH_ID, SEVENTH_TASK_ID);
    taskExecutionReportRequest.setTaskStepExecutionReports(new ArrayList<>());
    UndefinedStatusException actualException =
        assertThrows(
            UndefinedStatusException.class,
            () -> {
              taskExecutionReportController.create(taskExecutionReportRequest);
            });
    assertEquals(
        String.format("Status for TaskExecutionReport with id [%s] is undefined", SEVENTH_ID),
        actualException.getMessage());
  }
}
